package arquivos;

import aeds3.Arquivo;
import aeds3.ArvoreBMais;
import aeds3.HashExtensivel;
import aeds3.ListaInvertida;
import aeds3.ParIntInt;
import entidades.Livro;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

public class ArquivoLivros extends Arquivo<Livro> {

  HashExtensivel<ParIsbnId> indiceIndiretoISBN;
  ArvoreBMais<ParIntInt> relLivrosDaCategoria;

  // Cria uma lista invertida
  ListaInvertida lista;

  public ArquivoLivros() throws Exception {
    super("livros", Livro.class.getConstructor());
    indiceIndiretoISBN = new HashExtensivel<>(
        ParIsbnId.class.getConstructor(),
        4,
        "dados/livros_isbn.hash_d.db",
        "dados/livros_isbn.hash_c.db");
    relLivrosDaCategoria = new ArvoreBMais<>(ParIntInt.class.getConstructor(), 4, "dados/livros_categorias.btree.db");

    // Inicializa a lista invertida
    lista = new ListaInvertida();

  }

  // Métodos para o tratamento de palavras para adicionar na lista invertida

  // Lista de stop words em português
  private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
      "a", "o", "e", "de", "do", "da", "dos", "das", "em", "no", "na", "nos", "nas", "um", "uma", "uns", "umas",
      // Adicione mais palavras conforme necessário
      "por", "para", "com", "sem"));

  public static String removeStopWords(String text) {
    StringBuilder result = new StringBuilder();

    // Divide a string em palavras
    String[] words = text.split("\\s+");

    // Itera sobre as palavras e adiciona à saída se não for uma stop word
    for (String word : words) {
      word = deAccent(word.toLowerCase());
      if (!STOP_WORDS.contains(word)) {
        result.append(word).append(" ");
      }
    }

    // Remove o espaço extra no final
    return result.toString().trim();
  }

  // Metodo que tira os acentos
  public static String deAccent(String str) {
    String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    return pattern.matcher(nfdNormalizedString).replaceAll("");
  }

  /*
   * Método que remove os acentos e tira as letras maiúsculas e
   * retorna um arraylist com as palavras chave
   */
  public static ArrayList<String> tratamento(String t) {

    t = removeStopWords(t);
    String[] split = t.split(" ");
    ArrayList<String> resp = new ArrayList<>();
    for (String w : split) {
      resp.add(w);
    }
    return resp;

  }

  @Override
  public int create(Livro obj) throws Exception {
    int id = super.create(obj);
    obj.setID(id);
    indiceIndiretoISBN.create(new ParIsbnId(obj.getIsbn(), obj.getID()));
    relLivrosDaCategoria.create(new ParIntInt(obj.getIdCategoria(), obj.getID()));
    // lista de chaves que serão adicionadas a lista
    ArrayList<String> chaves = tratamento(obj.getTitulo());
    for (String s : chaves) {
      lista.create(s, obj.getID());
    }

    return id;
  }

  public Livro readISBN(String isbn) throws Exception {
    ParIsbnId pii = indiceIndiretoISBN.read(ParIsbnId.hashIsbn(isbn));
    if (pii == null)
      return null;
    int id = pii.getId();
    return super.read(id);
  }

  @Override
  public boolean delete(int id) throws Exception {
    Livro obj = super.read(id);
    if (obj != null)
      if (indiceIndiretoISBN.delete(ParIsbnId.hashIsbn(obj.getIsbn()))
          &&
          relLivrosDaCategoria.delete(new ParIntInt(obj.getIdCategoria(), obj.getID()))) {

        // Cria um arraylist de chaves e chama o metodo de exclusão da lista invertida
        ArrayList<String> chaves = tratamento(obj.getTitulo());
        for (String s : chaves) {
          lista.delete(s, id);
        }
        return super.delete(id);
      }
    return false;
  }

  @Override
  public boolean update(Livro novoLivro) throws Exception {
    Livro livroAntigo = super.read(novoLivro.getID());
    if (livroAntigo != null) {

      // Primeiro remove as chaves do titulo antigo para não ter chave duplicada
      ArrayList<String> chaves = tratamento(livroAntigo.getTitulo());
      for (String s : chaves) {
        lista.delete(s, livroAntigo.getID());
      }
      chaves.clear();

      // Adiciona as chaves novas
      chaves = tratamento(novoLivro.getTitulo());
      for (String s : chaves) {
        lista.create(s, novoLivro.getID());
      }
      // Atualiza o livro
      return super.update(novoLivro);
    }
    return false;
  }

  
  /*
   * O método de busca basicamente faz o tratamento do texto que o usuario inserir
   * cria um arraylist das chaves caso tenha mais de uma palavra na busca e pega a
   * interseção dos ids retornados para retornar os livros encontrados
   */
  public void busca(String s) throws Exception {
    ArrayList<String> chaves = tratamento(s);
    // lê a primeira chave para poder comparar com as outras
    int[] ids = lista.read(chaves.get(0));
    Livro livro = new Livro();

    for (String c : chaves) {

      ids = intersecao(ids, lista.read(c));
    }

    if (ids.length <= 0 || livro == null) {
      System.out.println("\nTermos não encontrados");
      return;
    }

    
    // loop que printa na tela os dados de todos os livros encontrados
    for (int i = 0; i < ids.length; i++) {

      livro = super.read(ids[i]);
      System.out.println("-----------------------------");
      System.out.println(livro.toString());
    }

  }

  // Método que pega a intersecao entre
  public static int[] intersecao(int[] array1, int[] array2) {
    // Ordena os arrays para facilitar a interseção
    Arrays.sort(array1);
    Arrays.sort(array2);

    int tamanhoIntersecao = Math.min(array1.length, array2.length);
    int[] resultado = new int[tamanhoIntersecao];

    int indiceResultado = 0;
    int indiceArray1 = 0;
    int indiceArray2 = 0;

    // Loop para encontrar elementos comuns
    while (indiceArray1 < array1.length && indiceArray2 < array2.length) {
      if (array1[indiceArray1] == array2[indiceArray2]) {
        resultado[indiceResultado++] = array1[indiceArray1];
        indiceArray1++;
        indiceArray2++;
      } else if (array1[indiceArray1] < array2[indiceArray2]) {
        indiceArray1++;
      } else {
        indiceArray2++;
      }
    }

    // Redimensiona o array resultante para remover elementos não utilizados
    return Arrays.copyOf(resultado, indiceResultado);
  }

}

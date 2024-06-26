import java.io.*;

import codigo.Arquivo;
import codigo.Livro;

class Principal {

  public static void main(String args[]) {

    new File("dados/livros.db").delete();
    new File("dados/livros.hash_d.db").delete();
    new File("dados/livros.hash_c.db").delete();

    Arquivo<Livro> arqLivros;
    Livro l1 = new Livro(-1, "9788576573135", "Duna", 79.90F);
    Livro l2 = new Livro(-1, "9788556510785", "A Guerra dos Tronos : As Crônicas de Gelo e Fogo, volume 1", 71.94F);
    Livro l3 = new Livro(-1, "9788501014863", "O estrangeiro", 35.85F);
    Livro l4 = new Livro(-1, "9788535909555", "Revolução dos bichos", 13.98F);
    Livro l5 = new Livro(-1, "9788525056009", "Admirável mundo novo", 44.9F);
    int id1, id2, id3, id4, id5;

    try {
      arqLivros = new Arquivo<>("livros", Livro.class.getConstructor());

      id1 = arqLivros.create(l1);
      System.out.println("Livro criado com o ID: " + id1);

      id2 = arqLivros.create(l2);
      System.out.println("Livro criado com o ID: " + id2);

      id3 = arqLivros.create(l3);
      System.out.println("Livro criado com o ID: " + id3);

      id4 = arqLivros.create(l4);
      System.out.println("Livro criado com o ID: " + id4);
      
      if (arqLivros.delete(id3))
        System.out.println("Livro de ID " + id3 + " excluído!");
      else
        System.out.println("Livro de ID " + id3 + " não encontrado!");

      id5 = arqLivros.create(l5);
      System.out.println("Livro criado com o ID: " + id5);

      if (arqLivros.delete(id2))
        System.out.println("Livro de ID " + id2 + " excluído!");
      else
        System.out.println("Livro de ID " + id2 + " não encontrado!");

      l4.setTitulo("A revolução dos bichos");
      if (arqLivros.update(l4))
        System.out.println("Livro de ID " + l4.getID() + " alterado!");
      else
        System.out.println("Livro de ID " + l4.getID() + " não encontrado!");

      

      
      System.out.println("\nLivro 1:\n" + arqLivros.read(1));
      System.out.println("\nLivro 2:\n" + arqLivros.read(2));
      System.out.println("\nLivro 3:\n" + arqLivros.read(3));
      System.out.println("\nLivro 4:\n" + arqLivros.read(4));
      System.out.println("\nLivro 5:\n" + arqLivros.read(5));
      
      
      arqLivros.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}

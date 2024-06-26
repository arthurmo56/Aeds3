# Trabalho Prático 2 - Busca por Palavras

**Trabalho feito por:** Arthur Martinho M. Oliveira e Rafael Maluf Araujo

**Descrição do trabalho:** Baseado no que vimos em sala de aula, desenvolvemos um código que adiciona uma lista invertida em um banco de dados de livros. A primeira coisa que fizemos foi adicionar a classe da lista invertida no diretorio( dentro da pasta aeds3), em seguida implementamos os metodos que iriam tratar das palavras: o "RemoveStopWords" que tira as stop words que definimos no codigo e o "deAccent" que remove os acentos usando o Normalizer, Ambos os metódos são usados na função "tratamento" que vai tratar um titulo e retornar um ArrayList de chaves para a lista invertida,tudo presente na classe ArquivoLivro. Por fim alteramos os metodos de inserir que vai pegar o titulo, gerar as chaves e inserir na lista invertida passando o id e as chaves, o remover que tambem vai gerar as chaves e remover na lista passando o id e as chaves e o update que vai gerar as chaves do titulo antigo, remover na lista e inserir as chaves geradas pelo  titulo novo. Criamos o metódo busca que vai pegar o texto digitado pelo usuario, fazer o tratamento dessas chaves e pegar a intersecção das chaves e printar na tela os dados dos livros encontrados.

**Experiência do grupo:** Achamos o trabalho em geral bem tranquilo, Implementamos os metódos sem muita dificuldade. O unico problema que tivemos foi a questão do encoding, quando uma string era lida por scanner as palavras com acento eram substituidas por '?' oque fazia o metodo de tirar os acentos não funcionar e passar as chaves com erros para a lista invertida, tentamos varias coisas: mudar o encoding do scanner, terminal, tentamos outros metódos de leitura, alteramos os metódos varias vezes. Até que decidimos testar o codigo em uma maquina linux, e o mesmo codigo que estava dando esse erro funcionou perfeitamente, ai percebemos que o problema estava na configuração do nosso vscode.

**Checklist:**
- A inclusão de um livro acrescenta os termos do seu título à lista invertida.
- A alteração de um livro modifica a lista invertida removendo ou acrescentando termos do título.
- A remoção de um livro gera a remoção dos termos do seu título na lista invertida.
- Há uma busca por palavras que retorna os livros que possuam essas palavras.
- Essa busca pode ser feita com mais de uma palavra.
- As stop words foram removidas de todo o processo.
- Apenas os requisitos mínimos desta tarefa.
- O trabalho está funcionando corretamente.
- O trabalho está completo.
- O trabalho é original e não a cópia de um trabalho de um colega.

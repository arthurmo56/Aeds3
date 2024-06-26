# Trabalho Prático 1 - Espaços Vazios

**Trabalho feito por:** Arthur Martinho M. Oliveira e Rafael Maluf Araujo

**Descrição do trabalho:** Baseado no que vimos em sala de aula, desenvolvemos um código para registros de livros, lidando com espaços vazios em arquivos excluídos e atualizados. Criamos uma lista de endereços usando ArrayList. Assim, toda vez que um arquivo for excluído ou aumentar de tamanho, um elemento é adicionado à lista. Utilizamos essa lista para procurar espaços que possam ser reutilizados. O método realiza um "seek" no ponteiro armazenado, pula um byte (que é a "lapide") e lê o valor short. Se esse número for menor ou igual ao tamanho do registro que será escrito, retorna o ponteiro para sobrescrever. Caso não encontre nenhum espaço com tamanho igual ou menor, retorna o final do arquivo para escrever nele.

**Experiência do grupo:** A ideia inicial era implementar um índice direto, porém, tivemos muita dificuldade em implementar nossas ideias e entender os códigos disponibilizados durante as aulas. Com isso, optamos por algo mais simples, que é a lista de ponteiros. Foi mais fácil e intuitivo de implementar, e conseguimos alcançar os resultados desejados com êxito.

**Checklist:**
- Consideramos como perda aceitável para o reuso de espaços vazios a ausência de espaços maiores ou iguais ao tamanho do registro que será adicionado, então o registro é acrescentado ao final do arquivo.
- O código do CRUD com arquivos de tipos genéricos está funcionando corretamente.
- O CRUD tem um índice direto implementado com a tabela hash extensível.
- A operação de inclusão busca o espaço vazio mais adequado para o novo registro antes de acrescentá-lo ao fim do arquivo.
- A operação de alteração busca o espaço vazio mais adequado para o registro quando ele cresce de tamanho antes de acrescentá-lo ao fim do arquivo.
- As operações de alteração (quando for o caso) e de exclusão estão gerenciando os espaços vazios para que possam ser reaproveitados.
- O trabalho está funcionando corretamente.
- O trabalho está completo.
- O trabalho é original e não a cópia de um trabalho de um colega.

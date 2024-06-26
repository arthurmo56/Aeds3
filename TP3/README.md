# Trabalho Prático 3 - Backup Compactado

**Trabalho feito por:** Arthur Martinho M. Oliveira e Rafael Maluf Araujo

**Descrição do trabalho:** Para a realização deste trabalho, pegamos o código que havíamos feito para o TP2, adicionamos a pasta com o algoritmo de LZW e criamos o arquivo Menubackup.java, que irá gerenciar e realizar os backups. O Menubackup tem como atributo um ArrayList com os nomes dos arquivos, que ao ser chamado pelo construtor no principal, adiciona o nome de todos os arquivos da pasta "dados".

O método que realiza o backup começa armazenando a data e hora, que será o nome da pasta do backup. Em seguida, ele vai compactando e armazenando um por um nessa pasta. Optamos por ler o arquivo em blocos de 8 KB por vez. Depois de compactar todos os arquivos, o método atualiza um arquivo de texto com o nome da pasta.

O método de recuperação mostra para o usuário todas as versões disponíveis. Após o usuário selecionar a opção desejada, o sistema faz um backup dos dados atuais e os apaga para serem substituídos. Depois de descompactar todos os arquivos, o método remove o nome do backup do arquivo de texto. Basicamente, este processo substitui a versão atual pelos dados do backup selecionado.

**Experiência do grupo:** Em geral o trabalho foi bem tranquilo, criar o MenuBackup do zero foi um pouco trabalhoso, mais nada muito dificil.

**Checklist:**
- Há uma rotina de compactação usando o algoritmo LZW para fazer backup dos arquivos
- Há uma rotina de descompactação usando o algoritmo LZW para recuperação dos arquivos
- O usuário pode escolher a versão a recuperar
- A taxa de compressão alcançada por esse backup foi de 50%, os arquivos eram pequenos, se fossem maiores a taxa seria melhor
- O trabalho está funcionando corretamente
- O trabalho está completo
- O trabalho é original e não a cópia de um trabalho de um colega

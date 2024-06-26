import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MenuBackup {

    Scanner console = new Scanner(System.in, "UTF-8");
    ArrayList<String> arquivos;

    MenuBackup() {

        // Cria um arraylist com o nome de todos os arquivos, ja que eles sempre serão
        // os mesmos
        this.arquivos = new ArrayList<String>();
        arquivos.addAll(Arrays.asList("autores", "autores.hash_c", "autores.hash_d", "blocos.listainv", "categorias",
                "categorias.hash_c", "categorias.hash_d", "dicionario.listainv", "livros_categorias.btree",
                "livros_isbn.hash_c", "livros_isbn.hash_d", "livros", "livros.hash_c", "livros.hash_d"));
    }

    public void menu() {

        // Mostra o menu
        int opcao;
        do {
            System.out.println("\n\n\nBOOKAEDS 1.0");
            System.out.println("------------");
            System.out.println("\n> Início > Backup");
            System.out.println("\n1) Fazer Backup");
            System.out.println("2) Recuperar Backup");
            System.out.println("\n0) Retornar ao menu anterior");

            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(console.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            // Seleciona a operação
            switch (opcao) {
                case 1:
                    Backup();
                    break;
                case 2:
                    Recuperar();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        } while (opcao != 0);
    }

    public void Backup() {

        // Obtém a data e hora atuais para o nome da pasta de backup
        LocalDateTime now = LocalDateTime.now();
        // Define o formato da data e hora para usar como nome do diretório
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String formattedDateTime = now.format(formatter);
        for (int i = 0; i < arquivos.size(); i++) {
            // chama p metodo com parametros para a criação dos arquivos compactados, um por
            // um
            Backup(arquivos.get(i), formattedDateTime);
        }

        // atualiza o txt da pasta de backup para manter um controle das versoes
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("backup/versoes.txt", true))) {
            writer.write(formattedDateTime + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void Backup(String path, String date) {

        // caminho do arquivo compactado
        File file = new File("backup/" + date + "/" + path + ".lzw");

        // Verifica se o diretório pai existe, se não, cria
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        try (FileInputStream fis = new FileInputStream("dados/" + path + ".db");
                FileOutputStream fos = new FileOutputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                BufferedOutputStream bos = new BufferedOutputStream(fos)) {

            // vamos ler de 8 em 8kb do arquivo original
            byte[] buffer = new byte[8 * 1024]; // 8 KB
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                byte[] dataToCompress = new byte[bytesRead];
                System.arraycopy(buffer, 0, dataToCompress, 0, bytesRead);

                // chama o metodo de codificar do lzw e escreve no arquivo
                byte[] compressedData = LZW.codifica(dataToCompress);
                bos.write(compressedData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Recuperar() {
        // armazena cada linha do txt em um arraylist para o usuario escolher qual versao deseja
        ArrayList<String> versoes = new ArrayList<>();
        System.out.println("Versões: (ano-mes-dia_hora-min-seg)");
        try (BufferedReader reader = new BufferedReader(new FileReader("backup/versoes.txt"))) {
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                count++;
                System.out.println(count + ") " + line);
                versoes.add(line);
            }
            int opcao;
            do {
                opcao = console.nextInt();
            } while (opcao > count || opcao < 1); // não permite selecionar opções invalidas

            // faz backup atual antes de substituir os arquivos
            Backup();
            // deleta todos os arquivos da pasta dados para substituilos
            deleteDirectory(new File("dados"));
            // loop que descompacta todos os arquivos compactados
            for (int i = 0; i < arquivos.size(); i++) {
                Recuperar(arquivos.get(i), versoes.get(opcao - 1));
            }
            // apaga da pasta de backup apos recuperar a versão
            File file = new File("backup/" + versoes.get(opcao - 1));
            deleteDirectory(file);
            file.delete();
            // tira o nome da pasta de backup do arraylist e atualiza o txt 
            versoes.remove(opcao - 1);
            AtualizarIndice(versoes);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Recuperar(String path, String date) {

        // caminho do arquivo compactado
        File file = new File("backup/" + date + "/" + path + ".lzw");

        // Verifica se o diretório pai existe, se não, cria
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        // basicamente mesmo codigo de backup, apenas inverte os arquivos de lida e escrita
        // e ao inves de codivicar nos decodificamos
        try (FileInputStream fis = new FileInputStream(file);
                FileOutputStream fos = new FileOutputStream("dados/" + path + ".db");
                BufferedInputStream bis = new BufferedInputStream(fis);
                BufferedOutputStream bos = new BufferedOutputStream(fos)) {

            byte[] buffer = new byte[8 * 1024]; // 8 KB
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                byte[] compressedData = new byte[bytesRead];
                System.arraycopy(buffer, 0, compressedData, 0, bytesRead);

                byte[] NormalData = LZW.decodifica(compressedData);
                bos.write(NormalData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // metodo recursivo que apaga arquivos de um diretorio
    public void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Exclui subdiretórios recursivamente
                    deleteDirectory(file);
                } else {
                    // Exclui arquivos
                    file.delete();
                }
            }
        }
    }

    // atualiza o txt das versões de backup
    public void AtualizarIndice(ArrayList<String> versoes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("backup/versoes.txt"))) {

            for (int i = 0; i < versoes.size(); i++) {
                writer.write(versoes.get(i) + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

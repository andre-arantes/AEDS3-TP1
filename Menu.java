import java.io.*;
import java.util.*;

class Menu extends Programa {

    public static void main(String[] args) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("./db/movie.db", "rw");
        Scanner sc = new Scanner(System.in);
        Imdb imdb = new Imdb();

        int opcao = 0;
        boolean loop = true;

        if (raf.length() == 0)
            raf.writeInt(0);
        raf.seek(0); // Volta para o inicio do arquivo
        while (loop) {
            System.out.println("|_________MENU_________|");
            System.out.println("|                      |");
            System.out.println("|0 - Sair              |");
            System.out.println("|1 - Create            |");
            System.out.println("|2 - Read              |");
            System.out.println("|3 - Update            |");
            System.out.println("|4 - Delete            |");
            System.out.println("|______________________|\n");
            System.out.print("-> ");
            do {
                try {
                    opcao = sc.nextInt();
                    if (opcao < 0 || opcao > 5)
                        System.out.println("Operação Inválida");
                } catch (Exception e) {
                    System.out.println("-> Digite um número!");
                    sc.nextLine();
                    break;
                }

            } while (opcao < 0 || opcao > 5);

            switch (opcao) {
                // CREATE
                case 1:
                    imdb = new Imdb();
                    System.out.println("\n____________CRIAR FILME____________");

                    System.out.print("-> Name: ");
                    String test;
                    sc.nextLine();
                    test = sc.nextLine();
                    imdb.setName(test);

                    System.out.print("-> Year: ");
                    imdb.setYear(sc.nextInt());

                    System.out.print("-> Runtime: ");
                    String runtime;
                    sc.nextLine();
                    runtime = sc.nextLine();
                    imdb.setRuntime(runtime);

                    System.out.print("-> Genre: ");
                    String genre;
                    genre = sc.nextLine();
                    imdb.setGenre(genre);

                    if (create(imdb))
                        System.out.println("\n-> Filme criado com sucesso!");
                    else
                        System.out.println("\n-> Erro ao criar filme!");
                    break;

                    // BUSCA
                case 2:
                    System.out.println("\n____________BUSQUE ____________");
                    System.out.print("-> Escreva o id do filme a ser buscado: ");
                    int search = sc.nextInt();
                    sc.nextLine();
                    imdb = readByRanking(search);
                    if (imdb == null)
                        System.out.println("-> Filme não encontrado!");
                    else
                        System.out.println(imdb);
                    break;
                // UPDATE
                case 3: 
                    System.out.println("\n____________ATUALIZAR FILME____________");
                    System.out.println("-> Escreva o id do filme a ser atualizado: ");
                    int searchID = sc.nextInt();
                    imdb = readByRanking(searchID);
                    // if (imdb != null) {
                        
                    // }
                // DELETE
                case 4:
                    System.out.println("\n____________DELETAR FILME____________");
                    System.out.println("-> Escreva o id do filme a ser deletado: ");
                    int deleteID = sc.nextInt();
                    sc.nextLine();
                    if (delete(deleteID))
                        System.out.println("\n-> Filme deletado com sucesso!");
                    else
                        System.out.println("\n-> Erro ao deletar filme!");
                    break;
                // SAIR
                case 0:
                    System.out.println("-> Saindo...");
                    loop = false;
                    break;
            }

        }
        raf.close();
        sc.close();
    }
}

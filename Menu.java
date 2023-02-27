import java.io.*;
import java.util.*;

class Menu extends Programa{

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
            System.out.println("__________MENU__________");
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
                        System.out.println("Operacao Inválida");
                } catch (Exception e) {
                    System.out.println("-> Digite um número!");
                    sc.nextLine();
                    break;
                }

            } while (opcao < 0 || opcao > 5);

            switch (opcao) {
                case 1: // CREATE
                    imdb = new Imdb();
                    System.out.println("\n____________CRIAR FILME____________");

                    System.out.print("-> Name: ");
                    imdb.setName(sc.next());

                    System.out.print(" -> Year: ");
                    imdb.setYear(sc.nextInt());

                    System.out.print("-> Runtime: ");
                    imdb.setRuntime(sc.next());

                    System.out.print("-> Genre: ");
                    imdb.setGenre(sc.next());

                    System.out.print("-> Rating: ");
                    imdb.setRating(sc.nextInt());

                    System.out.print("-> Director: ");
                    imdb.setDirector(sc.next());

                    if (create(imdb)) System.out.println("\n-> Conta criada com sucesso!");
                    else System.out.println("\n-> Erro ao criar conta!");

                    case 0: // SAIR
                    System.out.println("-> Saindo");
                    loop = false;
                    break;
            }

        }
        raf.close();
        sc.close();
    }
}

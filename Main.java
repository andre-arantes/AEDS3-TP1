// import java.io.*;
import java.util.*;

class Main {
    public static void main(String[] args) {

        // RandomAccessFile raf = new RandomAccessFile("db/imdb.csv", "rw");
        Scanner sc = new Scanner(System.in);
        boolean loop = true;
        int opcao = 0;
        while (loop) {
            System.out.println("__________MENU__________");
            System.out.println("|                      |");
            System.out.println("|0 - Sair              |");
            System.out.println("|1 - Create            |");
            System.out.println("|2 - Read              |");
            System.out.println("|3 - Update            |");
            System.out.println("|4 - Delete            |");
            System.out.println("|______________________|\n");
            System.out.print("->");
            do {
                try {
                    opcao = sc.nextInt();
                    if (opcao < 0 || opcao > 9)
                        System.out.println("Operacao Inválida");
                } catch (Exception e) {
                    System.out.println("Digite um número ->");
                    sc.nextLine();
                    break;
                }

            } while (opcao < 0 || opcao > 9);
            switch (opcao) {
                case 1:
                    System.out.println("Teste");
            }
        }

        sc.close();
    }
}

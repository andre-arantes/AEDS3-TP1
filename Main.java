
import java.io.*;
// import java.io.FileNotFoundException;    
import java.io.RandomAccessFile;
import java.util.*;

class Main {
    public static void main(String[] args) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("db/movies.db", "rw");
        Scanner sc = new Scanner(System.in);
        if (raf.length() == 0)
            raf.write(0); 
        raf.seek(0); // Volta para o inicio do arquivo
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

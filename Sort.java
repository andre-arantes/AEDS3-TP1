import java.io.*;
import java.util.ArrayList;

public class Sort extends CRUD {
    // ARQUVIOS DE DEBUG
    public static void printFile(RandomAccessFile raf) throws IOException {
        raf.seek(4);

        long currentPosition = raf.getFilePointer();
        long endPosition = raf.length();
        int len;
        byte[] ba;
        Imdb imdb = new Imdb();

        while (currentPosition < endPosition) {
            if (raf.readByte() == 0) {
                len = raf.readInt();
                ba = new byte[len];
                raf.read(ba);
                imdb.fromByteArray(ba);
                // System.out.println(imdb);
                System.out.println("Ranking: " + imdb.getRanking() + "\t| Nome: " + imdb.getName());
                currentPosition = raf.getFilePointer();
            } else {
                len = raf.readInt();
                long temp = raf.getFilePointer();
                raf.seek(temp + len);
                currentPosition = raf.getFilePointer();
            }
        }
        raf.close();
    }

    public static void printFile2(RandomAccessFile raf) throws IOException {
        raf.seek(0);
        while (raf.getFilePointer() < raf.length()) {
            raf.readByte();
            raf.readInt();
            int ranking = raf.readInt();
            String name = raf.readUTF();
            raf.readInt();
            raf.readUTF();
            raf.readUTF();

            System.out.println("Ranking: " + ranking + "\t| Nome: " + name);
        }
    }

    public static Imdb readInArq(RandomAccessFile raf, int search) throws IOException {
        Imdb imdb = new Imdb();
        raf.seek(0);
        long currentPosition = raf.getFilePointer();
        long endPosition = raf.length();
        int len;
        while (currentPosition < endPosition) {
            if (raf.readByte() == 0) { // Se a lápide não existe (0000)
                len = raf.readInt(); // Lê o tamanho do registro
                imdb.setRanking(raf.readInt());
                // System.out.println(imdb.getRanking());
                if (imdb.getRanking() == search) {
                    imdb.setName(raf.readUTF());
                    imdb.setYear(raf.readInt());
                    imdb.setRuntime(raf.readUTF());
                    imdb.setGenre(raf.readUTF());
                    return imdb;
                } else {
                    currentPosition = raf.getFilePointer();
                }
            } else { // Se a lapide existir, ele pula o registro
                len = raf.readInt();
                long temp = raf.getFilePointer();
                raf.seek(temp + len);
                currentPosition = raf.getFilePointer();
            }
        }
        raf.close();
        return null;
    }

    // INTERCALAÇÃO
    public static boolean intercalar() throws IOException {
        RandomAccessFile raf = new RandomAccessFile("./db/database.db", "r");
        ArrayList<Imdb> movies = new ArrayList<Imdb>();
        Imdb movie = new Imdb();
        // int rnk;
        int len;
        // byte ba[];
        long currentPosition = raf.getFilePointer();
        long endPosition = raf.length();
        RandomAccessFile arq1 = new RandomAccessFile("arq1.bin", "rw");
        RandomAccessFile arq2 = new RandomAccessFile("arq2.bin", "rw");

        raf.seek(4); // Posiciona o ponteiro no inicio do arquivo
        while (currentPosition < endPosition) {
            long pointer = currentPosition; // Enquanto o ponteiro não chegar no final do arquivo
            if (raf.readByte() == 0) {
                len = raf.readInt();
                movie = readByRanking(raf.readInt());
                movies.add(movie);
                raf.seek(len + (pointer + 5));
                currentPosition = raf.getFilePointer();
            } else { // pula o registro apagado
                len = raf.readInt();
                long temp = raf.getFilePointer();
                raf.seek(temp + len);
                currentPosition = raf.getFilePointer();
            }
        }
        // System.out.println(movies);
        ArrayList<Imdb> moviesTemp = new ArrayList<Imdb>(); // Array temporário para armazenar os filmes
        int contador = 0; // Contador para saber quantos filmes foram adicionadas no arquivo
        while (movies.size() > 0) { // Enquanto o array movies nao estiver vazio
            for (int j = 0; j < 10; j++) { // Adiciona 10 movies no array temporário
                if (movies.size() > 0) { // Se o array movies nao estiver vazio
                    moviesTemp.add(movies.get(0)); // Adiciona a primeira movie do array movies no array temporário
                    movies.remove(0); // Remove a primeira movie do array movies
                }
            }

            moviesTemp.sort((Imdb c1, Imdb c2) -> c1.getRanking() - c2.getRanking()); //

            // Ordena o array temporário

            contador++;

            if (contador % 2 != 0) { // Se o contador for impar adiciona no arquivo 1
                for (Imdb c : moviesTemp) {
                    arq1.writeByte(0);
                    arq1.writeInt(c.toByteArray().length);
                    arq1.write(c.toByteArray());
                }
            } else { // Se o contador for par adiciona no arquivo 2
                for (Imdb c : moviesTemp) {
                    arq2.writeByte(0);
                    arq2.writeInt(c.toByteArray().length);
                    arq2.write(c.toByteArray());
                }
            }

            moviesTemp.clear(); // Limpa o array temporário
        }

        // System.out.println("\nArquivo 1: ");
        // printFile2(arq1);
        // System.out.println("\nArquivo 2: ");
        // printFile2(arq2);

        // --------------------------------------------------------------------------------------

        ArrayList<Imdb> movies1 = new ArrayList<Imdb>();
        ArrayList<Imdb> movies2 = new ArrayList<Imdb>();
        System.out.println("\n-> Intercalação 1: ");

        arq1.seek(0); // Posiciona o ponteiro no inicio do arquivo 1
        while (arq1.getFilePointer() < arq2.length()) { // Enquanto o ponteiro nao chegar no final do arquivo
            arq1.readByte();
            arq1.readInt();
            movie = readInArq(arq1, arq1.readInt()); // Le o registro
            movies1.add(movie); // Adiciona o registro no array movies1
        }

        arq2.seek(0); // Posiciona o ponteiro no inicio do arquivo 2
        while (arq2.getFilePointer() < arq2.length()) { // Enquanto o ponteiro naochegar no final do arquivo
            arq2.readByte();
            arq2.readInt();
            movie = readInArq(arq2, arq2.readInt()); // Le o registro
            movies2.add(movie); // Adiciona o registro no array movies2
        }

        raf.close();
        return true;
    }

}

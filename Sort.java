import java.io.*;
import java.util.ArrayList;

public class Sort extends CRUD {
    // ARQUVIOS DE DEBUG
    public static void printFile(RandomAccessFile raf) throws IOException {
        raf.seek(0);
        System.out.println("Numero de filmes : " + raf.readInt());
        System.out.println();

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
                System.out.println(imdb);
                System.out.println();
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

    public static Imdb readFile(RandomAccessFile raf, int search) throws IOException {
        try {
            Imdb imdb = new Imdb();
            raf.seek(4);
            long currentPosition = raf.getFilePointer();
            long endPosition = raf.length();
            int len;
            byte ba[];
            boolean flag = false;
            while (currentPosition < endPosition) {
                if (raf.readByte() == 0) { // Se a lápide não existe (0000)
                    len = raf.readInt(); // Lê o tamanho do registro
                    ba = new byte[len]; // Cria um vetor de bytes de acordo com o tamanho (len)
                    raf.read(ba); // Lê o vetor de bytes
                    imdb.fromByteArray(ba);
                    if (imdb.getRanking() == search) { // Se o ranking for o mesmo que o buscado
                        currentPosition = endPosition;
                        flag = true;
                    } else { // Senão, continua a ler
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
            if (flag) { // Se contem o registro, retorna o filme
                return imdb;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(" -> Erro na leitura do registro!");
            return null;
        }
    }

    // INTERCALAÇÃO
    public static boolean intercalar(RandomAccessFile raf) throws IOException {
        ArrayList<Imdb> movies = new ArrayList<Imdb>();
        Imdb movie = new Imdb();
        int rnk;
        int len;
        long currentPosition = raf.getFilePointer();
        long endPosition = raf.length();
        RandomAccessFile arq1 = new RandomAccessFile("arq1.bin", "rw");
        RandomAccessFile arq2 = new RandomAccessFile("arq2.bin", "rw");

        raf.seek(4); // Posiciona o ponteiro no inicio do arquivo
        while (currentPosition < endPosition) {
            long pointer = currentPosition; // Enquanto o ponteiro não chegar no final do arquivo
            if (raf.readByte() == 0) {
                len = raf.readInt();
                rnk = raf.readInt();
                movie = readByRanking(rnk);
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

        System.out.println(movies);
        // ArrayList<Imdb> moviesTemp = new ArrayList<Imdb>(); // Array temporário para armazenar os filmes
        // int contador = 0; // Contador para saber quantos filmes foram adicionadas no arquivo
        // while (movies.size() > 0) { // Enquanto o array movies nao estiver vazio
        //     for (int j = 0; j < 5; j++) { // Adiciona 5 movies no array temporário
        //         if (movies.size() > 0) { // Se o array movies nao estiver vazio
        //             moviesTemp.add(movies.get(0)); // Adiciona a primeira conta do array movies no array temporário
        //             movies.remove(0); // Remove a primeira conta do array contas
        //         }
        //     }
        // }
            // System.out.println(movies);
            // System.out.println(moviesTemp);
            // moviesTemp.sort((Imdb c1, Imdb c2) -> c1.getRanking() - c2.getRanking()); //
            // Ordena o array temporário

            // contador++;

            // if (contador % 2 != 0) { // Se o contador for impar adiciona no arquivo 1
            // for (Imdb c : moviesTemp) {
            // arq1.writeByte(0);
            // arq1.writeInt(c.toByteArray().length);
            // arq1.write(c.toByteArray());
            // }
            // } else { // Se o contador for par adiciona no arquivo 2
            // for (Imdb c : moviesTemp) {
            // arq2.writeByte(0);
            // arq2.writeInt(c.toByteArray().length);
            // arq2.write(c.toByteArray());
            // }
            // }

            // moviesTemp.clear(); // Limpa o array temporário

            // }
            // System.out.println("\nArquivo 1: ");
            // printFile(arq1);
            // System.out.println("\nArquivo 2: ");
            // printFile(arq2);
            return true;
        }
    }
// }

import java.io.*;

class Programa {

    // --------------- Carrega os registros e escreve no DB ---------------

    public static void load() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("./db/test.csv"))) {
            Imdb imdb = new Imdb();
            br.readLine();
            String line = br.readLine();
            Long filePointer = (long) 0;
            // System.out.println(line);
            while (line != null) {
                String array[] = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                imdb.setRanking(Integer.parseInt(array[0]));
                imdb.setName(array[1]);
                imdb.setYear(Integer.parseInt(array[2]));
                imdb.setRuntime(array[4]);
                imdb.setGenre(array[5]);

                filePointer = write(imdb, filePointer);

                line = br.readLine();
                // System.out.println(imdb.getName());
            }
            br.close();
        }
    }

    public static long write(Imdb imdb, Long filePointer) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("./db/reduceddb.db", "rw");
        Long position = filePointer;
        byte[] ba;

        if (position == 0) {
            raf.writeInt(1);
            raf.writeByte(0); // Criação da lápide
            ba = imdb.toByteArray();
            raf.writeInt(ba.length); // Número inteiro
            raf.write(ba); // Escreve as informações
        } else {
            raf.seek(0); //
            int ranking = raf.readInt();
            ranking++;
            raf.seek(0);
            raf.writeInt(ranking);
            raf.seek(position);
            raf.writeByte(0); // Criação da lápide
            ba = imdb.toByteArray();
            raf.writeInt(ba.length);
            raf.write(ba);
        }

        position = raf.getFilePointer();
        raf.close();
        return position;
    }

    // --------------- CREATE ---------------

    public static boolean create(Imdb imdb) throws FileNotFoundException {
        RandomAccessFile raf = new RandomAccessFile("./db/reduceddb.db", "rw");
        try {
            // Processo de escrita do novo ranking no cabeçalho
            raf.seek(0);
            int ultimoID = raf.readInt();
            int novoID = ultimoID + 1;
            imdb.setRanking(novoID);
            raf.seek(0);
            raf.writeInt(novoID);

            // Começo da criação do novo registro
            raf.seek(raf.length());
            raf.writeByte(0);
            raf.writeInt(imdb.toByteArray().length); // Escreve o tamanho do registro
            raf.writeInt(imdb.getRanking()); // Escreve o ranking(id) no array de bytes
            raf.writeUTF(imdb.getName()); // Escreve o nome no array de bytes
            raf.writeInt(imdb.getYear()); // Escreve o year no array de bytes
            raf.writeUTF(imdb.getRuntime());// Escreve o runtime no array de bytes
            raf.writeUTF((imdb.getGenre()));// Escreve o array de genre no array de bytes
            System.out.println(imdb);
            return true;

        } catch (Exception e) {
            System.out.println("-> Erro ao criar o registro!");
            return false;
        }
    }

    // --------------- READS ---------------

    public static void readAll() throws IOException {
        RandomAccessFile raf = new RandomAccessFile("./db/reduceddb.db", "rw");

        raf.seek(0);
        System.out.println("Número de filmes: " + raf.readInt());
        System.out.println();

        long currentPosition = raf.getFilePointer();
        long endPosition = raf.length();
        int len;
        byte[] ba;
        Imdb imdb = new Imdb();

        while (currentPosition < endPosition) {
            if(raf.readByte() == 0) {
            // raf.seek(raf.getFilePointer() + 1);
            len = raf.readInt();
            ba = new byte[len];
            raf.read(ba);
            imdb.fromByteArray(ba);
            System.out.println(imdb);
            System.out.println();
            currentPosition = raf.getFilePointer();
        }
    }
        raf.close();
    }

    public static Imdb readByRanking(int search) {
        try {
            RandomAccessFile raf = new RandomAccessFile("./db/reduceddb.db", "r");
            Imdb imdb = new Imdb();
            raf.seek(4);
            // System.out.println("Número de filmes: " + raf.readInt());
            // System.out.println();
            long currentPosition = raf.getFilePointer();
            long endPosition = raf.length();
            int len;
            int test;
            // while (currentPosition < endPosition) {
            if (raf.readByte() == 0) {
                len = raf.readInt(); // Le o tamanho do registro
                // System.out.println(len);
                test = raf.readInt(); // Le o ranking atual
                imdb.setRanking(test);
                // if (imdb.getRanking() == search) { //ONDE DA O ERRO NA LEITURA DO REGISTRO

                // System.out.println(imdb.getRanking());
                if (imdb.getRanking() == 1) {
                    imdb.setName(raf.readUTF());
                    imdb.setYear(raf.readInt());
                    imdb.setRuntime(raf.readUTF());
                    imdb.setGenre(raf.readUTF());
                    System.out.println(imdb.getName());
                    // System.out.println("Certo");
                    // return imdb;
                    // }
                    // } else {
                    // raf.skipBytes(raf.readInt());
                    // }
                } else {
                    System.out.println("Error!");
                }
            }
            // }
            // return null;
        } catch (Exception e) {
            System.out.println("Erro na leitura do registro!");
            return null;
        }
        return null;

    }

    public static Imdb readByName(String nome) {
        try {
            RandomAccessFile raf = new RandomAccessFile("./db/reduceddb.db", "r");
            Imdb imdb = null;
            long currentPosition = raf.getFilePointer();
            long endPosition = raf.length();
            boolean achado = false;

            
            raf.seek(4);
            while (currentPosition < endPosition && !achado) {
            if (raf.readByte() == 0) {
                imdb = new Imdb();
                raf.readInt();

                imdb.setRanking(raf.readInt());
                imdb.setName(raf.readUTF());
                imdb.setYear(raf.readInt());
                imdb.setRuntime(raf.readUTF());
                imdb.setGenre(raf.readUTF());

                if (imdb.getName().equals(nome)) {
                    achado = true;
                    System.out.println("Achado");
                }
            } else {
                raf.skipBytes(raf.readInt());
            }
            }
            return imdb;
        } catch (Exception e) {
            System.out.println("Erro na leitura do registro!");
            return null;
        }    
    }

    // --------------- UPDATE ---------------
    // Codigo do update (precisa do read por id e por nome pronto)
    // --------------- DELETE ---------------

    public static boolean delete(Imdb imdb) throws FileNotFoundException { // Exclui uma conta
        RandomAccessFile raf = new RandomAccessFile("./db/reduceddb.db", "rw");
        try {
            raf.seek(4); // Posiciona o ponteiro no inicio do arquivo
            while (raf.getFilePointer() < raf.length()) { // Enquanto o ponteiro nao chegar no final do arquivo
                if (raf.readByte() == 0) { // Se a lapide for 0 (ativo)
                    int tam = raf.readInt();
                    int ranking = raf.readInt();

                    if (ranking == imdb.getRanking()) { // Se o id da conta for igual ao id da conta a ser excluida
                        raf.seek(raf.getFilePointer() - 9); // Volta o ponteiro para o inicio do registro
                        raf.writeByte(1); // Escreve a lapide 1 (excluido)
                        return true;
                    } else {
                        raf.skipBytes(tam - 4); // Pula o resto do registro
                    }
                } else {
                    raf.skipBytes(raf.readInt()); // Pula o registro
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("-> Erro ao deletar registro!");
            return false;
        }
    }

    public static void main(String[] args) throws IOException {
        // delete(3);
    }
}

import java.io.*;

class Programa {
    // --------------- Carrega os registros e escreve no DB ---------------

    public static void load() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("./db/test.csv"))) {
            Imdb imdb = new Imdb();
            br.readLine();
            String line = br.readLine();
            Long filePointer = (long) 0;
            while (line != null) {
                String array[] = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                imdb.setRanking(Integer.parseInt(array[0]));
                imdb.setName(array[1]);
                imdb.setYear(Integer.parseInt(array[2]));
                imdb.setRuntime(array[4]);
                imdb.setGenre(array[5]);

                filePointer = write(imdb, filePointer);

                line = br.readLine();
            }
            br.close();
        }
    }

    public static long write(Imdb imdb, Long filePointer) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("./db/reduceddb1.db", "rw");
        Long position = filePointer;
        byte[] ba;

        if (position == 0) {
            raf.writeInt(1); // Cria a posição 1
            raf.writeByte(0); // Criação da lápide
            ba = imdb.toByteArray(); // Escreve o primeiro elemento
            raf.writeInt(ba.length); // Número inteiro
            raf.write(ba); // Escreve as informações
        } else {
            raf.seek(0); // Vai para o início do registro
            int ranking = raf.readInt(); // Lê o ranking
            ranking++; // Adiciona o ranking novo
            raf.seek(0); // Volta para o início do arquivo
            raf.writeInt(ranking); // Atualiza o registro inicial
            raf.seek(position);
            raf.writeByte(0); // Criação da lápide
            ba = imdb.toByteArray(); // Impressao do filme no arquivo db
            raf.writeInt(ba.length);
            raf.write(ba);
        }

        position = raf.getFilePointer();
        raf.close();
        return position;
    }

    // --------------- CREATE ---------------

    public static boolean create(Imdb imdb) throws FileNotFoundException {
        RandomAccessFile raf = new RandomAccessFile("./db/reduceddb1.db", "rw");
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
            raf.close();
            return true;
        } catch (Exception e) {
            System.out.println("-> Erro ao criar o registro!");
            return false;
        }
    }

    // --------------- READS ---------------

    // public static void readAll() throws IOException {
    // RandomAccessFile raf = new RandomAccessFile("./db/reduceddb1.db", "rw");

    // raf.seek(0);
    // System.out.println("Número de filmes: " + raf.readInt());
    // System.out.println();

    // long currentPosition = raf.getFilePointer();
    // long endPosition = raf.length();
    // int len;
    // byte[] ba;
    // Imdb imdb = new Imdb();

    // while (currentPosition < endPosition) {
    // if (raf.readByte() == 0) {
    // raf.seek(raf.getFilePointer() + 1);
    // len = raf.readInt();
    // ba = new byte[len];
    // raf.read(ba);
    // imdb.fromByteArray(ba);
    // System.out.println(imdb);
    // System.out.println();
    // currentPosition = raf.getFilePointer();
    // }
    // }
    // raf.close();
    // }

    public static Imdb readByRanking(int search) throws IOException {
        try {
            RandomAccessFile raf = new RandomAccessFile("./db/reduceddb1.db", "r");
            Imdb imdb = new Imdb();
            raf.seek(4);
            long currentPosition = raf.getFilePointer();
            long endPosition = raf.length();
            int len;
            byte ba[];
            boolean flag = false;
            while (currentPosition < endPosition) {
                if (raf.readByte() == 0) { // se a lápide não existe (0000)
                    len = raf.readInt(); // Le o tamanho do registro
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
            System.out.println("Erro na leitura do registro!");
            return null;
        }
    }

    // public static Imdb readByName(String nome) {
    // try {
    // RandomAccessFile raf = new RandomAccessFile("./db/reduceddb.db", "r");
    // Imdb imdb = null;
    // long currentPosition = raf.getFilePointer();
    // long endPosition = raf.length();
    // boolean achado = false;

    // raf.seek(4);
    // while (currentPosition < endPosition && !achado) {
    // if (raf.readByte() == 0) {
    // imdb = new Imdb();
    // raf.readInt();

    // imdb.setRanking(raf.readInt());
    // imdb.setName(raf.readUTF());
    // imdb.setYear(raf.readInt());
    // imdb.setRuntime(raf.readUTF());
    // imdb.setGenre(raf.readUTF());

    // if (imdb.getName().equals(nome)) {
    // achado = true;
    // System.out.println("Achado");
    // }
    // } else {
    // raf.skipBytes(raf.readInt());
    // }
    // }
    // return imdb;
    // } catch (Exception e) {
    // System.out.println("Erro na leitura do registro!");
    // return null;
    // }
    // }

    // --------------- UPDATE ---------------
    // public static Imdb getImdbUpdate (int ranking) {
    // Imdb imdb = new Imdb();

    // }
    // --------------- DELETE ---------------

    public static boolean delete(int search) throws FileNotFoundException { // Exclui uma conta
        RandomAccessFile raf = new RandomAccessFile("./db/reduceddb1.db", "rw");
        try {
            raf.seek(4); // Posiciona o ponteiro no inicio do arquivo
            long currentPosition = raf.getFilePointer();
            long endPosition = raf.length();
            int len;
            boolean flag = false;
            while (currentPosition < endPosition) {
                long pointer = currentPosition;
                if (raf.readByte() == 0) { // Se o filme não foi deletado
                    len = raf.readInt();
                    int test = raf.readInt();
                    if (search == test) {
                        raf.seek(pointer);
                        raf.writeByte(1);
                        flag = true;
                        raf.seek(0);
                        int ultimoID = raf.readInt();
                        int novoID = ultimoID - 1;
                        raf.seek(0);
                        raf.writeInt(novoID);
                        currentPosition = endPosition;
                    } else { // Se o filme não tiver o mesmo id do 'search'
                        raf.seek(len + (pointer + 5)); // Pula o registro
                        currentPosition = raf.getFilePointer(); // Continua a leitura
                    }
                } else { // Se a lapide existir, ele pula o registro
                    len = raf.readInt();
                    long temp = raf.getFilePointer();
                    raf.seek(temp + len);
                    currentPosition = raf.getFilePointer();
                }
            }
            raf.close();
            if (flag) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("-> Erro ao deletar registro!");
            return false;
        }
    }

    public static void main(String[] args) throws IOException {

    }
}

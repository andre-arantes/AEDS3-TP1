import java.io.*;

class CRUD {
    // --------------- Carrega os registros e escreve no DB ---------------

    public static void loadInDatabase() throws IOException {
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
                filePointer = writeInDatabase(imdb, filePointer, 0);
                line = br.readLine();
            }
            br.close();
        }
    }

    public static long writeInDatabase(Imdb imdb, Long filePointer, int select) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("./db/database.db", "rw");
        Long position = filePointer;
        byte[] ba;

        if (position == 0) {
            raf.writeInt(1); // Cria a posição 1
            raf.writeByte(0); // Criação da lápide
            ba = imdb.toByteArray(); // Escreve o primeiro elemento
            raf.writeInt(ba.length); // Número inteiro
            raf.write(ba); // Escreve as informações
        } else if (select == 1) {
            raf.seek(position);
            raf.writeByte(0); // Criação da lápide
            ba = imdb.toByteArray(); // Impressão do filme no arquivo db
            raf.writeInt(ba.length);
            raf.write(ba);
        } else {
            raf.seek(0); // Vai para o início do registro
            int ranking = raf.readInt(); // Lê o ranking
            ranking++; // Adiciona o ranking novo
            raf.seek(0); // Volta para o início do arquivo
            raf.writeInt(ranking); // Atualiza o registro inicial
            raf.seek(position);
            raf.writeByte(0); // Criação da lápide
            ba = imdb.toByteArray(); // Impressão do filme no arquivo db
            raf.writeInt(ba.length);
            raf.write(ba);
        }
        position = raf.getFilePointer();
        raf.close();
        return position;
    }

    // --------------- CREATE ---------------

    public static boolean create(Imdb imdb) throws FileNotFoundException {
        RandomAccessFile raf = new RandomAccessFile("./db/database.db", "rw");
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


    public static Imdb readByRanking(int search) throws IOException {
        try {
            RandomAccessFile raf = new RandomAccessFile("./db/database.db", "r");
            Imdb imdb = new Imdb();
            raf.seek(4);
            long currentPosition = raf.getFilePointer();
            long endPosition = raf.length();
            int len;
            byte ba[];
            boolean flag = false;
            while (raf.getFilePointer() < raf.length()) {
                if (raf.readByte() == 0) { // se a lápide não existe (0000)
                    len = raf.readInt(); // Le o tamanho do registro
                    ba = new byte[len]; // Cria um vetor de bytes de acordo com o tamanho (len)
                    raf.read(ba); // Lê o vetor de bytes
                    imdb.fromByteArray(ba);
                    if (imdb.getRanking() == search) { // Se o ranking for o mesmo que o buscado
                        currentPosition = endPosition;
                        return imdb;
                    } else { // Senão, continua a ler
                        currentPosition = raf.getFilePointer();
                        // raf.skipBytes(len - 7);
                    }
                } else { // Se a lapide existir, ele pula o registro
                    len = raf.readInt();
                    long temp = raf.getFilePointer();
                    raf.seek(temp + len);
                    currentPosition = raf.getFilePointer();
                }
            }
            // raf.close();
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

    // --------------- UPDATE ---------------

    public static boolean update(Imdb newImdb) {
        /* Atualizar o id depois que adicionar um filme se registro ALTERAR de tamanho o
        registro desejado deve ser deletado e o novo deve ser criado no final do arquivo */
        try {
            RandomAccessFile raf = new RandomAccessFile("./db/database.db", "rw");
            Imdb imdb = new Imdb();
            raf.seek(4);
            long currentPosition = raf.getFilePointer();
            long endPosition = raf.length();
            int len;
            byte ba[];
            int flag = 0;
            boolean find = false;
            while (currentPosition < endPosition) {
                long pointer = raf.getFilePointer();
                if (raf.readByte() == 0) {
                    len = raf.readInt();
                    ba = new byte[len];
                    raf.read(ba);
                    imdb.fromByteArray(ba);
                    if (imdb.getRanking() == newImdb.getRanking()) {
                        if (!(newImdb.getName().equals(imdb.getName()))) {
                            if (newImdb.getName().length() != imdb.getName().length()) {
                                flag++;
                            }
                        }
                        imdb.setName(newImdb.getName());
                        do {
                            if (newImdb.getYear() > 1800) {
                                imdb.setYear(newImdb.getYear());
                            } else {
                                System.out.println("Data inválida! Sua data deve ser maior que 1800.");
                            }
                        } while (newImdb.getYear() < 1800);
                        if (!(newImdb.getRuntime().equals(imdb.getRuntime()))) {
                            if (newImdb.getRuntime().length() != imdb.getRuntime().length()) {
                                flag++;
                            }
                        }
                        imdb.setRuntime(newImdb.getRuntime());

                        if (!(newImdb.getGenre().equals(imdb.getGenre()))) {
                            if (newImdb.getGenre().length() != imdb.getGenre().length()) {
                                flag++;
                            }
                        }
                        imdb.setGenre(newImdb.getGenre());

                        System.out.println(imdb);
                        if (flag > 0) { // se o tamanho do registro foi aumentado, deve-se mover o ponteiro para o final do arquivo e adicionar o novo
                            raf.seek(pointer);
                            raf.writeByte(1);
                            writeInDatabase(imdb, raf.length(), 1);
                            currentPosition = endPosition;
                            find = true;
                        } else { // se o tamanho do registro for menor ou igual ao original somente atualizar os arquivos no local
                            writeInDatabase(imdb, pointer, 1);
                            currentPosition = endPosition;
                            find = true;
                        }
                    } else {
                        currentPosition = raf.getFilePointer();
                    }
                } else {
                    len = raf.readInt();
                    long temp = raf.getFilePointer();
                    raf.seek(temp + len);
                    currentPosition = raf.getFilePointer();
                }
            }
            raf.close();
            return find;
        } catch (Exception e) {
            System.out.println("Erro na leitura do registro!");
            return false;
        }

    }

    // --------------- DELETE ---------------

    public static boolean delete(int search) throws FileNotFoundException { // Exclui uma conta
        RandomAccessFile raf = new RandomAccessFile("./db/database.db", "rw");
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
                    int id = raf.readInt();
                    if (search == id) {
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
        loadInDatabase();
    }
}
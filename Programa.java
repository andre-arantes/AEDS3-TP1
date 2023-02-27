import java.io.*;

class Programa {
    public static void load() throws IOException {
        try (BufferedReader br = new BufferedReader(
                new FileReader("newimdb.csv"))) {
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
                imdb.setRating(Float.parseFloat(array[6]));
                imdb.setDirector(array[9]);

                filePointer = write(imdb, filePointer);

                line = br.readLine();
                // System.out.println(imdb.getName());
            }
            br.close();
        }
    }

    public static long write(Imdb imdb, Long filePointer) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("./db/movie.db", "rw");

        Long position = filePointer;
        byte[] ba;

        if (position == 0) {
            raf.writeInt(1);
            raf.writeByte(0); // Criação da lápide
            ba = imdb.toByteArray();
            raf.writeInt(ba.length); // numero inteiro
            raf.write(ba); // escreve as informações
        } else {
            raf.seek(0); //
            int ranking = raf.readInt();
            raf.seek(0);
            raf.writeInt(ranking);
            ranking++;
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

    public static void read() throws IOException {
        RandomAccessFile raf = new RandomAccessFile("./db/movie.db", "rw");

        raf.seek(0);
        System.out.println("Numero de filmes:" + raf.readInt());
        System.out.println();

        long currentPosition = raf.getFilePointer();
        long endPosition = raf.length();
        int len;
        byte[] ba;
        Imdb imdb = new Imdb();

        while (currentPosition < endPosition) {
            raf.seek(raf.getFilePointer() + 1);
            len = raf.readInt();
            ba = new byte[len];
            raf.read(ba);
            imdb.fromByteArray(ba);
            System.out.println(imdb);
            System.out.println();
            currentPosition = raf.getFilePointer();
        }

        raf.close();
    }

    // public static Imdb readByRanking(int search) {
    // try {
    // RandomAccessFile raf = new RandomAccessFile(".db/movie.db", "rw");
    // raf.seek(0);
    // Imdb imdb = new Imdb();

    // while (raf.getFilePointer() < raf.length()) {
    // raf.seek(raf.getFilePointer() + 1);
    // int len = raf.readInt();
    // imdb.setRanking(raf.readInt());
    // if (imdb.getRanking() == search) {
    // imdb.setName(raf.readUTF());
    // imdb.setYear(raf.readInt());
    // imdb.setRuntime(raf.readUTF());
    // imdb.setGenre(raf.readUTF());
    // imdb.setRating(raf.readInt());
    // imdb.setDirector(raf.readUTF());

    // return imdb;

    // } else {
    // System.out.println("Registro não encontrado");
    // }
    // raf.close();
    // }
    // return null;

    // } catch (Exception e) {
    // System.out.println("Erro na leitura do registro!");
    // return null;
    // }

    // }

    public static boolean create(Imdb imdb) throws FileNotFoundException {
        RandomAccessFile raf = new RandomAccessFile("./db/movie.db", "rw");
        try {
            raf.seek(raf.length());
            raf.writeByte(0);
            raf.writeInt(imdb.toByteArray().length);
            raf.writeInt(imdb.getRanking()); // Escreve o ranking(id) no array de bytes
            raf.writeUTF(imdb.getName()); // Escreve o nome no array de bytes
            raf.writeInt(imdb.getYear()); // Escreve o year no array de bytes
            raf.writeUTF(imdb.getRuntime());// Escreve o runtime no array de bytes

            raf.writeUTF((imdb.getGenre()));// Escreve o array de genre no array de bytes

            raf.writeFloat(imdb.getRating());// Escreve o rating no array de bytes
            raf.writeUTF(imdb.getDirector());// Escreve o director no array de bytes
            return true;

        } catch (Exception e) {
            System.out.println("-> Erro ao criar o registro!");
            return false;
        }

    }

    // public static void main(String[] args) {
    //     // readByRanking(4);
    //     try {
    //         read();
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
}

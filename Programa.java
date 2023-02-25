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
                System.out.println(imdb.getRanking());
            }
            br.close();
        }
    }

    public static long write(Imdb imdb, Long filePointer) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("db/movie.db", "rw");

        Long position = filePointer;
        byte[] ba;

        if (position == 0) {
            raf.writeInt(1);
            raf.writeBoolean(false);
            ba = imdb.toByteArray();
            raf.writeInt(ba.length); // numero inteiro
            raf.write(ba); // escreve as informações
        } else {
            raf.seek(0); //
            int ranking = raf.readInt();
            ranking++;
            raf.seek(0);
            raf.writeInt(ranking);
            raf.seek(position);
            raf.writeBoolean(false); // lapide
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
        // System.out.println(raf.length());
        int len;
        byte ba[];
        Imdb imdb = new Imdb();

        while (currentPosition < endPosition) {
            raf.seek(raf.getFilePointer() + 1);
            len = raf.readInt();
            ba = new byte[len];
            raf.read(ba);
            System.out.println(raf.read(ba));
            imdb.fromByteArray(ba);
            System.out.println(imdb);
            System.out.println();
            currentPosition = raf.getFilePointer();
        }
        raf.close();
    }

    public static void main(String[] args) {
        try {
            read();
            // load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

import java.io.*;

class Imdb {

    // ------------ Atributos ------------

    private int ranking;
    private String name;
    private int year;
    private int runtime;
    private String[] genre;
    private float rating;
    private String director;

    // ------------ Construtores ---------------

    public Imdb() { // Construtor padrão
        this.ranking = -1;
        this.name = null;
        this.year = 0;
        this.runtime = 0;
        this.genre = new String[1000];
        this.rating = 0F;
        this.director = null;
    }

    public Imdb(int ranking, String name, int year, int runtime, String[] genre, float rating, String director) {// Construtor
                                                                                                                 // sem
                                                                                                                 // parametros
        this.ranking = ranking;
        this.name = name;
        this.year = year;
        this.runtime = runtime;
        this.genre = genre;
        this.rating = rating;
        this.director = director;
    }

    // ------------ Getters e Setters ------------//
    // Ranking
    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    // Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Year
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    // Runtime
    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    // Genre
    public String[] getGenre() {
        return genre;
    }

    public void setGenre(String[] genre) {
        this.genre = genre;
    }

    // Rating
    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    // Director
    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    // ------------ Métodos ------------
    public byte[] toByteArray() throws IOException { // Converte objeto para um fluxo de bytes

        ByteArrayOutputStream baos = new ByteArrayOutputStream(); // Cria um novo array dde bytes
        DataOutputStream dos = new DataOutputStream(baos); // Cria um fluxo de dados

        dos.writeInt(this.getRanking()); // Escreve o ranking(id) no array de bytes
        dos.writeUTF(this.getName()); // Escreve o nome no array de bytes
        dos.writeInt(this.getYear()); // Escreve o year no array de bytes
        dos.writeInt(this.getRuntime());// Escreve o runtime no array de bytes
        for (int i = 0; i < 1000; i++) {
            dos.writeUTF((this.getGenre()[i]));// Escreve o array de genre no array de bytes
        }
        dos.writeFloat(this.getRating());// Escreve o rating no array de bytes
        dos.writeUTF(this.getDirector());// Escreve o director no array de bytes

        dos.close();
        baos.close();

        return baos.toByteArray(); // Retorno do array de bytes
    }

    public void fromByteArray(byte ba[]) throws IOException { // Recebe o array de bytes e faz a gravação

        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        this.ranking = dis.readInt(); // Le o ranking do array de bytes
        this.name = dis.readUTF();// Le o name do array de bytes
        this.year = dis.readInt();// Le o year do array de bytes
        this.runtime = dis.readInt();// Le o runtime do array de bytes
        for (int i = 0; i < 1000; i++) {
            this.genre[i] = dis.readUTF();// Le o array de genre do array de bytes
        }
        this.rating = dis.readFloat();// Le o rating do array de bytes
        this.director = dis.readUTF();// Le o director do array de bytes
    }

    public short size() throws IOException {
        return (short) this.toByteArray().length;
    }
}

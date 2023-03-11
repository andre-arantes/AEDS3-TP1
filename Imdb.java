import java.io.*;

class Imdb {

    // ------------ Atributos ------------

    private int ranking; // Inteiro 
    private String name; // String de tamanho variável
    private int year; // Data
    private String runtime; // String de tamanho fixo
    private String genre; // Lista de valores com separador


    // ------------ Construtores ---------------

    // Construtor padrão
    public Imdb() { 
        this.ranking = -1;
        this.name = null;
        this.year = -1;
        this.runtime = null;
        this.genre = null;
    }

    public Imdb(int ranking, String name, int year, String runtime, String genre) {
        this.ranking = ranking; // Inteiro
        this.name = name; // String de tamanho variável
        this.year = year; // Data
        this.runtime = runtime; // String de tamanho fixo
        this.genre = genre; // Lista de valores
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
    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    // Genre
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    // ------------ Métodos ------------

    public String toString() {
        return "{" +
                " ranking='" + getRanking() + "'" +
                ", name='" + getName() + "'" +
                ", year='" + getYear() + "'" +
                ", runtime='" + getRuntime() + "'" +
                ", genre='" + getGenre() + "'" +
                "}";
    }

    public byte[] toByteArray() throws IOException { // Converte objeto para um fluxo de bytes

        ByteArrayOutputStream baos = new ByteArrayOutputStream(); // Cria um novo array de bytes
        DataOutputStream dos = new DataOutputStream(baos); // Cria um fluxo de dados
        dos.writeInt(this.getRanking()); // Escreve o ranking(id) no array de bytes
        dos.writeUTF(this.getName()); // Escreve o nome no array de bytes
        dos.writeInt(this.getYear()); // Escreve o year no array de bytes
        dos.writeUTF(this.getRuntime());// Escreve o runtime no array de bytes
        dos.writeUTF(this.getGenre());// Escreve o array de genre no array de bytes

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
        this.runtime = dis.readUTF();// Le o runtime do array de bytes
        this.genre = dis.readUTF();// Le o array de genre do array de bytes
    }

    public short size() throws IOException {
        return (short) this.toByteArray().length;
    }
}
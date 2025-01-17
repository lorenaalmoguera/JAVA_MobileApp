package es.umh.dadm.mispelis48796558b;

public class Pelicula {


    int idMovie, idPlatform, MovieRating;

    byte[] image_data;
    String email, movieName, MovieGenre, MovieLength;

    /**
     * Constructor
     *
     * @param idMovie     idpeli
     * @param email       correo
     * @param idPlatform  idplat
     * @param image_data  imagen
     * @param movieName   nombre
     * @param MovieLength duracion
     * @param MovieGenre  genero
     * @param MovieRating valoracion
     */
    public Pelicula(int idMovie, String email, int idPlatform, byte[] image_data, String movieName, String MovieLength, String MovieGenre, int MovieRating) {
        this.idMovie = idMovie;
        this.email = email;
        this.image_data = image_data;
        this.idPlatform = idPlatform;
        this.movieName = movieName;
        this.MovieLength = MovieLength;
        this.MovieGenre = MovieGenre;
        this.MovieRating = MovieRating;
    }

    /**
     * Constructor sin la id de la pelicula
     *
     * @param email       correo
     * @param idPlatform  idplat
     * @param image_data  imagen
     * @param movieName   nombre
     * @param MovieLength duracion
     * @param MovieGenre  genero
     * @param MovieRating valoracion
     */
    public Pelicula(String email, int idPlatform, byte[] image_data, String movieName, String MovieLength, String MovieGenre, int MovieRating) {
        this.email = email;
        this.image_data = image_data;
        this.idPlatform = idPlatform;
        this.movieName = movieName;
        this.MovieLength = MovieLength;
        this.MovieGenre = MovieGenre;
        this.MovieRating = MovieRating;
    }

    /**
     * Getter idmmovie
     *
     * @return idmovie
     */
    public int getIdMovie() {
        return idMovie;
    }

    /**
     * Getter Idplatform
     *
     * @return idplatform
     */
    public int getIdPlatform() {
        return idPlatform;
    }

    /**
     * Getter movierating
     *
     * @return movierating
     */
    public int getMovieRating() {
        return MovieRating;
    }

    /**
     * Getter de la foto
     *
     * @return foto en formato byte[]
     */
    public byte[] getImage_data() {
        return image_data;
    }

    /**
     * Getter correo
     *
     * @return correo
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter correo
     *
     * @param email usuario
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter nombre peli
     *
     * @return nombre peli
     */
    public String getMovieName() {
        return movieName;
    }

    /**
     * Getter MovieGenre
     *
     * @return moviegenre
     */
    public String getMovieGenre() {
        return MovieGenre;
    }

    /**
     * Getter MovieLength
     *
     * @return movielength
     */
    public String getMovieLength() {
        return MovieLength;
    }

}

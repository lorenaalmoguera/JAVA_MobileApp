package es.umh.dadm.mispelis48796558b;

public class Plataforma {
    //idPlatform INTEGER PRIMARY KEY, email TEXT, image_data BLOB, nombre TEXT, url TEXT, usuario TEXT, password TEXT, FOREIGN KEY(email) REFERENCES Userdetails(email)

    int idPlatform;
    byte[] image_data;
    String email, nombre, url, usuario, password;

    /**
     * Constructor sin id
     * @param image_data de plat
     * @param email de creador
     * @param nombre de plat
     * @param url de plat
     * @param usuario de plat
     * @param password de plat
     */
    public Plataforma(byte[] image_data, String email, String nombre, String url, String usuario, String password){
        this.image_data = image_data;
        this.email = email;
        this.nombre = nombre;
        this.url = url;
        this.usuario = usuario;
        this.password = password;
    }

    /**
     * Constructor con id
     * @param idPlatform de plat
     * @param image_data de creador
     * @param email de plat
     * @param nombre de plat
     * @param url de plat
     * @param usuario de plat
     * @param password de plat
     */
    public Plataforma(int idPlatform, byte[] image_data, String email, String nombre, String url, String usuario, String password){
        this.idPlatform = idPlatform;
        this.image_data = image_data;
        this.email = email;
        this.nombre = nombre;
        this.url = url;
        this.usuario = usuario;
        this.password = password;
    }


    /**
     * Getter la id de la paltaforma
     * @return plat id
     */
    public int getIdPlatform() {
        return idPlatform;
    }

    /**
     * Getter imagen
     * @return imagen en formato byte[]
     */
    public byte[] getImage_data() {
        return image_data;
    }


    /**
     * Getter email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter email
     * @param email de usuaro
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter nombre
     * @return nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Setter nombre
     * @param nombre de peli
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Getter url
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Getter usuario plataforma
     * @return usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Setter usuario plataforma
     * @param usuario plat
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * Getter password
     * @return password
     */
    public String getPassword() {
        return password;
    }

}
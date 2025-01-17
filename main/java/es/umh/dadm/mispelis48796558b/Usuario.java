package es.umh.dadm.mispelis48796558b;

public class Usuario {

    String nombre, apellido, email, password, fechaNac, preguntaS, resS, ResAuth, interes5;
    int check1, check2, check3, check4, check5;

    public Usuario(String nombre, String apellido, String email, String password, String fechaNac, String preguntaS, String resS, String ResAuth, int check1, int check2, int check3, int check4, int check5, String interes5){
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.fechaNac = fechaNac;
        this.preguntaS = preguntaS;
        this.resS = resS;
        this.ResAuth = ResAuth;
        this.check1 = check1;
        this.check2 = check2;
        this.check3 = check3;
        this.check4 = check4;
        this.check5 = check5;
        this.interes5 = interes5;
    }

    /**
     * Getter Nombre
     * @return nombre
     */
    public String getNombre(){
        return nombre;
    }

    /**
     * Getter Apellido
     * @return apellido
     */
    public String getApellido(){
        return apellido;
    }

    /**
     * Getter email
     * @return email
     */
    public String getEmail(){
        return email;
    }

    /**
     * Getter password
     * @return password
     */
    public String getPassword(){
        return password;
    }

    /**
     * Getter fechanac
     * @return fechanac
     */
    public String getFechaNac(){
        return fechaNac;
    }

    /**
     * Getter preguntas
     * @return fechaNac
     */
    public String getPreguntaS(){
        return preguntaS;
    }

    /**
     * Getter Respuesta de seguridad
     * @return respuesta
     */
    public String getResS(){
        return resS;
    }

    /**
     * Getter Interes 5
     * @return interes5
     */
    public String getInteres5(){
        return interes5;
    }

    /**
     * Getter check1
     * @return 1 o 0
     */
    public int getCheck1(){
        return check1;
    }

    /**
     * Getter Check2
     * @return 1 o 0
     */
    public int getCheck2(){
        return check2;
    }

    /**
     * Getter Check3
     * @return 1 o 0
     */
    public int getCheck3(){
        return check3;
    }

    /**
     * Getter Check4
     * @return 1 o 0
     */
    public int getCheck4(){
        return check4;
    }

    /**
     * Getter 5
     * @return 1 o 0
     */
    public int getCheck5(){
        return check5;
    }

    /**
     * Si o No
     * @return Si/No
     */
    public String getResAuth() {
        return ResAuth;
    }
}

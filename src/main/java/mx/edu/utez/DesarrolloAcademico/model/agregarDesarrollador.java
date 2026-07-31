package mx.edu.utez.DesarrolloAcademico.model;

public class agregarDesarrollador {

    private String nombre;
    private String apellidoM;
    private String getApellidoP;
    private int numeroEmpleado;
    private int telefonoEmpleado;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTelefonoEmpleado() {
        return telefonoEmpleado;
    }

    public void setTelefonoEmpleado(int telefonoEmpleado) {
        this.telefonoEmpleado = telefonoEmpleado;
    }

    public int getNumeroEmpleado() {
        return numeroEmpleado;
    }

    public void setNumeroEmpleado(int numeroEmpleado) {
        this.numeroEmpleado = numeroEmpleado;
    }

    public String getGetApellidoP() {
        return getApellidoP;
    }

    public void setGetApellidoP(String getApellidoP) {
        this.getApellidoP = getApellidoP;
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }

    public agregarDesarrollador(String nombre, String apellidoM, String getApellidoP, int numeroEmpleado, int telefonoEmpleado) {
        this.nombre = nombre;
        this.apellidoM = apellidoM;
        this.getApellidoP = getApellidoP;
        this.numeroEmpleado = numeroEmpleado;
        this.telefonoEmpleado = telefonoEmpleado;
    }
}

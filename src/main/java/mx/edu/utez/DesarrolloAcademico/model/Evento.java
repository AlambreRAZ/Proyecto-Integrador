package mx.edu.utez.DesarrolloAcademico.model;

import java.util.Date;



public class Evento {
    private  int ID;
    private  String nombre;
    private Date Fecha_Inicio;
    private   Date Fecha_Fin;
    private String Lugar;
    private String Institucion;
    private String Tipo_Evento;
    private String Descripcion;
    private String Modalidad;


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Date getFecha_Inicio() {
        return Fecha_Inicio;
    }

    public void setFecha_Inicio(Date fecha_Inicio) {
        Fecha_Inicio = fecha_Inicio;
    }

    public Date getFecha_Fin() {
        return Fecha_Fin;
    }

    public void setFecha_Fin(Date fecha_Fin) {
        Fecha_Fin = fecha_Fin;
    }

    public Evento() {
    }

    public String getLugar() {
        return Lugar;
    }

    public void setLugar(String lugar) {
        Lugar = lugar;
    }

    public String getInstitucion() {
        return Institucion;
    }

    public void setInstitucion(String institucion) {
        Institucion = institucion;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getTipo_Evento() {
        return Tipo_Evento;
    }

    public void setTipo_Evento(String tipo_Evento) {
        Tipo_Evento = tipo_Evento;
    }

    public String getModalidad() {
        return Modalidad;
    }

    public void setModalidad(String modalidad) {
        Modalidad = modalidad;
    }
}

package mx.edu.utez.DesarrolloAcademico.model;

import java.util.List;

public class agregarEvento_co {



    private int id;
    private String nombre;
    private String lugar;
    private String institucion;
    private String tipo;
    private String descripcion;
    private String fechaInicio; // Puedes usar java.sql.Date si prefieres
    private String fechaFin;
    private String modalidad;
    private List<Integer> docentesAsignados; // Lista de IDs de los docentes

    // Agrega constructores, getters y setters

    public agregarEvento_co() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getDocentesAsignados() {
        return docentesAsignados;
    }

    public void setDocentesAsignados(List<Integer> docentesAsignados) {
        this.docentesAsignados = docentesAsignados;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getNombre() {
        return nombre;
    }
    private Integer idDivision;
    private Integer creadoPor;

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public agregarEvento_co (int id, String nombre, String lugar, String institucion, String tipo, String descripcion, String fechaInicio, String fechaFin, String modalidad, List<Integer> docentesAsignados) {
        this.id = id;
        this.nombre = nombre;
        this.lugar = lugar;
        this.institucion = institucion;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.modalidad = modalidad;
        this.docentesAsignados = docentesAsignados;
    }

    public Integer getIdDivision() {
        return idDivision;
    }

    public void setIdDivision(Integer idDivision) {
        this.idDivision = idDivision;
    }

    public Integer getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(Integer creadoPor) {
        this.creadoPor = creadoPor;
    }
}

package mx.edu.utez.DesarrolloAcademico.model;

import java.sql.Timestamp;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String rol;
    private Integer idDivision; // Puede ser null
    private String numeroEmpleado;
    private String telefono;
    private String correoInstitucional;
    private String contrasena;
    private Timestamp fechaRegistro;
    private int activo;
    private Integer creadoPor; // Puede ser null

    public Usuario() {}

    public Usuario(int idUsuario, String nombre, String apellidoPaterno, String apellidoMaterno, String rol, 
                   Integer idDivision, String numeroEmpleado, String telefono, String correoInstitucional, 
                   String contrasena, Timestamp fechaRegistro, int activo, Integer creadoPor) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.rol = rol;
        this.idDivision = idDivision;
        this.numeroEmpleado = numeroEmpleado;
        this.telefono = telefono;
        this.correoInstitucional = correoInstitucional;
        this.contrasena = contrasena;
        this.fechaRegistro = fechaRegistro;
        this.activo = activo;
        this.creadoPor = creadoPor;
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidoPaterno() { return apellidoPaterno; }
    public void setApellidoPaterno(String apellidoPaterno) { this.apellidoPaterno = apellidoPaterno; }

    public String getApellidoMaterno() { return apellidoMaterno; }
    public void setApellidoMaterno(String apellidoMaterno) { this.apellidoMaterno = apellidoMaterno; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public Integer getIdDivision() { return idDivision; }
    public void setIdDivision(Integer idDivision) { this.idDivision = idDivision; }

    public String getNumeroEmpleado() { return numeroEmpleado; }
    public void setNumeroEmpleado(String numeroEmpleado) { this.numeroEmpleado = numeroEmpleado; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreoInstitucional() { return correoInstitucional; }
    public void setCorreoInstitucional(String correoInstitucional) { this.correoInstitucional = correoInstitucional; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public Timestamp getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Timestamp fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public int getActivo() { return activo; }
    public void setActivo(int activo) { this.activo = activo; }

    public Integer getCreadoPor() { return creadoPor; }
    public void setCreadoPor(Integer creadoPor) { this.creadoPor = creadoPor; }
}

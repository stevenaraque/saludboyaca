package co.sena.cimm.adso.saludboyaca.dto;

public class Usuario {

    private int id;
    private String nombres;
    private String apellidos;
    private String documento;
    private String email;
    private String username;
    private String password;
    private String rol;
    private String especialidad;
    private String langPreferido;
    private int activo;

    // Constructor vacío
    public Usuario() {
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getLangPreferido() {
        return langPreferido;
    }

    public void setLangPreferido(String langPreferido) {
        this.langPreferido = langPreferido;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    // Método extra
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    public boolean esMedico() {
        return "MEDICO".equalsIgnoreCase(rol);
    }

    public boolean esEnfermero() {
        return "ENFERMERO".equalsIgnoreCase(rol);
    }

    public boolean esRecepcionista() {
        return "RECEPCIONISTA".equalsIgnoreCase(rol);
    }

    public boolean estaActivo() {
        return activo == 1;
    }
}

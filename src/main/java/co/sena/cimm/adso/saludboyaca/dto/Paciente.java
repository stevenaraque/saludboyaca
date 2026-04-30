package co.sena.cimm.adso.saludboyaca.dto;

import java.util.Date;

public class Paciente {
    private int id;
    private String nombres;
    private String apellidos;
    private String documento;
    private Date fechaNacimiento;
    private String telefono;
    private String email;
    private String eps;
    private String veredaBarrio;

    public Paciente() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEps() { return eps; }
    public void setEps(String eps) { this.eps = eps; }

    public String getVeredaBarrio() { return veredaBarrio; }
    public void setVeredaBarrio(String veredaBarrio) { this.veredaBarrio = veredaBarrio; }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}
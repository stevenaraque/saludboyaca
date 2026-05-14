package co.sena.cimm.adso.saludboyaca.dto;

import java.sql.Time;
import java.util.Date;

public class Cita {

    private int id;
    private int idPaciente;
    private int idMedico;
    private int idEspecialidad;
    private Date fechaCita;
    private Time horaCita;
    private String motivo;
    private String estado;
    private String observaciones;
    private Date fechaRegistro;
    private int idRegistradoPor;
    // Agregar este campo junto a los otros campos desnormalizados
    private String documentoPaciente;

// Agregar estos métodos después de setNombrePaciente
    public String getDocumentoPaciente() {
        return documentoPaciente;
    }

    public void setDocumentoPaciente(String documentoPaciente) {
        this.documentoPaciente = documentoPaciente;
    }

    // Campos desnormalizados para mostrar en JSP
    private String nombrePaciente;
    private String nombreMedico;
    private String nombreEspecialidad;

    public Cita() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public int getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(int idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public Time getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(Time horaCita) {
        this.horaCita = horaCita;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdRegistradoPor() {
        return idRegistradoPor;
    }

    public void setIdRegistradoPor(int idRegistradoPor) {
        this.idRegistradoPor = idRegistradoPor;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getNombreMedico() {
        return nombreMedico;
    }

    public void setNombreMedico(String nombreMedico) {
        this.nombreMedico = nombreMedico;
    }

    public String getNombreEspecialidad() {
        return nombreEspecialidad;
    }

    public void setNombreEspecialidad(String nombreEspecialidad) {
        this.nombreEspecialidad = nombreEspecialidad;
    }

    public boolean estaProgramada() {
        return "PROGRAMADA".equalsIgnoreCase(estado);
    }

    public boolean estaConfirmada() {
        return "CONFIRMADA".equalsIgnoreCase(estado);
    }

    public boolean estaCancelada() {
        return "CANCELADA".equalsIgnoreCase(estado);
    }

    public boolean tieneObservaciones() {
        return observaciones != null && !observaciones.isBlank();
    }

    public String getResumenCita() {
        return nombrePaciente + " - " + nombreEspecialidad + " (" + fechaCita + ")";
    }
}

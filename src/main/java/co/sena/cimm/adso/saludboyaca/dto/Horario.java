package co.sena.cimm.adso.saludboyaca.dto;

import java.sql.Time;

public class Horario {
    private int id;
    private int idMedico;
    private String nombreMedico;
    private int diaSemana;
    private Time horaInicio;
    private Time horaFin;
    private int maxCitas;

    public Horario() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdMedico() { return idMedico; }
    public void setIdMedico(int idMedico) { this.idMedico = idMedico; }

    public String getNombreMedico() { return nombreMedico; }
    public void setNombreMedico(String nombreMedico) { this.nombreMedico = nombreMedico; }

    public int getDiaSemana() { return diaSemana; }
    public void setDiaSemana(int diaSemana) { this.diaSemana = diaSemana; }

    public Time getHoraInicio() { return horaInicio; }
    public void setHoraInicio(Time horaInicio) { this.horaInicio = horaInicio; }

    public Time getHoraFin() { return horaFin; }
    public void setHoraFin(Time horaFin) { this.horaFin = horaFin; }

    public int getMaxCitas() { return maxCitas; }
    public void setMaxCitas(int maxCitas) { this.maxCitas = maxCitas; }
}
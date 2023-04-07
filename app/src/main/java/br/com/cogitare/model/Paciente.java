package br.com.cogitare.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.time.LocalDate;

@Entity
public class Paciente {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String nome;
    private GeneroEnum sexo;
    private LocalDate dataNascimento;
    private Integer numeroProntuario;
    private String unidadeInternacao;

    public Paciente() {

    }

    public Paciente(@NonNull String nome, GeneroEnum sexo, LocalDate dataNascimento, Integer numeroProntuario, String unidadeInternacao) {
        this.nome = nome;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
        this.numeroProntuario = numeroProntuario;
        this.unidadeInternacao = unidadeInternacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getNome() {
        return nome;
    }

    public void setNome(@NonNull String nome) {
        this.nome = nome;
    }

    public GeneroEnum getSexo() {
        return sexo;
    }

    public void setSexo(GeneroEnum sexo) {
        this.sexo = sexo;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Integer getNumeroProntuario() {
        return numeroProntuario;
    }

    public void setNumeroProntuario(Integer numeroProntuario) {
        this.numeroProntuario = numeroProntuario;
    }

    public String getUnidadeInternacao() {
        return unidadeInternacao;
    }

    public void setUnidadeInternacao(String unidadeInternacao) {
        this.unidadeInternacao = unidadeInternacao;
    }
}

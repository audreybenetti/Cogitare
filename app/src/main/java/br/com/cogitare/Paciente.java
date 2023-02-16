package br.com.cogitare;

import java.time.LocalDate;

public class Paciente {

    private String nome;
    private GeneroEnum sexo;
    private LocalDate dataNascimento;
    private Integer numeroProntuario;
    private String unidadeInternacao;

    public Paciente (String nome, Integer numeroProntuario, String unidadeInternacao){
        setNome(nome);
        setNumeroProntuario(numeroProntuario);
        setUnidadeInternacao(unidadeInternacao);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
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

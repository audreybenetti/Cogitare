package br.com.cogitare.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Avaliacao {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private RitmoCardiacoEnum ritmoCardiaco;

    private Integer auscultaPulmonar;

    public Avaliacao() {
    }

    public Avaliacao(RitmoCardiacoEnum ritmoCardiaco, Integer auscultaPulmonar) {
        this.ritmoCardiaco = ritmoCardiaco;
        this.auscultaPulmonar = auscultaPulmonar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RitmoCardiacoEnum getRitmoCardiaco() {
        return ritmoCardiaco;
    }

    public void setRitmoCardiaco(RitmoCardiacoEnum ritmoCardiaco) {
        this.ritmoCardiaco = ritmoCardiaco;
    }

    public Integer getAuscultaPulmonar() {
        return auscultaPulmonar;
    }

    public void setAuscultaPulmonar(Integer auscultaPulmonar) {
        this.auscultaPulmonar = auscultaPulmonar;
    }

}

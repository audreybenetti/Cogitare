package br.com.cogitare.persistence;



import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.com.cogitare.model.Paciente;

@Dao
public interface PacienteDao {

    @Insert
    long insert (Paciente paciente);

    @Delete
    void delete (Paciente paciente);

    @Update
    void update(Paciente paciente);

    @Query("SELECT * FROM paciente WHERE id = :id")
    Paciente findById(long id);

    @Query("SELECT * FROM paciente ORDER BY nome ASC")
    List<Paciente> findAll();
}

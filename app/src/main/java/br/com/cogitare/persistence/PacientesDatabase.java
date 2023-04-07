package br.com.cogitare.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import br.com.cogitare.model.Paciente;
import br.com.cogitare.utils.RoomTypeConverters;

@Database(entities = {Paciente.class}, version = 1, exportSchema = false)
@TypeConverters({RoomTypeConverters.class})
public abstract class PacientesDatabase extends RoomDatabase {

    public abstract PacienteDao pacienteDao();
    private static PacientesDatabase instance;

    public static PacientesDatabase getDatabase(final Context context){
        if (instance == null) {
            synchronized (PacientesDatabase.class) {
                if(instance == null) {
                    instance = Room.databaseBuilder(context,
                                    PacientesDatabase.class,
                                    "pacientes.db")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        } return instance;
    }
}

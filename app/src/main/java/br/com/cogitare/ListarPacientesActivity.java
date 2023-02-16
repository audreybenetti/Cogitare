package br.com.cogitare;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListarPacientesActivity extends AppCompatActivity {

    private ListView listViewPacientes;
    private ArrayList<Paciente> pacientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_pacientes);

        listViewPacientes = findViewById(R.id.listViewPacientes);

        listViewPacientes.setOnItemClickListener(
                (adapterView, view, position, id) -> {
                    Paciente paciente = (Paciente) listViewPacientes.getItemAtPosition(position);

                    Toast.makeText(getApplicationContext(),
                            "Paciente de nome " + paciente.getNome() + " foi clicado!",
                            Toast.LENGTH_SHORT).show();
                }
        );

        popularLista();
    }

    public void cadastrarPaciente(View view) {
        Intent intent = new Intent(this, CadastrarPacienteActivity.class);
        startActivity(intent);
    }

    public void mostrarAutoria(View view){
        Intent intent = new Intent(this, AutoriaActivity.class);
        startActivity(intent);
    }

    private void popularLista() {
        String [] nomes = getResources().getStringArray(R.array.nomes);
        int [] sexo = getResources().getIntArray(R.array.sexos);
        int [] numerosProntuario = getResources().getIntArray(R.array.prontuarios);
        String [] unidadesInternacao = getResources().getStringArray(R.array.unidades_internacao);

        pacientes = new ArrayList<>();
        GeneroEnum [] generoEnum = GeneroEnum.values();
        for(int i = 0; i < nomes.length; i++){
            Paciente paciente = new Paciente(nomes[i], numerosProntuario[i], unidadesInternacao[i]);
            paciente.setSexo(generoEnum[sexo[i]]);
            pacientes.add(paciente);
        }

        PacienteAdapter pacienteAdapter = new PacienteAdapter(this, pacientes);
        listViewPacientes.setAdapter(pacienteAdapter);
    }
}
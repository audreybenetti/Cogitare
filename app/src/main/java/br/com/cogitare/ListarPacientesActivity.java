package br.com.cogitare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListarPacientesActivity extends AppCompatActivity {

    private ListView listViewPacientes;
    private ArrayList<Paciente> pacientes;
    private PacienteAdapter pacienteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_pacientes);
        setTitle("Pacientes cadastrados");

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
        CadastrarPacienteActivity.novoPaciente(this);
    }

    public void mostrarAutoria(View view){
        AutoriaActivity.mostrarAutoria(this);
    }

    private void popularLista() {
        pacientes = new ArrayList<>();
        pacienteAdapter = new PacienteAdapter(this, pacientes);
        listViewPacientes.setAdapter(pacienteAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent intent) {

        if (resultCode == Activity.RESULT_OK) {

            Bundle bundle = intent.getExtras();
            String nome = bundle.getString(CadastrarPacienteActivity.KEY_NOME);
            String sexo = bundle.getString(CadastrarPacienteActivity.KEY_GENERO);
            String prontuario = bundle.getString(CadastrarPacienteActivity.KEY_PRONTUARIO);
            String data = bundle.getString(CadastrarPacienteActivity.KEY_DATA);
            String unidade = bundle.getString(CadastrarPacienteActivity.KEY_UNIDADE);

            Paciente paciente = new Paciente(nome, Integer.parseInt(prontuario), unidade);
            paciente.setSexo(sexo.equals(String.valueOf(R.id.buttonMasculino)) ? GeneroEnum.MASCULINO : GeneroEnum.FEMININO);
            pacientes.add(paciente);

            pacienteAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
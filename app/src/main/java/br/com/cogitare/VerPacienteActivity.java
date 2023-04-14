package br.com.cogitare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import java.util.List;

import br.com.cogitare.model.GeneroEnum;
import br.com.cogitare.model.Paciente;
import br.com.cogitare.persistence.PacientesDatabase;
import br.com.cogitare.utils.UtilsDate;

public class VerPacienteActivity extends AppCompatActivity{

    public static final String ID      = "ID";

    private RadioGroup radioGroupGeneros;
    private EditText editNome, editData, editProntuario, editUnidade;
    private Button buttonPaciente, buttonRegistro;
    private Integer idPaciente;
    private Paciente paciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_paciente);
        setTitle("Patient data");

        radioGroupGeneros = findViewById(R.id.groupGeneros);
        editNome = findViewById(R.id.edit_nome);
        editProntuario = findViewById(R.id.edit_prontuario);
        editUnidade = findViewById(R.id.edit_unidade_internacao);
        editData = findViewById(R.id.edit_data);
        buttonPaciente = findViewById(R.id.buttonEditPatient);
        buttonRegistro = findViewById(R.id.buttonAddRegistro);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        idPaciente = bundle.getInt(ID);
        insereDadosPaciente(idPaciente);
        configuraBottomUp();
    }

    public static void verPaciente(AppCompatActivity activity, Paciente paciente) {
        Intent intent = new Intent(activity, VerPacienteActivity.class);
        intent.putExtra(ID, paciente.getId());
        activity.startActivity(intent);
    }

    private void insereDadosPaciente(Integer id) {
        PacientesDatabase database = PacientesDatabase.getDatabase(this);
        paciente = database.pacienteDao().findById(id);

        editNome.setText(paciente.getNome());
        editProntuario.setText(paciente.getNumeroProntuario().toString());
        editData.setText(UtilsDate.formatDate(VerPacienteActivity.this, paciente.getDataNascimento()));
        radioGroupGeneros.check(paciente.getSexo() == GeneroEnum.MASCULINO ? R.id.buttonMasculino : R.id.buttonFeminino);
        editUnidade.setText(paciente.getUnidadeInternacao());
    }

    public void editarPaciente(View view){
        CadastrarPacienteActivity.editarPaciente(VerPacienteActivity.this, paciente);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK){
            insereDadosPaciente(idPaciente);
        }
    }

    public void adicionarAvaliacao(View view){
        AdicionarAvaliacaoActivity.adicionarAvaliacao(VerPacienteActivity.this);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void configuraBottomUp() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

}

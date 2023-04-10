package br.com.cogitare;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.Calendar;

import br.com.cogitare.model.GeneroEnum;
import br.com.cogitare.model.Paciente;
import br.com.cogitare.persistence.PacientesDatabase;
import br.com.cogitare.utils.UtilsDate;

public class AdicionarRegistroActivity extends AppCompatActivity{

    public static final String ID      = "ID";

    private RadioGroup radioGroupGeneros;
    private EditText editNome, editData, editProntuario, editUnidade;
    private Paciente paciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_adicionar_registro);
        setTitle("Add record");

        radioGroupGeneros = findViewById(R.id.groupGeneros);
        editNome = findViewById(R.id.edit_nome);
        editProntuario = findViewById(R.id.edit_prontuario);
        editUnidade = findViewById(R.id.edit_unidade_internacao);
        editData = findViewById(R.id.edit_data);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        insereDadosPaciente(bundle.getInt(ID));
        configuraBottomUp();
    }

    public static void adicionarRegistro(AppCompatActivity activity, Paciente paciente) {
        Intent intent = new Intent(activity, AdicionarRegistroActivity.class);
        intent.putExtra(ID, paciente.getId());
        activity.startActivity(intent);
    }

    private void insereDadosPaciente(Integer id) {
        PacientesDatabase database = PacientesDatabase.getDatabase(this);
        paciente = database.pacienteDao().findById(id);

        editNome.setText(paciente.getNome());
        editProntuario.setText(paciente.getNumeroProntuario().toString());
        editData.setText(UtilsDate.formatDate(AdicionarRegistroActivity.this, paciente.getDataNascimento()));
        radioGroupGeneros.check(paciente.getSexo() == GeneroEnum.MASCULINO ? R.id.buttonMasculino : R.id.buttonFeminino);
        editUnidade.setText(paciente.getUnidadeInternacao());
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

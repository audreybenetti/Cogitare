package br.com.cogitare;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import br.com.cogitare.model.GeneroEnum;
import br.com.cogitare.model.Paciente;
import br.com.cogitare.persistence.PacientesDatabase;
import br.com.cogitare.utils.UtilsDate;

public class CadastrarPacienteActivity extends AppCompatActivity {

    private RadioGroup radioGroupGeneros;
    private Calendar calendario;
    private EditText editNome, editData, editProntuario;
    private CheckBox editTermos;
    private Spinner editUnidade;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Paciente paciente;
    private int    modo;
    private static final String MODO = "MODO";
    public static final String ID      = "ID";
    public static final int NOVO_PACIENTE = 1;
    public static final int ALTERAR_PACIENTE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_paciente);

        radioGroupGeneros = findViewById(R.id.groupGeneros);
        editNome = findViewById(R.id.edit_nome);
        editProntuario = findViewById(R.id.edit_prontuario);
        editUnidade = findViewById(R.id.edit_unidade_internacao);
        editTermos = findViewById(R.id.check_termos);
        editData = findViewById(R.id.edit_data);

        displayDatePicker();
        configuraBottomUp();
        definePagina();
    }

    public static void novoPaciente(AppCompatActivity activity) {
        Intent intent = new Intent(activity, CadastrarPacienteActivity.class);
        intent.putExtra(MODO, NOVO_PACIENTE);
        activity.startActivityForResult(intent, NOVO_PACIENTE);
    }

    public static void editarPaciente(AppCompatActivity activity, Paciente paciente) {
        Intent intent = new Intent(activity, CadastrarPacienteActivity.class);
        intent.putExtra(MODO, ALTERAR_PACIENTE);
        intent.putExtra(ID, paciente.getId());
        activity.startActivityForResult(intent, ALTERAR_PACIENTE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.cadastrar_paciente_opcoes, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemCadastrar:
                verificaDados();
                return true;
            case R.id.menuItemLimparCampos:
                limparCampos();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void definePagina() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            modo = bundle.getInt(MODO, NOVO_PACIENTE);
            if (modo == NOVO_PACIENTE) {
                paciente = new Paciente();
                setTitle(getString(R.string.titulo_cadastro));
            } else {
                setTitle(getString(R.string.titulo_alteracao));
                insereDadosPaciente(bundle.getInt(ID));
            }
        }
    }

    private void insereDadosPaciente(Integer id) {
        PacientesDatabase database = PacientesDatabase.getDatabase(this);
        paciente = database.pacienteDao().findById(id);

        editNome.setText(paciente.getNome());
        editProntuario.setText(paciente.getNumeroProntuario().toString());
        calendario.setTime(paciente.getDataNascimento());
        editData.setText(UtilsDate.formatDate(CadastrarPacienteActivity.this, paciente.getDataNascimento()));
        radioGroupGeneros.check(paciente.getSexo() == GeneroEnum.MASCULINO ? R.id.buttonMasculino : R.id.buttonFeminino);
        editUnidade.setSelection(selecionarUnidade(paciente.getUnidadeInternacao()));
    }

    public void verificaDados() {
        if(isCamposPreenchidos() && isBotoesSelecionados()){
            salvarPaciente();
        } else {
            Toast.makeText(this,
                    R.string.toast_erro_cadastro,
                    Toast.LENGTH_SHORT).show();
            editUnidade.requestFocus();
        }
    }

    public void salvarPaciente() {
        String nome = editNome.getText().toString();
        String genero = String.valueOf(radioGroupGeneros.getCheckedRadioButtonId());
        Date dataNascimento = calendario.getTime();
        String prontuario = editProntuario.getText().toString();
        String unidade = editUnidade.getSelectedItem().toString();

        paciente.setNome(nome);
        paciente.setSexo(toGeneroEnum(genero));
        paciente.setDataNascimento(dataNascimento);
        paciente.setNumeroProntuario(Integer.valueOf(prontuario));
        paciente.setUnidadeInternacao(unidade);

        PacientesDatabase database = PacientesDatabase.getDatabase(this);
        if (modo == NOVO_PACIENTE){
            database.pacienteDao().insert(paciente);
        } else {
            database.pacienteDao().update(paciente);
        }

        setResult(Activity.RESULT_OK);
        finish();
    }

    private Integer selecionarUnidade(String unidade) {
        String[] unidades = getResources().getStringArray(R.array.array_unidades_internacao);
        int i = 0;
        for (String u : unidades) {
            if (u.equals(unidade)) {
                return i;
            } else i++;
        }
        return 0;
    }

    private GeneroEnum toGeneroEnum(String sexo) {
        return sexo.equals(String.valueOf(R.id.buttonMasculino)) ? GeneroEnum.MASCULINO : GeneroEnum.FEMININO;
    }

    public void limparCampos(){
        SpinnerAdapter adapter = editUnidade.getAdapter();
        editNome.setText(null);
        radioGroupGeneros.clearCheck();
        editData.setText(null);
        editProntuario.setText(null);
        editUnidade.setAdapter(null);
        editUnidade.setAdapter(adapter);
        editTermos.setChecked(false);

        Toast.makeText(this,
                R.string.toast_limpar_campos,
                Toast.LENGTH_LONG).show();

        editNome.requestFocus();
    }

    private boolean isCamposPreenchidos(){
        EditText[] edits =  {editNome, editData, editProntuario};
        for (EditText edit: edits) {
            if(TextUtils.isEmpty(edit.getText())) {
                edit.requestFocus();
                return false;
            }
        } return true;
    }

    private boolean isBotoesSelecionados() {
        if (radioGroupGeneros.getCheckedRadioButtonId() == -1) {
            return false;
        } else if (!editTermos.isChecked()){
            editTermos.requestFocus();
            return false;
        } else if (editUnidade.getSelectedItemId() == 0){
            editUnidade.requestFocus();
            return false;
        } else if (calendario.getTime().after(Calendar.getInstance().getTime())){
            return false;
        } else return true;
    }

    private void displayDatePicker(){
        calendario = Calendar.getInstance();
        editData.setOnClickListener(v -> {
            int day = calendario.get(Calendar.DAY_OF_MONTH);
            int month = calendario.get(Calendar.MONTH);
            int year = calendario.get(Calendar.YEAR);

            DatePickerDialog datePicker = new DatePickerDialog(
                    CadastrarPacienteActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    dateSetListener,
                    year, month, day);

            datePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePicker.show();
        });
        dateSetListener = (datePicker, year, month, day) -> {
            calendario.set(year, month, day);
            String data = UtilsDate.formatDate(this, calendario.getTime());
            editData.setText(data);
        };
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
package br.com.cogitare;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

import br.com.cogitare.model.Paciente;
import br.com.cogitare.persistence.PacientesDatabase;
import br.com.cogitare.model.GeneroEnum;

public class CadastrarPacienteActivity extends AppCompatActivity {

    private RadioGroup radioGroupGeneros;
    final Calendar calendario = Calendar.getInstance();
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
        editData.setText(paciente.getDataNascimento().toString());
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
        String data = editData.getText().toString();
        String prontuario = editProntuario.getText().toString();
        String unidade = editUnidade.getSelectedItem().toString();

        paciente = new Paciente(nome, toGeneroEnum(genero), toLocalDate(data), Integer.valueOf(prontuario), unidade);
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

    private LocalDate toLocalDate(String data) {
        DateTimeFormatter parser = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            parser = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return LocalDate.from(LocalDate.parse(data, parser));
        }
        return null;
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
        }
        return true;
    }

    private void displayDatePicker(){
        editData.setOnClickListener(v -> {
            int dia = calendario.get(Calendar.DAY_OF_MONTH);
            int mes = calendario.get(Calendar.MONTH);
            int ano = calendario.get(Calendar.YEAR);

            DatePickerDialog dialog = new DatePickerDialog(
                    CadastrarPacienteActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    dateSetListener,
                    dia, mes, ano);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });
        dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String data = String.format(Locale.getDefault(),"%04d/%02d/%02d", year, month, day);
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
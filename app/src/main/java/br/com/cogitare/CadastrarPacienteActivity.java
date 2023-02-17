package br.com.cogitare;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.Calendar;

public class CadastrarPacienteActivity extends AppCompatActivity {

    private RadioGroup radioGroupGeneros;
    final Calendar calendario = Calendar.getInstance();
    private EditText editNome, editData, editProntuario;
    private CheckBox editTermos;
    private Spinner editUnidade;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    public static final String KEY_NOME = "NOME";
    public static final String KEY_GENERO = "GENERO";
    public static final String KEY_DATA = "DATA";
    public static final String KEY_UNIDADE = "UNIDADE";
    public static final String KEY_PRONTUARIO = "PRONTUARIO";
    public static final String MODO    = "MODO";
    public static final int    NOVO    = 1;
    public static final int    ALTERAR = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_paciente);
        setTitle("Cadastro de pacientes");

        radioGroupGeneros = findViewById(R.id.groupGeneros);
        editNome = findViewById(R.id.edit_nome);
        editProntuario = findViewById(R.id.edit_prontuario);
        editUnidade = findViewById(R.id.edit_unidade_internacao);
        editTermos = findViewById(R.id.check_termos);

        editData = findViewById(R.id.edit_data);
        displayDatePicker();
    }

    public static void novoPaciente(AppCompatActivity activity) {
        Intent intent = new Intent(activity, CadastrarPacienteActivity.class);
        intent.putExtra(MODO, NOVO);
        activity.startActivityForResult(intent, NOVO);
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
        String data = year + "/" + month + "/" + day;
        editData.setText(data);
        };
    }

    public void limparCampos(View view){
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

    public void salvarPaciente(View view) {
        if(isCamposPreenchidos() && isBotoesSelecionados()){
            Toast.makeText(this,
                    R.string.toast_paciente_cadastrado,
                    Toast.LENGTH_SHORT).show();
                cadastrarPaciente();
        } else {
            Toast.makeText(this,
                    R.string.toast_erro_cadastro,
                    Toast.LENGTH_SHORT).show();
            editUnidade.requestFocus();
        }
    }

    public void cadastrarPaciente() {
        String nome = editNome.getText().toString();
        String genero = String.valueOf(radioGroupGeneros.getCheckedRadioButtonId());
        String data = editData.getText().toString();
        String prontuario = editProntuario.getText().toString();
        String unidade = editUnidade.getSelectedItem().toString();

        Intent intent = new Intent();
        intent.putExtra(KEY_NOME, nome);
        intent.putExtra(KEY_GENERO, genero);
        intent.putExtra(KEY_DATA, data);
        intent.putExtra(KEY_PRONTUARIO, prontuario);
        intent.putExtra(KEY_UNIDADE, unidade);

        setResult(Activity.RESULT_OK, intent);
        finish();
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

}
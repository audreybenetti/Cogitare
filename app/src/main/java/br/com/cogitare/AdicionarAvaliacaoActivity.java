package br.com.cogitare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import br.com.cogitare.model.Avaliacao;
import br.com.cogitare.model.RitmoCardiacoEnum;

public class AdicionarAvaliacaoActivity extends AppCompatActivity {

    private EditText editAusculta;
    private Spinner spinnerRitmoCardiaco;
    private Avaliacao avaliacao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_avaliacao);
        setTitle(R.string.adicionar_registro);

        editAusculta = findViewById(R.id.edit_ausculta);
        spinnerRitmoCardiaco = findViewById(R.id.spinner_ritmo_cardiaco);

        spinnerRitmoCardiaco.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, RitmoCardiacoEnum.values()));

        avaliacao = new Avaliacao();
    }



    public void salvarAvaliacao() {

        String ausculta = editAusculta.getText().toString();
        String ritmoCardiaco = spinnerRitmoCardiaco.getSelectedItem().toString();

        avaliacao = new Avaliacao(toRitmoEnum(ritmoCardiaco), Integer.valueOf(ausculta));
//        database.pacienteDao().insert(paciente);

        setResult(Activity.RESULT_OK);
        finish();
    }

    public static void adicionarAvaliacao(Activity activity){
        Intent intent = new Intent(activity, AdicionarAvaliacaoActivity.class);
        activity.startActivity(intent);
    }


    private RitmoCardiacoEnum toRitmoEnum(String ritmoCardiaco){
        return ritmoCardiaco.equals(RitmoCardiacoEnum.IRREGULAR.toString()) ? RitmoCardiacoEnum.IRREGULAR : RitmoCardiacoEnum.REGULAR;
    }
}

package br.com.cogitare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MostrarDadosPacienteActivity extends AppCompatActivity {

    public static final String KEY_NOME = "NOME";
    public static final String KEY_GENERO = "GENERO";
    public static final String KEY_DATA = "DATA";
    public static final String KEY_UNIDADE = "UNIDADE";
    public static final String KEY_PRONTUARIO = "PRONTUARIO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_dados_paciente);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            String nome = bundle.getString(KEY_NOME, getString(R.string.nome_nao_informado));
            String sexo = bundle.getString(KEY_GENERO, getString(R.string.sexo_nao_informado));
            String prontuario = bundle.getString(KEY_PRONTUARIO, getString(R.string.prontuario_nao_informado));
            String data = bundle.getString(KEY_DATA, getString(R.string.nascimento_nao_informado));
            String unidade = bundle.getString(KEY_UNIDADE, getString(R.string.unidade_nao_informada));

            String saida = R.string.nome_completo + " " + nome + "\n"
                    + R.string.sexo_paciente + " " + sexo + "\n"
                    + R.string.data_de_nascimento + " " + data + "\n"
                    + R.string.numero_prontuario + " " + prontuario + "\n"
                    + R.string.unidade_internacao + " " + unidade;

            TextView textViewDados = findViewById(R.id.textViewDadosPaciente);
            textViewDados.setText(saida);
        }
        setTitle(getString(R.string.paciente_cadastrado));
    }
}
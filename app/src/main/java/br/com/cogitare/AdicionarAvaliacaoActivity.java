package br.com.cogitare;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.sql.Date;
import java.util.Calendar;

import br.com.cogitare.model.Avaliacao;
import br.com.cogitare.model.RitmoCardiacoEnum;
import br.com.cogitare.persistence.PacientesDatabase;

public class AdicionarAvaliacaoActivity extends AppCompatActivity {

    private EditText editAusculta;
    private Spinner spinnerRitmoCardiaco;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_avaliacao);
        setTitle(R.string.adicionar_registro);

        editAusculta = findViewById(R.id.edit_ausculta);
        spinnerRitmoCardiaco = findViewById(R.id.spinner_ritmo_cardiaco);

        spinnerRitmoCardiaco.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, RitmoCardiacoEnum.values()));

        configuraBottomUp();
    }

    public static void adicionarAvaliacao(Activity activity){
        Intent intent = new Intent(activity, AdicionarAvaliacaoActivity.class);
        activity.startActivity(intent);
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

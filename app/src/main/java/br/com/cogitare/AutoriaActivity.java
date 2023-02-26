package br.com.cogitare;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AutoriaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autoria);
        setTitle("Autoria do aplicativo");

        configuraBottomUp();
    }

    public static void mostrarAutoria(AppCompatActivity activity) {
        Intent intent = new Intent(activity, AutoriaActivity.class);
        activity.startActivity(intent);
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
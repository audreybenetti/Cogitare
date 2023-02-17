package br.com.cogitare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AutoriaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autoria);
        setTitle("Autoria do aplicativo");
    }

    public static void mostrarAutoria(AppCompatActivity activity) {
        Intent intent = new Intent(activity, AutoriaActivity.class);
        activity.startActivity(intent);
    }
}
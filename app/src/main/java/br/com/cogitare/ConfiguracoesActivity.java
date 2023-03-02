package br.com.cogitare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;

public class ConfiguracoesActivity extends AppCompatActivity {

    private SwitchCompat darkModeSwitch;
    private boolean enableDarkMode;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("MODE", MODE_PRIVATE);

        if(loadDarkMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        setTitle("Settings");

        darkModeSwitch = findViewById(R.id.switchModoNoturno);

        if(loadDarkMode()){
            darkModeSwitch.setChecked(true);
        }

        darkModeSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                setDarkMode(true);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                setDarkMode(false);
            }
            restart();
        });

        loadDarkMode();
    }

    public void setDarkMode(Boolean state){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("DarkMode", state);
        editor.apply();
    }

    public boolean loadDarkMode(){
        return sharedPreferences.getBoolean("DarkMode", true);
    }

    public static void mostrarConfiguracoes(AppCompatActivity activity) {
        Intent intent = new Intent(activity, ConfiguracoesActivity.class);
        activity.startActivity(intent);
    }

    private void restart(){
        finish();
        startActivity(getIntent());
    }

}
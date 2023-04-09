package br.com.cogitare;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import br.com.cogitare.model.Paciente;
import br.com.cogitare.persistence.PacientesDatabase;
import br.com.cogitare.utils.PacienteAdapter;
import br.com.cogitare.utils.UtilsGUI;

public class ListarPacientesActivity extends AppCompatActivity {

    public static final int NOVO_PACIENTE = 1;
    public static final int ALTERAR_PACIENTE = 2;

    private ListView listViewPacientes;
    private PacienteAdapter pacienteAdapter;
    private boolean modoNoturnoDestaActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("MODE", MODE_PRIVATE);
        modoNoturnoDestaActivity = sharedPreferences.getBoolean("DarkMode", true);
        if(modoNoturnoDestaActivity) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_pacientes);
        setTitle(getString(R.string.titulo_lista_pacientes));
        listViewPacientes = findViewById(R.id.listViewPacientes);

        listViewPacientes.setOnItemClickListener((parent, view, position, id) -> {
            Paciente pacientes = (Paciente) parent.getItemAtPosition(position);
            CadastrarPacienteActivity.editarPaciente(ListarPacientesActivity.this,
                    pacientes);
        });

        registerForContextMenu(listViewPacientes);
        popularLista();
    }

    @Override
    protected void onResume() {
        SharedPreferences sharedPreferences = getSharedPreferences("MODE", MODE_PRIVATE);
        boolean modoSalvo = sharedPreferences.getBoolean("DarkMode", true);

        if (modoNoturnoDestaActivity != modoSalvo){
            if(modoSalvo){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            recreate();
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.listar_pacientes_opcoes, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemAdicionar:
                CadastrarPacienteActivity.novoPaciente(this);
                return true;
            case R.id.menuItemSobre:
                AutoriaActivity.mostrarAutoria(this);
                return true;
            case R.id.menuItemConfiguracoes:
                ConfiguracoesActivity.mostrarConfiguracoes(this);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.listar_pacientes_item_selecionado, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info;
        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Paciente paciente = (Paciente) listViewPacientes.getItemAtPosition(info.position);

        switch(item.getItemId()){
            case R.id.menuItemEditar:
                CadastrarPacienteActivity.editarPaciente(this,
                        paciente);
                return true;
            case R.id.menuItemExcluir:
                excluirPaciente(paciente);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void excluirPaciente(Paciente paciente){
        String mensagem = String.format(getString(R.string.confirmacao_deletar_paciente), paciente.getNome());

        DialogInterface.OnClickListener listener =
                (dialog, which) -> {

                    switch(which){
                        case DialogInterface.BUTTON_POSITIVE:
                            PacientesDatabase database = PacientesDatabase.getDatabase(ListarPacientesActivity.this);
                            database.pacienteDao().delete(paciente);
                            pacienteAdapter.remove(paciente);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                };

        UtilsGUI.confirmaAcao(this, mensagem, listener);

    }

    public void popularLista() {
        PacientesDatabase database = PacientesDatabase.getDatabase(this);
        List<Paciente> listaPacientes = database.pacienteDao().findAll();
        pacienteAdapter = new PacienteAdapter(this, listaPacientes);
        listViewPacientes.setAdapter(pacienteAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);
        if ((requestCode == NOVO_PACIENTE || requestCode == ALTERAR_PACIENTE) &&
                resultCode == Activity.RESULT_OK){

            popularLista();
        }
    }

}
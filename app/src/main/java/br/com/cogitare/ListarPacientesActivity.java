package br.com.cogitare;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class ListarPacientesActivity extends AppCompatActivity {

    private ListView listViewPacientes;
    private ArrayList<Paciente> listaPacientes;
    private PacienteAdapter pacienteAdapter;

    private ActionMode actionMode;
    private int pacienteSelecionado = -1;

    private final ActionMode.Callback mActionCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflate = mode.getMenuInflater();
            inflate.inflate(R.menu.listar_pacientes_item_selecionado, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch(item.getItemId()){
                case R.id.menuItemEditar:
                    editarPaciente();
                    mode.finish();
                    return true;
                case R.id.menuItemExcluir:
                    excluirPaciente();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            listViewPacientes.setEnabled(true);
        }
    };

    private void excluirPaciente(){
        listaPacientes.remove(pacienteSelecionado);
        pacienteAdapter.notifyDataSetChanged();
    }

    private void editarPaciente(){
        CadastrarPacienteActivity.editarPaciente(this, listaPacientes.get(pacienteSelecionado));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_pacientes);
        setTitle("Pacientes cadastrados");
        listViewPacientes = findViewById(R.id.listViewPacientes);

        listViewPacientes.setOnItemLongClickListener(
                (parent, view, position, id) -> {
                    if (actionMode != null){
                        return false;
                    }
                    pacienteSelecionado = position;
                    listViewPacientes.setEnabled(false);
                    actionMode = startSupportActionMode(mActionCallback);
                    return true;
                });

        popularLista();
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void popularLista() {
        listaPacientes = new ArrayList<>();
        pacienteAdapter = new PacienteAdapter(this, listaPacientes);
        listViewPacientes.setAdapter(pacienteAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent intent) {

        if (resultCode == Activity.RESULT_OK) {

            Bundle bundle = intent.getExtras();

            String nome = bundle.getString(CadastrarPacienteActivity.KEY_NOME);
            String sexo = bundle.getString(CadastrarPacienteActivity.KEY_GENERO);
            String prontuario = bundle.getString(CadastrarPacienteActivity.KEY_PRONTUARIO);
            String data = bundle.getString(CadastrarPacienteActivity.KEY_DATA);
            String unidade = bundle.getString(CadastrarPacienteActivity.KEY_UNIDADE);

            if (requestCode == CadastrarPacienteActivity.ALTERAR_PACIENTE) {
                alterarPaciente(nome, sexo, data, prontuario, unidade);
            } else {
                criarPaciente(nome, sexo, data, prontuario, unidade);
            }
            pacienteAdapter.notifyDataSetChanged();
        }
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

    private void alterarPaciente(String nome, String sexo, String data, String prontuario, String unidade) {
        Paciente paciente = listaPacientes.get(pacienteSelecionado);
        paciente.setNome(nome);
        paciente.setSexo(getGeneroEnum(sexo));
        paciente.setDataNascimento(paciente.getDataNascimento().toString().equals(data) ? paciente.getDataNascimento() : toLocalDate(data));
        paciente.setNumeroProntuario(Integer.valueOf(prontuario));
        paciente.setUnidadeInternacao(unidade);
    }

    private void criarPaciente(String nome, String sexo, String data, String prontuario, String unidade) {
        Paciente paciente = new Paciente(nome, Integer.parseInt(prontuario), unidade);
        paciente.setDataNascimento(toLocalDate(data));
        paciente.setSexo(getGeneroEnum(sexo));
        listaPacientes.add(paciente);
    }

    private GeneroEnum getGeneroEnum(String sexo) {
        return sexo.equals(String.valueOf(R.id.buttonMasculino)) ? GeneroEnum.MASCULINO : GeneroEnum.FEMININO;
    }
}
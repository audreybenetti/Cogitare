package br.com.cogitare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class PacienteAdapter extends BaseAdapter {

    private final Context context;
    private final List<Paciente> pacientes;

    public PacienteAdapter(Context context, List<Paciente> pessoas) {
        this.context = context;
        this.pacientes = pessoas;
    }

    private static class PacienteHolder {
        public TextView textViewValorNome;
        public TextView textViewValorSexo;
        public TextView textViewValorProntuario;
        public TextView textViewValorUnidade;
    }

    @Override
    public int getCount() {
        return pacientes.size();
    }

    @Override
    public Object getItem(int i) {
        return pacientes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PacienteHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.linha_lista_pacientes, viewGroup, false);

            holder = new PacienteHolder();
            holder.textViewValorNome = view.findViewById(R.id.textViewValorNome);
            holder.textViewValorSexo = view.findViewById(R.id.textViewValorGenero);
            holder.textViewValorProntuario = view.findViewById(R.id.textViewValorProntuario);
            holder.textViewValorUnidade = view.findViewById(R.id.textViewValorUnidade);

            view.setTag(holder);
        } else {
            holder = (PacienteHolder) view.getTag();
        }

        holder.textViewValorNome.setText(pacientes.get(i).getNome());
        switch (pacientes.get(i).getSexo()) {
            case FEMININO:
                holder.textViewValorSexo.setText(R.string.sexo_feminino);
                break;
            case MASCULINO:
                holder.textViewValorSexo.setText(R.string.sexo_masculino);
                break;
        }
        holder.textViewValorProntuario.setText(Integer.toString(pacientes.get(i).getNumeroProntuario()));
        holder.textViewValorUnidade.setText(pacientes.get(i).getUnidadeInternacao());
        return view;
    }

}

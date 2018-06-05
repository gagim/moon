package com.example.henrique.blocodeanotas.activitys;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.henrique.blocodeanotas.R;
import com.example.henrique.blocodeanotas.banco.ControladorSQLite;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Cursor cursor;
    private ControladorSQLite controladorSQLite = new ControladorSQLite(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pesquisar();
    }

    private void pesquisar(){
        buscarDados();
        ListView lsAnotacoes = findViewById(R.id.ls_itens);
        if (cursor.getCount() > 0) {
            TextView semAnotacao = findViewById(R.id.semAnotacao);
            semAnotacao.setVisibility(View.GONE);
            if (cursor != null) {
                String[] nomeCampos = new String[]{"_id", "nome"};
                int[] idViews = new int[]{R.id.txtId, R.id.txt_nome,};

                SimpleCursorAdapter ap = new SimpleCursorAdapter(MainActivity.this, R.layout.mostar_anotacao, cursor, nomeCampos, idViews);

                lsAnotacoes.setAdapter(ap);
                lsAnotacoes.setOnItemClickListener(MainActivity.this);
            }
        }
    }

    private void mostrarOpcoes(final long id){
        AlertDialog.Builder salvar = new AlertDialog.Builder(MainActivity.this);
        salvar.setTitle("O que deseja fazer?");
        salvar.setPositiveButton("Alterar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alterar(id);
            }
        });
        salvar.setNegativeButton("Apagar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletar(id);
            }
        });
        salvar.setNeutralButton("Mostrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { mostrarAnotacao((int) id);
            }
        });
        salvar.create();
        salvar.show();
    }

    private void deletar(final long id){
        AlertDialog.Builder salvar = new AlertDialog.Builder(MainActivity.this);
        salvar.setTitle("Deseja mesmo deletar essa anotação?");
        salvar.setCancelable(false);
        salvar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                controladorSQLite.deletarAnotacao(id);
                recreate();
                Toast.makeText(getApplicationContext(), "Deletado com sucesso!", Toast.LENGTH_SHORT).show();

            }
        });
        salvar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        salvar.create();
        salvar.show();
    }

    private void buscarDados(){
        try {
            cursor = controladorSQLite.buscarAnotacoes() ;
        }catch (Exception e){
            Toast.makeText(MainActivity.this,"error ao buscar os dados!",Toast.LENGTH_SHORT).show();
        }
    }

    public void add(View view){
        Intent intent = new Intent(MainActivity.this,TextActivity.class);
        startActivity(intent);
        onStop();
    }

    private void alterar(long id){
        Intent intent = new Intent(MainActivity.this,MostrarActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
        onPause();
    }

    private void mostrarAnotacao(int idAnotacao){
        @SuppressLint("Recycle")
        Cursor cursor = controladorSQLite.buscarAnotacao(idAnotacao);
        if (cursor.moveToNext()) {
            LayoutInflater li = LayoutInflater.from(this);
            @SuppressLint("InflateParams")
            View view = li.inflate(R.layout.layout_anotacao, null);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            AlertDialog bui = alertDialog.create();
            EditText editTituloAnotacao = view.findViewById(R.id.txt_titulo_anotacao);
            EditText editMensagemAnotacao = view.findViewById(R.id.txt_mensagem_anotacao);
            bui.setView(view);
            bui.create();
            bui.show();
            String tituloAnotacao = cursor.getString(1);
            String mensagemAnotacao = cursor.getString(2);
            editTituloAnotacao.setText(tituloAnotacao);
            editMensagemAnotacao.setText(mensagemAnotacao);
        }else {
            Toast.makeText(getApplicationContext(), "Não possue nenhum registro", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        pesquisar();
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mostrarOpcoes(id);
    }

}

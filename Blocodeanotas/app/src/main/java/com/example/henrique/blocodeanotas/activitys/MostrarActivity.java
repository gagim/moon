package com.example.henrique.blocodeanotas.activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.henrique.blocodeanotas.R;
import com.example.henrique.blocodeanotas.banco.ControladorSQLite;

public class MostrarActivity extends Activity {

    private TextView nome,mensagem;
    private int idAnotacao;
    private ControladorSQLite controladorSQLite = new ControladorSQLite(this);
    private String nomeAnotacao,mensagemAnotacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar);

        Intent intent = getIntent();
        Bundle dados;
        dados = intent.getExtras();
        assert dados != null;
        idAnotacao= (int) dados.getLong("id");

        nome = findViewById(R.id.txt_mostar);
        mensagem = findViewById(R.id.txtConteudo);

        pesquisar();
        onDestroy();
    }

    public void pesquisar(){
        @SuppressLint("Recycle")
        Cursor cursor = controladorSQLite.buscarAnotacao(idAnotacao);
        if (cursor.moveToNext()) {
            nomeAnotacao = cursor.getString(1);
            mensagemAnotacao = cursor.getString(2);
            nome.setText(nomeAnotacao);
            mensagem.setText(mensagemAnotacao);
        }else {
            Toast.makeText(getApplicationContext(), "Não possue nenhum registro", Toast.LENGTH_SHORT).show();
        }
    }

    public void alterar(View view){
        nomeAnotacao = nome.getText().toString();
        mensagemAnotacao = mensagem.getText().toString();
        if (nomeAnotacao.isEmpty() || mensagemAnotacao.isEmpty()){
            Toast.makeText(this, "Nenhum campo deve está vazio!", Toast.LENGTH_SHORT).show();
        }else {
            if (mensagem.length() > 200 || nome.length() > 10) {
                Toast.makeText(this, "Algum campo excede o limite de caracteres!", Toast.LENGTH_SHORT).show();
            } else {
                alterarAnotacao();
                Toast.makeText(this, "Sucesso ao atlterar!", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }
    }

    public void alterarAnotacao(){
        controladorSQLite.alterarAnotacao(nomeAnotacao,mensagemAnotacao,idAnotacao);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}

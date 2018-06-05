package com.example.henrique.blocodeanotas.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ControladorSQLite {

    private BancoSQLite banco;
    private String tabela = "item_anotacao";
    private ContentValues valores = new ContentValues();
    private SQLiteDatabase db;

    public ControladorSQLite(Context context){
        banco = new BancoSQLite(context);
    }

    public void inserirAnotacao(String name, String mensagem) {
        db = banco.getWritableDatabase();
        valores.put(BancoSQLite.NOME_ANOTACAO, name);
        valores.put(BancoSQLite.MENSAGEM_ANOTACAO, mensagem);
        db.insert(tabela,null,valores);
        db.close();

    }

    public void alterarAnotacao(String nomeAnotacao, String mensagemAnotacao, int idAnotacao){
        db = banco.getWritableDatabase();
        valores.put(BancoSQLite.NOME_ANOTACAO, nomeAnotacao);
        valores.put(BancoSQLite.MENSAGEM_ANOTACAO, mensagemAnotacao);
        db.update(tabela,valores,"_id = " + idAnotacao,null);
        db.close();
    }

    public void deletarAnotacao(long id){
        db = banco.getWritableDatabase();
        db.delete(tabela,  "_id = " + id, null);
        db.close();
    }

    public Cursor buscarAnotacoes(){
        db = banco.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + tabela,null);
    }

    public Cursor buscarAnotacao(int idAnotacao){
        db = banco.getReadableDatabase();
        return db.rawQuery("SELECT * FROM item_anotacao WHERE _id =" + idAnotacao, null);
    }

}

package com.example.henrique.blocodeanotas.banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BancoSQLite extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "dbanotacao";

    //Tabela anotacaoes
    private static final String TABELA = "item_anotacao";
    private static final String ID_ANOTACAO = "_id";
    public static final String NOME_ANOTACAO = "nome";
    public static final String MENSAGEM_ANOTACAO = "mensagem";
    public static final String AUDIO_ANOTACAO = "audio";


    //versão
    private static final int VERSAO = 5;

    BancoSQLite(Context ctx) {
        super(ctx, NOME_BANCO, null, VERSAO);
    }

    //cria o db SQLite
    @Override
    public void onCreate(SQLiteDatabase db) {

        String tabela = "CREATE TABLE IF NOT EXISTS "+ TABELA + "("
                + ID_ANOTACAO + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NOME_ANOTACAO  + " VARCHAR(10) not null, "
                + MENSAGEM_ANOTACAO + " VARCHAR(200) not null, "
                + AUDIO_ANOTACAO + " VARCHAR(250) not null "
                + ")";

        db.execSQL(tabela);

    }

    //deleta a tabela item_anotacao do bd SQLite
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABELA);
        onCreate(db);
    }

}

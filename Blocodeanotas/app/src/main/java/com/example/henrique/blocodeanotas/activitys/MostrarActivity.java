package com.example.henrique.blocodeanotas.activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.henrique.blocodeanotas.R;
import com.example.henrique.blocodeanotas.banco.ControladorSQLite;

import java.io.IOException;
import java.util.UUID;

public class MostrarActivity extends Activity {

    private ImageView img_new_gravacao;
    private MediaPlayer mediaPlayer;
    private MediaRecorder recorder;
    public static final int request_code = 1000;
    private String arquivo= "@",subArquivo;
    private Boolean if_reproduzindo = false,if_pause = false;
    private AlertDialog bui;
    private AlertDialog.Builder alertDialog;
    private View view;

    private ImageView img_reproduzir,img_delete_audio;

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

        img_new_gravacao = findViewById(R.id.img_new_gravacao_alterar);
        img_delete_audio = findViewById(R.id.img_delete_audio_alterar);
        img_reproduzir = findViewById(R.id.img_reproduzir_alterar);

        img_new_gravacao.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InflateParams")
            @Override
            public void onClick(View v) {

                if (checkPermissionFromDevice()){

                    LayoutInflater li = LayoutInflater.from(MostrarActivity.this);
                    view = li.inflate(R.layout.layout_gravacao, null);
                    alertDialog = new AlertDialog.Builder(MostrarActivity.this);
                    bui = alertDialog.create();
                    bui.setCancelable(false);

                    final ImageView btn_gravar = view.findViewById(R.id.btn_gravar);
                    final ImageView btn_play = view.findViewById(R.id.btn_play);
                    final ImageView btn_pause = view.findViewById(R.id.btn_pause);
                    final ImageView btn_stop = view.findViewById(R.id.btn_stop);
                    final ImageView btn_reset = view.findViewById(R.id.btn_reset);

                    final TextView txt_status = view.findViewById(R.id.txt_status);

                    btn_pause.setEnabled(false);
                    btn_play.setEnabled(false);
                    btn_stop.setEnabled(false);

                    btn_gravar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            gravar();
                            if_pause = false;
                            if_reproduzindo =false;

                            btn_gravar.setImageResource(R.drawable.ic_action_mic_red);
                            btn_gravar.setEnabled(false);

                            btn_pause.setImageResource(R.drawable.ic_action_playback_pause);
                            btn_pause.setEnabled(true);

                            btn_play.setEnabled(false);

                            btn_stop.setImageResource(R.drawable.ic_action_playback_stop);
                            btn_stop.setEnabled(true);

                            txt_status.setText("Gravando...");
                        }
                    });
                    btn_pause.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            pause();
                            btn_gravar.setImageResource(R.drawable.ic_action_mic_green);
                            btn_gravar.setEnabled(true);
                            btn_play.setImageResource(R.drawable.ic_action_playback_play);
                            btn_play.setEnabled(true);
                            btn_pause.setEnabled(false);
                            btn_stop.setImageResource(R.drawable.ic_action_playback_stop);
                            btn_stop.setEnabled(true);
                            txt_status.setText("Pausado");
                        }
                    });
                    btn_play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (if_pause ){
                                resume();
                            }else {
                                start();
                            }

                            if_reproduzindo = true;

                            btn_gravar.setImageResource(R.drawable.ic_action_mic_red);
                            btn_gravar.setEnabled(false);

                            btn_play.setImageResource(R.drawable.ic_action_playback_play_red);
                            btn_play.setEnabled(false);

                            btn_pause.setImageResource(R.drawable.ic_action_playback_pause);
                            btn_pause.setEnabled(true);

                            btn_stop.setImageResource(R.drawable.ic_action_playback_stop);
                            btn_stop.setEnabled(true);
                            txt_status.setText("Reproduzindo...");
                        }
                    });
                    btn_stop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            finalizarGravacao();
                        }
                    });

                    btn_reset.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            resetGravacao();
                        }
                    });

                    bui.setView(view);
                    bui.create();
                    bui.show();
                }
             else {

                requestPermissionFromDevice();
            }
            }

        });

        pesquisar();
        onDestroy();
    }

    private void requestPermissionFromDevice() {
        ActivityCompat.requestPermissions(this,new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO},
                request_code);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case request_code:
            {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    img_new_gravacao.setImageResource(R.drawable.ic_action_mic);
                    img_new_gravacao.setEnabled(true);
                }
                else {
                    img_new_gravacao.setImageResource(R.drawable.ic_action_micoff);
                    img_new_gravacao.setEnabled(false);
                }
            }
            break;
        }
    }

    private boolean checkPermissionFromDevice() {
        int storage_permission= ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int recorder_permssion=ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
        return storage_permission == PackageManager.PERMISSION_GRANTED && recorder_permssion == PackageManager.PERMISSION_GRANTED;
    }

    public void pesquisar(){
        @SuppressLint("Recycle")
        Cursor cursor = controladorSQLite.buscarAnotacao(idAnotacao);
        if (cursor.moveToNext()) {
            nomeAnotacao = cursor.getString(1);
            mensagemAnotacao = cursor.getString(2);
            nome.setText(nomeAnotacao);
            mensagem.setText(mensagemAnotacao);
            subArquivo = cursor.getString(3);
            if (subArquivo.contains("@")){
                img_delete_audio.setVisibility(View.GONE);
            }
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
        if (arquivo.contains("@")){
            controladorSQLite.alterarAnotacao(nomeAnotacao,mensagemAnotacao,idAnotacao,subArquivo);
        }else if (arquivo.contains("!")) {
            controladorSQLite.alterarAnotacao(nomeAnotacao,mensagemAnotacao,idAnotacao,"@");
        }else {
            controladorSQLite.alterarAnotacao(nomeAnotacao,mensagemAnotacao,idAnotacao,arquivo);
        }
    }

    private void pause(){
        if (if_reproduzindo) {
            mediaPlayer.pause();
            if_pause = true;
        }else {
            recorder.stop();
        }
    }

    private void resume(){
        mediaPlayer.start();
    }

    private void start(){
        mediaPlayer=new MediaPlayer();
        try {
            mediaPlayer.setDataSource(arquivo);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reproduzirAudio(){
        mediaPlayer=new MediaPlayer();
        try {
            mediaPlayer.setDataSource(arquivo);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deletarAudio(){
            if (recorder != null) {
                arquivo = "@";
                recorder.reset();
            }else {
                arquivo = "!";
            }
            img_reproduzir.setVisibility(View.GONE);
            img_delete_audio.setVisibility(View.GONE);
            img_new_gravacao.setVisibility(View.VISIBLE);
        Toast.makeText(MostrarActivity.this,"Audio anterior deletado!",Toast.LENGTH_SHORT).show();
    }

    public void deletar(View view){
        deletarAudio();
    }

    public void reproduzir(View view){
        reproduzirAudio();
    }

    private void gravar(){
        arquivo= Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+
                UUID.randomUUID()+"AudioFile.3gp";

        SetupMediaRecorder();

        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void SetupMediaRecorder() {
        recorder=new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(arquivo);
    }

    private void finalizarGravacao(){
        SetupMediaRecorder();
        bui.dismiss();
        img_reproduzir.setVisibility(View.VISIBLE);
        img_delete_audio.setVisibility(View.VISIBLE);
        img_new_gravacao.setVisibility(View.GONE);
    }

    private void resetGravacao(){
        if (recorder != null){
            recorder.reset();
            bui.dismiss();
        }else {
            bui.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}

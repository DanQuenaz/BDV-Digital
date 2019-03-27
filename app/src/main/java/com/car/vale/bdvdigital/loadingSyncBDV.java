package com.car.vale.bdvdigital;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONException;

import estruturas.ThreadUploadDados;
import estruturas.UploadStatus;
import interfaces.HttpCon;

public class loadingSyncBDV extends AppCompatActivity {
    private AnimationDrawable animacao;
    private ImageView imagem;

    public static Activity _tela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_carregamento);

        _tela = this;

        this.imagem = (ImageView)findViewById(R.id.ldgGeral);
        this.animacao = (AnimationDrawable)imagem.getDrawable();

        HttpCon ws = new HttpCon(loadingSyncBDV.this);

        ThreadUploadDados thread = new ThreadUploadDados();
        thread.start();

        try {
            if(!UploadStatus.get_bdv()) ws.CallBDVRequest(loadingSyncBDV.this, getString(R.string.msg_bdvs_sincronizados), getString(R.string.msg_erro_conexao_servidor));

            if(!UploadStatus.get_checklist()) ws.CallCheckListRequest(loadingSyncBDV.this, getString(R.string.msg_bdvs_sincronizados), getString(R.string.msg_erro_conexao_servidor));

            if(!UploadStatus.get_horaextra()) ws.CallHoraExtraRequest(loadingSyncBDV.this, getString(R.string.msg_bdvs_sincronizados), getString(R.string.msg_erro_conexao_servidor));

            if(!UploadStatus.get_custosmotorista()) ws.CallCustosMotoristaRequest(loadingSyncBDV.this, getString(R.string.msg_bdvs_sincronizados), getString(R.string.msg_erro_conexao_servidor));
        } catch (JSONException e) {
            Log.e("", e.getMessage());
        }

        this.animacao.start();
    }
}

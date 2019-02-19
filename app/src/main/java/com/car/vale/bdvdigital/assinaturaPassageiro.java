package com.car.vale.bdvdigital;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import estruturas.AssinaturaPassageiro;
import estruturas.AssinaturasBDV;

public class assinaturaPassageiro extends AppCompatActivity {
    private Button btnOK;
    private RatingBar rbBDV;
    private EditText edtNome;
    private EditText edtMatricula;
    private EditText edtObservacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assinatura_passageiro);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        Integer width = dm.widthPixels;
        Integer heigth = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(heigth*.58));

        this.edtNome = (EditText)findViewById(R.id.edtNomePassageiro);
        this.edtMatricula = (EditText)findViewById(R.id.edtMatriculaPassageiro);
        this.edtObservacao = (EditText)findViewById(R.id.edtObservacao);

        this.btnOK = (Button)findViewById(R.id.btnOkAss);
        this.rbBDV = (RatingBar)findViewById(R.id.ratingBar);

        AssinaturasBDV.getInstance();

        this.btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean _status = true;
                String nome, matricula, observacao;
                Integer avaliacao;

                if(edtNome.getText().toString().isEmpty()){
                    edtNome.setError(getString(R.string.campo_obrigatorio));
                    _status = false;
                }

                if(edtMatricula.getText().toString().isEmpty()){
                    edtMatricula.setError(getString(R.string.campo_obrigatorio));
                    _status = false;
                }

                if(rbBDV.getRating() == 0){
                    Toast.makeText(getApplicationContext(), getString(R.string.msg_avalicao_necessaria), Toast.LENGTH_LONG).show();
                    _status = false;
                }

                nome = edtNome.getText().toString();
                matricula = edtMatricula.getText().toString();
                observacao = edtObservacao.getText().toString();
                avaliacao = (int)(rbBDV.getRating());

                if(_status){
                    AssinaturaPassageiro aux = new AssinaturaPassageiro(nome, matricula, observacao, avaliacao);
                    AssinaturasBDV.addAssinatura(aux);
                    Toast.makeText(getApplicationContext(), getString(R.string.msg_avaliacao_realizada), Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

    }
}

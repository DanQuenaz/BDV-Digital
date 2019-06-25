package com.car.vale.bdvdigital;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import estruturas.Motorista;
import interfaces.BancoDados;
import interfaces.HttpCon;

public class alteraSenha extends AppCompatActivity {
    public static Activity _tela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altera_senha);

        _tela = this;

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        Integer width = dm.widthPixels;
        Integer heigth = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(heigth*.58));

        final EditText edtSenhaAtual = (EditText) findViewById(R.id.edtSenhaAtual);
        final EditText edtNovaSenha1 = (EditText) findViewById(R.id.edtNovaSenha);
        final EditText edtNovaSenha2 = (EditText) findViewById(R.id.edtNovaSenha2);
        Button btnOkSenha = (Button) findViewById(R.id.btnOkSenha);

        btnOkSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BancoDados  bd = new BancoDados(getApplicationContext());
                String matricula = Motorista.get_matricula();
                String senhaAtual = edtSenhaAtual.getText().toString();
                String novaSenha1 = edtNovaSenha1.getText().toString();
                String novaSenha2 = edtNovaSenha2.getText().toString();
                Boolean _status = true;
                if(senhaAtual.isEmpty()){
                    edtSenhaAtual.setError(getString(R.string.campo_obrigatorio));
                    _status = false;
                }
                if(novaSenha1.isEmpty()){
                    edtNovaSenha1.setError(getString(R.string.campo_obrigatorio));
                    _status = false;
                }
                if(novaSenha2.isEmpty()){
                    edtNovaSenha2.setError(getString(R.string.campo_obrigatorio));
                    _status = false;
                }
                if(!novaSenha1.equals(novaSenha2)){
                    edtNovaSenha1.setError(getString(R.string.erro_senha_diferente));
                    _status = false;
                }
                if(!bd.checaSenha(matricula, senhaAtual)){
                    edtSenhaAtual.setError(getString(R.string.error_invalid_password));
                    _status = false;
                }
                if(_status){
                    HttpCon hc = new HttpCon(getApplicationContext());
                    JSONObject jo = new JSONObject();
                    try {
                        jo.put("matricula", matricula);
                        jo.put("nova_senha", novaSenha1);
                        String dados = jo.toString();
                        hc.CallChangePasswordDriverRequest(getApplicationContext(), matricula, novaSenha1, dados);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        });
    }
}

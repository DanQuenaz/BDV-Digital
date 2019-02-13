package com.car.vale.bdvdigital;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import estruturas.BancoDados;
import estruturas.Comunicator;
import estruturas.Motorista;
import estruturas.WebInterface;

public class MainActivity extends AppCompatActivity {
    private BancoDados db;
    private ImageButton insereTest;
    private ImageButton logaMotorista;
    private ImageButton btnOpenVeiculoConfig;
    private WebInterface ws;
    private Boolean _status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        }

        //startActivity(new Intent(this, teste.class));

        this.db = new BancoDados(getBaseContext());

        this.db.insereMotorista("81001", "Joao", "1224324", "4353456", "senha123");
        this.ws = new WebInterface(MainActivity.this);

        this.btnOpenVeiculoConfig = (ImageButton) findViewById(R.id.btnOpenVeiculoConfig);
        this.logaMotorista = (ImageButton) findViewById(R.id.btnLogin);

        this._status = true;

        this.logaMotorista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText aux1 = (EditText)(findViewById(R.id.txtMatricula));
                EditText aux2 = (EditText)(findViewById(R.id.txtSenha));

                String matricula = aux1.getText().toString();
                String senha = aux2.getText().toString();

                if(matricula.isEmpty()){
                    aux1.setError(getString(R.string.error_field_required));
                    _status = false;
                }
                if(senha.isEmpty()){
                    aux2.setError(getString(R.string.error_field_required));
                    _status = false;
                }

                if(!db.setVeiculoConfig()){
                    Toast.makeText(getApplicationContext(), "Configure o veículo antes de logar!", Toast.LENGTH_LONG).show();
                    _status = false;
                }

                if(!db.logaMotorista(matricula, senha)){
                    Toast.makeText(getApplicationContext(), "Erro de login!", Toast.LENGTH_LONG).show();
                    _status = false;
                }

                if(_status){
                    startActivity( new Intent(getApplicationContext(), CheckList.class));
                    //finish();
                }
            }
        });

        this.insereTest = (ImageButton) findViewById(R.id.buttonTest);
        this.insereTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ws.CallJsonAR(MainActivity.this);


            }
        });


        this.btnOpenVeiculoConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(getBaseContext(), veiculoConfig.class) );
            }
        });


    }
}

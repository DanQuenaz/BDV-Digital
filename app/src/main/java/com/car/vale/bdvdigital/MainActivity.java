package com.car.vale.bdvdigital;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import estruturas.Motorista;
import interfaces.BancoDados;
import estruturas.Configuracao;
import interfaces.HttpCon;

public class MainActivity extends Activity {
    private BancoDados db;
    private Button atualizaMotorista;
    private Button logaMotorista;
    private HttpCon ws;
    private Boolean _status;

    public static Activity _tela;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _tela = this;

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        //startActivity(new Intent(this, cadastroCustoMotorista.class));
        //Log.i("", ""+Trajeto.getKmTrajeto(-19.8609648,-43.9494139, -19.8609584,-43.9468089));

        Configuracao.inicializa();

        this.db = new BancoDados(getBaseContext());

        //this.db.insereMotorista("81001", "Joao", "1224324", "4353456", "senha123");
        this.ws = new HttpCon(MainActivity.this);
        this.logaMotorista = (Button) findViewById(R.id.buttonLogar);

        this._status = true;

        Configuracao.setReserva(false);

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
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String hour = format.format(new Date());

                    Motorista.setHora_login(hour);

                    startActivity( new Intent(getApplicationContext(), CheckList.class));
                    MainActivity._tela.finish();
                }
                _status = true;
            }
        });

        this.atualizaMotorista = (Button) findViewById(R.id.buttonAtualizaMotorista);
        this.atualizaMotorista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ws.CallJsonAR(MainActivity.this, getString(R.string.msg_erro_conexao_servidor));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuItem m1 = menu.add(0,0,0,"CONFIGURAR VEÍCULO");
        m1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        m1.setIcon(android.R.drawable.ic_menu_preferences);

        return true;
    }

    @Override
    public boolean onMenuItemSelected(int parse, MenuItem id){

        switch(id.getItemId()){
            case 0:
                startActivity( new Intent(getBaseContext(), veiculoConfig.class) );
                break;
        }
        return true;
    }
}


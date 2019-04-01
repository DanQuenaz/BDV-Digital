package com.car.vale.bdvdigital;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import estruturas.BDV;
import estruturas.Configuracao;
import estruturas.Motorista;
import estruturas.VeiculoConfig;
import interfaces.BancoDados;

public class kmInicial extends Activity {
    private EditText edtKminicial;
    private Button btnKminicial;

    private TextView txtMotoristaLogado;
    private TextView txtInfoVeiculo;

    private CheckBox cbReserva;
    private EditText edtReserva;

    public static Activity _tela;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_km_inicial);

        _tela = this;

        BancoDados db = new BancoDados(getApplicationContext());

        this.edtKminicial = (EditText) findViewById(R.id.edtKmInicial);
        this.edtKminicial.setText(db.getUltimoKM().toString());

        this.btnKminicial = (Button) findViewById(R.id.btnKmInicial);

        this.txtMotoristaLogado = (TextView)findViewById(R.id.txtMotorislaLogado);
        this.txtInfoVeiculo = (TextView)findViewById(R.id.txtInfoVeiculo);

        this.txtMotoristaLogado.setText(Motorista.get_nome());
        this.txtInfoVeiculo.setText("Veículo: "+ VeiculoConfig.getVeiculoModelo() + " Placa:" + VeiculoConfig.getVeiculoPlaca()+"\nCartela: "+VeiculoConfig.getVeiculoCartela() );

        this.cbReserva = (CheckBox)findViewById(R.id.cbReserva);
        this.edtReserva = (EditText)findViewById(R.id.edtPlacaReserva);

        this.edtReserva.setEnabled(false);

        this.cbReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( cbReserva.isChecked() ){
                    edtReserva.setEnabled(true);
                }else{
                    edtReserva.setText("");
                    edtReserva.setEnabled(false);
                }
            }
        });


        this.btnKminicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean _status = true;

                if( cbReserva.isChecked() ){
                    if(edtReserva.getText().toString().isEmpty()){
                        _status = false;
                        edtReserva.setError(getString(R.string.campo_obrigatorio));
                    }
                }

                if( edtKminicial.getText().toString().isEmpty()){
                    _status = false;
                    edtKminicial.setError(getString(R.string.campo_obrigatorio));
                }

                if(_status){
                    if( cbReserva.isChecked() ){
                        Configuracao.setReserva(true);
                        Configuracao.setPlacaReserva(edtReserva.getText().toString());
                    }

                    Float kmInicail = Float.parseFloat(edtKminicial.getText().toString());
                    BDV.setKm_inicial(kmInicail);


                    Intent intent = new Intent(getApplicationContext(), logadoMotorista.class);
                    startActivity(intent);

                    kmInicial._tela.finish();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuItem m1 = menu.add(0,0,0,"SAIR");
        m1.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        return true;
    }

    @Override
    public boolean onMenuItemSelected(int parse, MenuItem id){

        switch(id.getItemId()){
            case 0:
                deslogar();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(kmInicial.this, MainActivity.class));
        kmInicial._tela.finish();
    }

    private void deslogar(){
        startActivity(new Intent(kmInicial.this, MainActivity.class));
        kmInicial._tela.finish();
    }
}

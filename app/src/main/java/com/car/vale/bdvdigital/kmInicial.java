package com.car.vale.bdvdigital;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import estruturas.BDV;
import estruturas.Configuracao;
import estruturas.Motorista;
import estruturas.VeiculoConfig;

public class kmInicial extends AppCompatActivity {
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

        this.edtKminicial = (EditText) findViewById(R.id.edtKmInicial);
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
}

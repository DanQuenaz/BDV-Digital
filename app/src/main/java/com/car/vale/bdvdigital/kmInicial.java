package com.car.vale.bdvdigital;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import estruturas.BDV;
import estruturas.CarroReserva;
import estruturas.Comunicator;
import estruturas.Motorista;
import estruturas.VeiculoConfig;

public class kmInicial extends AppCompatActivity {
    private EditText edtKminicial;
    private Button btnKminicial;

    private TextView txtMotoristaLogado;
    private TextView txtInfoVeiculo;

    private CheckBox cbReserva;
    private EditText edtReserva;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_km_inicial);

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
                        CarroReserva.setReserva(true);
                        CarroReserva.setPlacaReserva(edtReserva.getText().toString());
                    }

                    Float kmInicail = Float.parseFloat(edtKminicial.getText().toString());
                    BDV.setKm_inicial(kmInicail);

                    Intent intent = new Intent(getApplicationContext(), logadoMotorista.class);
                    startActivity(intent);
                }
            }
        });

    }
}

package com.car.vale.bdvdigital;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import estruturas.BDV;
import estruturas.Comunicator;
import estruturas.Motorista;
import estruturas.VeiculoConfig;

public class kmInicial extends AppCompatActivity {
    private EditText edtKminicial;
    private ImageButton btnKminicial;

    private TextView txtMotoristaLogado;
    private TextView txtInfoVeiculo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_km_inicial);


        this.edtKminicial = (EditText) findViewById(R.id.edtKmInicial);
        this.btnKminicial = (ImageButton) findViewById(R.id.btnKmInicial);

        this.txtMotoristaLogado = (TextView)findViewById(R.id.txtMotorislaLogado);
        this.txtInfoVeiculo = (TextView)findViewById(R.id.txtInfoVeiculo);

        this.txtMotoristaLogado.setText(Motorista.get_nome());
        this.txtInfoVeiculo.setText("Veículo: "+ VeiculoConfig.getVeiculoModelo() + " Placa:" + VeiculoConfig.getVeiculoPlaca()+"\nCartela: "+VeiculoConfig.getVeiculoCartela() );

        this.btnKminicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtKminicial.getText().toString() != ""){
                    Float kmInicail = Float.parseFloat(edtKminicial.getText().toString());
                    BDV.setKm_inicial(kmInicail);

                    Intent intent = new Intent(getApplicationContext(), logadoMotorista.class);
                    startActivity(intent);
                }
            }
        });

    }
}

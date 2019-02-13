package com.car.vale.bdvdigital;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import estruturas.BDV;
import estruturas.BancoDados;
import estruturas.Comunicator;
import estruturas.Motorista;
import estruturas.VeiculoConfig;

public class kmFinal extends AppCompatActivity {
    private ImageButton btnKmFinal;
    private EditText edtKmFinal;
    private TextView txtMotoristaLogado;
    private TextView txtInfoVeiculo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_km_final);

        this.txtMotoristaLogado = (TextView)findViewById(R.id.pctName1);
        this.txtInfoVeiculo = (TextView)findViewById(R.id.pctAge1);

        this.txtMotoristaLogado.setText(Motorista.get_nome());
        this.txtInfoVeiculo.setText("Veículo: "+ VeiculoConfig.getVeiculoModelo() + " Placa:" + VeiculoConfig.getVeiculoPlaca()+"\nCartela: "+VeiculoConfig.getVeiculoCartela() );

        this.btnKmFinal = (ImageButton)findViewById(R.id.btnKmFinal);
        this.edtKmFinal = (EditText)findViewById(R.id.edtKmFinal);

        this.btnKmFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comunicator.getInstance();
                if(edtKmFinal.getText().toString().compareTo("")!=0) {
                    Float KmFinal = Float.parseFloat(edtKmFinal.getText().toString());
                    BDV.setKm_final(KmFinal);

                    if(BDV.checkKM()){
                        BancoDados db = new BancoDados(getApplicationContext());
                        try {
                            if(db.insereBDV(
                                    Motorista.get_nome(),
                                    Motorista.get_id(),
                                    VeiculoConfig.getVeiculoCartela(),
                                    BDV.getHora_inicial(),
                                    BDV.getHora_final(),
                                    BDV.getKm_inicial(),
                                    BDV.getKm_final(),
                                    BDV.getKm_total(),
                                    BDV.getServico(),
                                    BDV.getFoto()
                            )){
                                BDV.resetBDV();
                                BDV.setKm_inicial(KmFinal);
                                Intent intent = new Intent(getApplicationContext(), logadoMotorista.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "BDV cadastrado com sucesso.", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Km final deve ser maior que o inicial.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Insira o Km final antes de finalizar a rota.", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Toast.makeText(getApplicationContext(), (String)Comunicator.getItem("horaFinal"), Toast.LENGTH_LONG).show();
    }
}

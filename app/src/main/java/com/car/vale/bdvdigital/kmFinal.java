package com.car.vale.bdvdigital;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import estruturas.AssinaturaPassageiro;
import estruturas.AssinaturasBDV;
import estruturas.BDV;
import estruturas.BancoDados;
import estruturas.CarroReserva;
import estruturas.Comunicator;
import estruturas.Motorista;
import estruturas.VeiculoConfig;

public class kmFinal extends AppCompatActivity {
    private Button btnKmFinal;
    private EditText edtKmFinal;
    private TextView txtMotoristaLogado;
    private TextView txtInfoVeiculo;
    private CheckBox cbReserva;
    private EditText edtReserva;
    public static Activity _esta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_km_final);

        _esta = this;

        this.txtMotoristaLogado = (TextView)findViewById(R.id.pctName1);
        this.txtInfoVeiculo = (TextView)findViewById(R.id.pctAge1);

        this.txtMotoristaLogado.setText(Motorista.get_nome());
        this.txtInfoVeiculo.setText("Veículo: "+ VeiculoConfig.getVeiculoModelo() + " Placa:" + VeiculoConfig.getVeiculoPlaca()+"\nCartela: "+VeiculoConfig.getVeiculoCartela() );

        this.btnKmFinal = (Button)findViewById(R.id.btnKmFinal);
        this.edtKmFinal = (EditText)findViewById(R.id.edtKmFinal);

        this.cbReserva = (CheckBox)findViewById(R.id.cbReserva);
        this.edtReserva = (EditText)findViewById(R.id.edtPlacaReserva);

        if(CarroReserva.getReserva()){
            this.cbReserva.setChecked(true);
            this.cbReserva.setEnabled(false);
            this.edtReserva.setText(CarroReserva.getPlacaReserva());
            this.edtReserva.setEnabled(false);
        }

        this.btnKmFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                    BDV.getReserva(),
                                    BDV.getPlacaReserva(),
                                    BDV.getServico()
                            )){
                                Integer bdvID = db.ultimoID(BancoDados.getTabelaBdv());
                                ArrayList<AssinaturaPassageiro> assinaturas = AssinaturasBDV.getInstance();
                                if(db.insereAssinatura(assinaturas, bdvID)) {
                                    AssinaturasBDV.clear();
                                    BDV.resetBDV();
                                    BDV.setKm_inicial(KmFinal);
                                    Intent intent = new Intent(getApplicationContext(), logadoMotorista.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "BDV cadastrado com sucesso.", Toast.LENGTH_LONG).show();
                                    _esta.finish();
                                }
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

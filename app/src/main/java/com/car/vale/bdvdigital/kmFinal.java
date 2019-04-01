package com.car.vale.bdvdigital;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import estruturas.AssinaturaPassageiro;
import estruturas.AssinaturasBDV;
import estruturas.BDV;
import estruturas.Comunicator;
import estruturas.Coordenada;
import estruturas.Trajeto;
import interfaces.BancoDados;
import estruturas.Configuracao;
import estruturas.Motorista;
import estruturas.VeiculoConfig;
import interfaces.Localizacao;

public class kmFinal extends Activity {
    private Button btnKmFinal;
    private EditText edtKmFinal;
    private TextView txtMotoristaLogado;
    private TextView txtInfoVeiculo;
    private CheckBox cbReserva;
    private EditText edtReserva;
    public static Activity _tela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_km_final);

        _tela = this;

        this.txtMotoristaLogado = (TextView)findViewById(R.id.pctName1);
        this.txtInfoVeiculo = (TextView)findViewById(R.id.pctAge1);

        this.txtMotoristaLogado.setText(Motorista.get_nome());
        this.txtInfoVeiculo.setText("Veículo: "+ VeiculoConfig.getVeiculoModelo() + " Placa:" + VeiculoConfig.getVeiculoPlaca()+"\nCartela: "+VeiculoConfig.getVeiculoCartela() );

        this.btnKmFinal = (Button)findViewById(R.id.btnKmFinal);
        this.edtKmFinal = (EditText)findViewById(R.id.edtKmFinal);

        this.cbReserva = (CheckBox)findViewById(R.id.cbReserva);
        this.edtReserva = (EditText)findViewById(R.id.edtPlacaReserva);

        if(Configuracao.getReserva()){
            this.cbReserva.setChecked(true);
            this.cbReserva.setEnabled(false);
            this.edtReserva.setText(Configuracao.getPlacaReserva());
            this.edtReserva.setEnabled(false);
        }

        this.btnKmFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtKmFinal.getText().toString().compareTo("")!=0) {
                    Float KmFinal = Float.parseFloat(edtKmFinal.getText().toString());
                    BDV.setKm_final(KmFinal);

                    if(BDV.checkKM()){
                        HashMap<String, Double> dist = Trajeto.getKmTrajeto();
                        Log.i("KM:", dist.get("TOTAL")+" "+dist.get("RODOVIA")+" "+dist.get("CIDADE"));
                        BancoDados db = new BancoDados(getApplicationContext());

                            if(db.insereBDV(
                                    Motorista.get_nome(),
                                    Motorista.get_id(),
                                    VeiculoConfig.getVeiculoCartela(),
                                    BDV.getHora_inicial(),
                                    BDV.getHora_final(),
                                    BDV.getKm_inicial(),
                                    BDV.getKm_final(),
                                    BDV.getKm_total(),
                                    dist.get("TOTAL"),
                                    dist.get("RODOVIA"),
                                    dist.get("CIDADE"),
                                    BDV.getReserva(),
                                    BDV.getPlacaReserva(),
                                    BDV.getCentro_custo(),
                                    BDV.getServico()
                            )){
                                Integer bdvID = db.ultimoID(BancoDados.getTabelaBdv());
                                ArrayList<Coordenada> coordenadas = Trajeto.inicializa();
                                ArrayList<AssinaturaPassageiro> assinaturas = AssinaturasBDV.getInstance();
                                if(db.insereAssinatura(assinaturas, bdvID) && db.insereRota(coordenadas, bdvID)) {
                                    AssinaturasBDV.clear();
                                    BDV.resetBDV();
                                    Trajeto.clear();
                                    BDV.setKm_inicial(KmFinal);
                                    Configuracao.setRodovia(false);

                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                    String hour = format.format(new Date());
                                    Motorista.setHora_ultima_rota(hour);

                                    db.atualizaUltimoKM(KmFinal);

                                    Comunicator.getInstance();
                                    Localizacao loc = (Localizacao) Comunicator.getItem("Localizacao");
                                    loc.stopLocationUpdate();

                                    Intent intent = new Intent(getApplicationContext(), logadoMotorista.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), getString(R.string.msg_bdv_cadastrado), Toast.LENGTH_LONG).show();
                                    kmFinal._tela.finish();
                                }
                            }

                    }else{
                        edtKmFinal.setError(getString(R.string.msg_kmfina_maior_kminicial));
                    }
                }else{
                    edtKmFinal.setError(getString(R.string.msg_inisira_km_final));
                }
            }
        });

        //Toast.makeText(getApplicationContext(), (String)Comunicator.getItem("horaFinal"), Toast.LENGTH_LONG).show();
    }
}

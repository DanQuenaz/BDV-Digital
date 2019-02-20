package com.car.vale.bdvdigital;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.pm.ActivityInfoCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import estruturas.AssinaturaPassageiro;
import estruturas.AssinaturasBDV;
import estruturas.BDV;
import estruturas.CarroReserva;
import estruturas.Comunicator;
import estruturas.Motorista;
import estruturas.VeiculoConfig;
import estruturas.WebInterface;

public class logadoMotorista extends AppCompatActivity {
    private Button btnBDV;
    private Spinner spnrServicos;
    private static Boolean state;
    private Boolean resume;
    private Button btnSyncBdv;
    private WebInterface ws;
    private Chronometer bdvTimer;
    private TextView txtMotoristaLogado;
    private TextView txtInfoVeiculo;
    private long elapsedTime;
    private CheckBox cbReserva;
    private EditText edtReserva;
    private Button btnAddAss;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logado_motorista);

        this.txtMotoristaLogado = (TextView)findViewById(R.id.pctName);
        this.txtInfoVeiculo = (TextView)findViewById(R.id.pctAge);

        this.txtMotoristaLogado.setText(Motorista.get_nome());
        this.txtInfoVeiculo.setText("Veículo: "+ VeiculoConfig.getVeiculoModelo() + " Placa:" + VeiculoConfig.getVeiculoPlaca()+"\nCartela: "+
                                    VeiculoConfig.getVeiculoCartela() );

        this.cbReserva = (CheckBox)findViewById(R.id.cbReserva);
        this.edtReserva = (EditText)findViewById(R.id.edtPlacaReserva);

        if(CarroReserva.getReserva()){
            this.cbReserva.setChecked(true);
            this.cbReserva.setEnabled(false);
            this.edtReserva.setText(CarroReserva.getPlacaReserva());
            this.edtReserva.setEnabled(false);

            BDV.setReserva(CarroReserva.getReserva());
            BDV.setPlacaReserva(CarroReserva.getPlacaReserva());
        }else{
            this.cbReserva.setEnabled(false);
            this.edtReserva.setEnabled(false);

            BDV.setReserva(false);
            BDV.setPlacaReserva(null);
        }

        state = true;
        this.resume = false;

        this.ws = new WebInterface(logadoMotorista.this);

        this.btnSyncBdv = (Button)findViewById(R.id.btnSyncBDV);
        this.spnrServicos = (Spinner)findViewById(R.id.spnrServicos);

        this.bdvTimer = (Chronometer)findViewById(R.id.bdvTimer);

        this.bdvTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if(!resume){
                    long minutes = ((SystemClock.elapsedRealtime()-bdvTimer.getBase())/1000)/60;
                    long seconds = ((SystemClock.elapsedRealtime()-bdvTimer.getBase())/100)%60;
                    elapsedTime = SystemClock.elapsedRealtime();
                    Log.d("", ""+minutes+":"+seconds);
                }else{
                    long minutes = ((SystemClock.elapsedRealtime()-bdvTimer.getBase())/1000)/60;
                    long seconds = ((SystemClock.elapsedRealtime()-bdvTimer.getBase())/100)%60;
                    elapsedTime += 1000;
                    Log.d("", ""+minutes+":"+seconds);
                }
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.servicos, R.layout.spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);

        this.spnrServicos.setAdapter(adapter);

        this.btnBDV = (Button)findViewById(R.id.btnBDV);
        this.btnBDV.setBackgroundColor(Color.GREEN);


        this.btnBDV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state == true){
                    btnBDV.setBackgroundColor(Color.RED);
                    btnBDV.setText(getString(R.string.btnFimBdv));
                    spnrServicos.setEnabled(false);
                    state = false;

                    BDV.setServico((String)spnrServicos.getSelectedItem());

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String hour = format.format(new Date());
                    BDV.setHora_inicial(hour);

                    if(!resume){
                        bdvTimer.setBase(SystemClock.elapsedRealtime());
                        bdvTimer.start();
                    }else{
                        bdvTimer.start();
                    }

                }else if(state == false){
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String hour = format.format(new Date());
                    BDV.setHora_final(hour);

                    bdvTimer.stop();
                    bdvTimer.setText("00:00");
                    resume = false;

                    AssinaturasBDV.getInstance();

                    if(!AssinaturasBDV.isVazio()) {
                        Intent intent = new Intent(getApplicationContext(), kmFinal.class);
                        startActivity(intent);
                        //startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 1);
                    }else{
                        Toast.makeText(getApplicationContext(), getString(R.string.msg_lista_ass_vazia),Toast.LENGTH_LONG).show();
                    }

                }else{}
            }
        });

        this.btnSyncBdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ws.CallBDVRequest(logadoMotorista.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        this.btnAddAss = (Button) findViewById(R.id.btnAddAss);
        this.btnAddAss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!state) {
                    startActivity(new Intent(getApplicationContext(), assinaturaPassageiro.class));
                }else{
                    Toast.makeText(getApplicationContext(), getString(R.string.msg_inicie_rota),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){

            Bundle extra = data.getExtras();
            Bitmap bmp = (Bitmap) extra.get("data");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            bmp.recycle();

            *//*int size = bmp.getRowBytes() * bmp.getHeight();
            ByteBuffer byteBuffer = ByteBuffer.allocate(size);
            bmp.copyPixelsToBuffer(byteBuffer);
            byte[] byteArray = byteBuffer.array();*//*

            BDV.setFoto(byteArray);


            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String hour = format.format(new Date());
            BDV.setHora_final(hour);

            bdvTimer.stop();
            bdvTimer.setText("00:00");
            resume = false;

            Intent intent = new Intent(getApplicationContext(), kmFinal.class);
            startActivity(intent);

        }

    }*/
}

package com.car.vale.bdvdigital;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import estruturas.AssinaturasBDV;
import estruturas.BDV;
import estruturas.Comunicator;
import estruturas.Trajeto;
import estruturas.UploadStatus;
import interfaces.BancoDados;
import estruturas.Configuracao;
import estruturas.Motorista;
import estruturas.VeiculoConfig;
import interfaces.HttpCon;
import interfaces.Localizacao;

public class logadoMotorista extends Activity {
    private Button btnBDV;
    private Spinner spnrServicos;
    private static Boolean state;
    private Boolean resume;
    private Button btnSyncBdv;
    private HttpCon ws;
    private Chronometer bdvTimer;
    private TextView txtMotoristaLogado;
    private TextView txtInfoVeiculo;
    private long elapsedTime;
    private CheckBox cbReserva;
    private EditText edtReserva;
    private Button btnAddAss;
    private Localizacao loc;
    private Boolean count_aux;
    private Switch swtRodovia;
    private CheckBox cbCC;
    private EditText edtCC;
    public static Activity _tela;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logado_motorista);

        _tela = this;

        UploadStatus.inicializa();

        this.count_aux = false;

        this.txtMotoristaLogado = (TextView)findViewById(R.id.pctName);
        this.txtInfoVeiculo = (TextView)findViewById(R.id.pctAge);

        this.txtMotoristaLogado.setText(Motorista.get_nome());
        this.txtInfoVeiculo.setText("Veículo: "+ VeiculoConfig.getVeiculoModelo() + " Placa:" + VeiculoConfig.getVeiculoPlaca()+"\nCartela: "+
                                    VeiculoConfig.getVeiculoCartela() );

        this.cbReserva = (CheckBox)findViewById(R.id.cbReserva);
        this.edtReserva = (EditText)findViewById(R.id.edtPlacaReserva);

        this.swtRodovia = (Switch)findViewById(R.id.switch1);

        this.loc = new Localizacao();
        this.loc.callConnection(logadoMotorista.this);

        setaInfoSync();

        if(Configuracao.getReserva()){
            this.cbReserva.setChecked(true);
            this.cbReserva.setEnabled(false);
            this.edtReserva.setText(Configuracao.getPlacaReserva());
            this.edtReserva.setEnabled(false);

            BDV.setReserva(Configuracao.getReserva());
            BDV.setPlacaReserva(Configuracao.getPlacaReserva());
        }else{
            this.cbReserva.setEnabled(false);
            this.edtReserva.setEnabled(false);

            BDV.setReserva(false);
            BDV.setPlacaReserva("-");
        }

        state = true;
        this.resume = false;

        this.ws = new HttpCon(logadoMotorista.this);

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
                }else{
                    long minutes = ((SystemClock.elapsedRealtime()-bdvTimer.getBase())/1000)/60;
                    long seconds = ((SystemClock.elapsedRealtime()-bdvTimer.getBase())/100)%60;
                    elapsedTime += 1000;
                }
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.servicos, R.layout.spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);

        this.spnrServicos.setAdapter(adapter);

        this.btnBDV = (Button)findViewById(R.id.btnBDV);
        this.btnBDV.setBackgroundColor(Color.GREEN);

        this.swtRodovia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Configuracao.setRodovia(swtRodovia.isChecked());
            }
        });


        this.btnBDV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state == true){
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    boolean GPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                    if(!GPSEnabled){
                        //startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        Toast.makeText(getApplicationContext(), getString(R.string.msg_ativa_gps), Toast.LENGTH_LONG).show();
                    }else {
                        btnBDV.setBackgroundColor(Color.RED);
                        btnBDV.setText(getString(R.string.btnFimBdv));
                        btnSyncBdv.setEnabled(false);
                        spnrServicos.setEnabled(false);
                        state = false;

                        BDV.setServico((String) spnrServicos.getSelectedItem());

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        String hour = format.format(new Date());
                        BDV.setHora_inicial(hour);
                        Motorista.setHora_peimeira_rota(hour);

                        if (!resume) {
                            bdvTimer.setBase(SystemClock.elapsedRealtime());
                            bdvTimer.start();
                        } else {
                            bdvTimer.start();
                        }

                        loc.startLocationUpdate();
                    }

                }else if(state == false){
                    new AlertDialog.Builder(logadoMotorista.this)
                        .setTitle("Finalizar rota")
                        .setMessage("Deseja mesmo finalizar a rota?")
                        .setPositiveButton("Finalizar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                AssinaturasBDV.getInstance();
                                Comunicator.getInstance();
                                Comunicator.clear();
                                Comunicator.addObject("Localizacao", loc);
                                if(!AssinaturasBDV.isVazio()) {
                                    if(cbCC.isChecked() && !edtCC.getText().toString().isEmpty()) {
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                        String hour = format.format(new Date());
                                        BDV.setHora_final(hour);
                                        BDV.setCentro_custo(edtCC.getText().toString());
                                        bdvTimer.stop();
                                        bdvTimer.setText("00:00");
                                        resume = false;
                                        Intent intent = new Intent(getApplicationContext(), kmFinal.class);
                                        startActivity(intent);
                                        logadoMotorista._tela.finish();
                                        //startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 1);
                                    }else if(!cbCC.isChecked()){
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                        String hour = format.format(new Date());
                                        BDV.setHora_final(hour);
                                        BDV.setCentro_custo(VeiculoConfig.getCentro_custo());
                                        bdvTimer.stop();
                                        bdvTimer.setText("00:00");
                                        resume = false;
                                        Intent intent = new Intent(getApplicationContext(), kmFinal.class);
                                        startActivity(intent);
                                        logadoMotorista._tela.finish();
                                    }else{
                                        edtCC.setError(getString(R.string.campo_obrigatorio));
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(), getString(R.string.msg_lista_ass_vazia),Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                }else{}
            }
        });

        this.btnSyncBdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checaWIFI()) {
                    try {
                        BancoDados db = new BancoDados(getApplicationContext());
                        Boolean _statusBDV = db.checkStatusBDV();
                        Boolean _statusCL = db.checkStatusCheckin();
                        Boolean _statusHE = db.checkStatusHoraExtra();
                        Boolean _statusCM = db.checkStatusCustosMotorista();
                        if( _statusBDV || _statusCL || _statusHE || _statusCM) {
                            startActivity(new Intent(getApplicationContext(), loadingSyncBDV.class));
                        }else{
                            Toast.makeText(getApplicationContext(), getString(R.string.msg_sem_bdv_dessincronizado), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), getString(R.string.erro_nome_wifi), Toast.LENGTH_LONG).show();
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

        this.edtCC = (EditText)findViewById(R.id.edtCentroCusto);
        this.edtCC.setEnabled(false);

        this.cbCC = (CheckBox)findViewById(R.id.cbCentroCusto);
        this.cbCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cbCC.isChecked()){
                    edtCC.setEnabled(true);
                }else{
                    edtCC.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        if(state) {
            if (!count_aux) {
                Toast.makeText(getApplicationContext(), getString(R.string.msg_botao_voltar), Toast.LENGTH_LONG).show();
                count_aux = true;
            } else {
                deslogar();
            }
        }else Toast.makeText(getApplicationContext(), getString(R.string.msg_finalize_rota), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        setaInfoSync();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuItem m1 = menu.add(0,0,0,"REGISTRAR CUSTO DIÁRIO");
        m1.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        MenuItem m2 = menu.add(0,1,1,"ALTERAR SENHA");
        m2.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        MenuItem m3 = menu.add(0,2,2,"SAIR");
        m3.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        return true;
    }

    @Override
    public boolean onMenuItemSelected(int parse, MenuItem id){

        switch(id.getItemId()){
            case 0:
                if(state){
                    startActivity(new Intent(getApplicationContext(), cadastroCustoMotorista.class));
                }else Toast.makeText(getApplicationContext(), getString(R.string.msg_finalize_rota), Toast.LENGTH_LONG).show();
                break;
            case 1:
                if(state){
                    if(checaWIFI()) {
                        startActivity(new Intent(getApplicationContext(), alteraSenha.class));
                    }else Toast.makeText(getApplicationContext(), getString(R.string.erro_nome_wifi), Toast.LENGTH_LONG).show();
                }else Toast.makeText(getApplicationContext(), getString(R.string.msg_finalize_rota), Toast.LENGTH_LONG).show();
                break;
            case 2:
                if(state){
                    new AlertDialog.Builder(logadoMotorista.this)
                        .setTitle("Encerrar Sessão")
                            .setMessage("Deseja mesmo sair?")
                            .setPositiveButton("Sair", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deslogar();
                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }else Toast.makeText(getApplicationContext(), getString(R.string.msg_finalize_rota), Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    public void deslogar() {
        BDV.resetBDV();
        AssinaturasBDV.clear();
        Trajeto.clear();
        Configuracao.setReserva(false);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String hour = format.format(new Date());
        Motorista.setHora_logout(hour);

        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E");
        String dia_semana = simpleDateformat.format(new Date());

        BancoDados bd = new BancoDados(getApplicationContext());
        try {
            if(bd.insereHoraExtra(
                    Motorista.get_matricula(),
                    Motorista.getHora_login(),
                    Motorista.getHora_logout(),
                    Motorista.getHora_peimeira_rota(),
                    Motorista.getHora_ultima_rota(),
                    Motorista.getDifLogado(),
                    Motorista.getDifRota(),
                    dia_semana
            )){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                logadoMotorista._tela.finish();
            }else{
                Toast.makeText(getApplicationContext(), getString(R.string.erro_logout), Toast.LENGTH_LONG).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private void setaInfoSync(){
        BancoDados bd = new BancoDados(getApplicationContext());
        LinearLayout layoutInSync = (LinearLayout)findViewById(R.id.layoutInfoSync);
        TextView edtInfoSync = (TextView)findViewById(R.id.edtInfoSync);
        if(bd.checkStatusBDV() || bd.checkStatusCheckin() || bd.checkStatusHoraExtra() || bd.checkStatusCustosMotorista()){
            layoutInSync.setBackgroundColor(Color.RED);
            edtInfoSync.setText(R.string.info_dados_nao_sync);
        }else{
            layoutInSync.setBackgroundColor(Color.GREEN);
            edtInfoSync.setText(R.string.info_todos_dados_sync);
        }
    }

    private boolean checaWIFI(){

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return mWifi.isConnected() && mWifi.getExtraInfo().equals(getString(R.string.WIFI_NAME));

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

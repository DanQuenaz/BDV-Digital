package com.car.vale.bdvdigital;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import estruturas.Motorista;
import interfaces.BancoDados;

public class cadastroCustoMotorista extends AppCompatActivity {

    private EditText edtDescricao;
    private EditText edtValor;
    private Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_custo_motorista);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        Integer width = dm.widthPixels;
        Integer heigth = dm.heightPixels;

        getWindow().setLayout((int)(width*.85),(int)(heigth*.70));

        final long[] _data = new long[1];

        this.edtDescricao = (EditText) findViewById(R.id.edtDescricaoCustoMotorista);
        this.edtValor = (EditText) findViewById(R.id.edtValorCustoMotorista);
        this.btnOk = (Button) findViewById(R.id.btnOKCustoMotorista);

        final CalendarView clndData = (CalendarView) findViewById(R.id.clndCustoMotorista);


        clndData.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                _data[0] = clndData.getDate();
            }
        });

        this.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean _status = true;

                String _descricao = edtDescricao.getText().toString();
                String _valor = edtValor.getText().toString();

                if(_descricao.isEmpty()){
                    edtDescricao.setError(getString(R.string.campo_obrigatorio));
                    _status = false;
                }

                if(_valor.isEmpty()){
                    edtValor.setError(getString(R.string.campo_obrigatorio));
                    _status = false;
                }

                if(_status){
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String hour = format.format(new Date(clndData.getDate()));
                    Log.i("", hour);
                    BancoDados bd = new BancoDados(getApplicationContext());

                    if(bd.insereCustoMotorista(
                            _descricao,
                            hour,
                            Float.parseFloat(_valor),
                            Motorista.get_matricula())){
                        Toast.makeText(getApplicationContext(), getString(R.string.msg_custo_cadastrado), Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        });
    }
}

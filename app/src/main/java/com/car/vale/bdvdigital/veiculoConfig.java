package com.car.vale.bdvdigital;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import estruturas.WebInterface;

public class veiculoConfig extends AppCompatActivity {

    private ImageButton btnAtlzConfig;
    private EditText edtVeiculoCartela;
    private EditText edtVeiculoModelo;
    private EditText edtVeiculoPlaca;
    private EditText edtSenhaVeiculo;
    private boolean status;
    public static Activity _tela;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiculo_config);

        _tela = this;

        this.status = true;
        this.btnAtlzConfig = (ImageButton)findViewById(R.id.btnConfigVeiculo);
        this.edtVeiculoCartela = (EditText)findViewById(R.id.edtConfigCartela);
        this.edtVeiculoModelo = (EditText)findViewById(R.id.edtConfigModelo);
        this.edtVeiculoPlaca = (EditText)findViewById(R.id.edtConfigPlaca);
        this.edtSenhaVeiculo = (EditText)findViewById(R.id.edtSenhaConfig);

        this.btnAtlzConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtVeiculoCartela.getText().toString().isEmpty() ){
                    edtVeiculoCartela.setError(getString(R.string.error_field_required));
                    status = false;
                }

                if(edtVeiculoModelo.getText().toString().isEmpty() ){
                    edtVeiculoModelo.setError(getString(R.string.error_field_required));
                    status = false;
                }

                if(edtVeiculoPlaca.getText().toString().isEmpty() ){
                    edtVeiculoPlaca.setError(getString(R.string.error_field_required));
                    status = false;
                }

                if(edtSenhaVeiculo.getText().toString().isEmpty() ){
                    edtSenhaVeiculo.setError(getString(R.string.error_field_required));
                    status = false;
                }

                if(status){
                    String _cartela = edtVeiculoCartela.getText().toString();
                    String _modelo = edtVeiculoModelo.getText().toString();
                    String _placa = edtVeiculoPlaca.getText().toString();
                    String _senha = edtSenhaVeiculo.getText().toString();

                    WebInterface ws = new WebInterface(getApplicationContext());

                    ws.CallPassWordAdmRequest(getApplicationContext(), _senha, _cartela, _modelo, _placa);


                }
            }
        });


    }
}

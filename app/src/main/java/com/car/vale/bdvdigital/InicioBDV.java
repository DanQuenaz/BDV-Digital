package com.car.vale.bdvdigital;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class InicioBDV extends AppCompatActivity {

    private Spinner listaServicos;
    private Button btnTest;
    private static String servicoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_bdv);

        String[] servicos = new String[]{"Horário almoço", "Horário janta", "Troca de motorista", "Rota estação", "Atendimento pátio"};

        this.listaServicos = (Spinner)findViewById(R.id.listaServicos);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, servicos);
        this.listaServicos.setAdapter(adapter);

        this.listaServicos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                servicoSelecionado = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        this.btnTest = (Button)findViewById(R.id.buttonTest);

        this.btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Servico: "+servicoSelecionado, Toast.LENGTH_SHORT).show();
            }
        });

    }
}

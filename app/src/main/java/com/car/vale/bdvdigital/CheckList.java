package com.car.vale.bdvdigital;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adaptadores.ListViewItemCheckboxBaseAdapter;
import adaptadores.ListViewItemDTO;
import interfaces.BancoDados;
import estruturas.Motorista;
import estruturas.VeiculoConfig;

public class CheckList extends Activity {

    public static Activity _tela;

    private ListView checkList;
    private Button btnCheckList;
    private EditText edtObsCheckList;
    private TextView txtMotoristaLogado;
    private TextView txtInfoVeiculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        _tela = this;

        this.txtMotoristaLogado = (TextView)findViewById(R.id.pctName);
        this.txtInfoVeiculo = (TextView)findViewById(R.id.pctAge);

        this.txtMotoristaLogado.setText(Motorista.get_nome());
        this.txtInfoVeiculo.setText("Veículo: "+ VeiculoConfig.getVeiculoModelo() + " Placa:" + VeiculoConfig.getVeiculoPlaca()+"\nFrota: "+VeiculoConfig.getVeiculoCartela() );

        this.edtObsCheckList = (EditText) findViewById(R.id.edtObsCheckList);

        this.btnCheckList = (Button) findViewById(R.id.btnCheckList);
        this.checkList = (ListView) findViewById(R.id.list_view_with_checkbox);

        final List<ListViewItemDTO> initItemList = this.getInitViewItemDtoList();

        final ListViewItemCheckboxBaseAdapter listViewDataAdapter = new ListViewItemCheckboxBaseAdapter(getApplicationContext(),
                                                                        initItemList);

        listViewDataAdapter.notifyDataSetChanged();

        this.checkList.setAdapter(listViewDataAdapter);

        this.checkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {
                // Get user selected item.
                Object itemObject = adapterView.getAdapter().getItem(itemIndex);

                // Translate the selected item to DTO object.
                ListViewItemDTO itemDto = (ListViewItemDTO)itemObject;

                // Get the checkbox.
                CheckBox itemCheckbox = (CheckBox) view.findViewById(R.id.list_view_item_checkbox);
                TextView itemCheckList = (TextView) view.findViewById(R.id.list_view_item_text);

                // Reverse the checkbox and clicked item check state.
                if(itemDto.isChecked())
                {
                    itemCheckbox.setChecked(false);
                    itemDto.setChecked(false);
                    itemCheckList.setTextColor(Color.BLACK);
                    itemDto.setItemColor(Color.BLACK);
                }else
                {
                    itemCheckbox.setChecked(true);
                    itemDto.setChecked(true);
                    itemCheckList.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    itemDto.setItemColor(getResources().getColor(R.color.colorPrimaryDark));
                }

                //Toast.makeText(getApplicationContext(), "select item text : " + itemDto.getItemText(), Toast.LENGTH_SHORT).show();
            }
        });

        this.btnCheckList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String dia_hora = format1.format(new Date());

                String observacoes = edtObsCheckList.getText().toString();

                int size = initItemList.size();
                Boolean[] checkList = new Boolean[30];

                for(int i=0;i<size;i++) {
                    ListViewItemDTO dto = initItemList.get(i);

                    if(dto.isChecked()) {
                        checkList[i] = new Boolean(true);
                    }
                }
                listViewDataAdapter.notifyDataSetChanged();

                BancoDados db = new BancoDados(getApplicationContext());

                if(db.insereCheckList(VeiculoConfig.getVeiculoCartela(), Motorista.get_nome(), Motorista.get_matricula(), dia_hora,
                        observacoes, checkList)){
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.checklist_completed),
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), kmInicial.class));
                    CheckList._tela.finish();
                }else{
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.checklist_error),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuItem m1 = menu.add(0,0,0,"SAIR");
        m1.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        return true;
    }

    @Override
    public boolean onMenuItemSelected(int parse, MenuItem id){

        switch(id.getItemId()){
            case 0:
                deslogar();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(CheckList.this, MainActivity.class));
        CheckList._tela.finish();
    }

    private List<ListViewItemDTO> getInitViewItemDtoList() {
        String itemTextArr[] = getResources().getStringArray( R.array.check_itens);

        List<ListViewItemDTO> ret = new ArrayList<ListViewItemDTO>();

        int length = itemTextArr.length;

        for(int i=0;i<length;i++) {
            String itemText = itemTextArr[i];

            ListViewItemDTO dto = new ListViewItemDTO();
            dto.setChecked(false);
            dto.setItemText(itemText);

            ret.add(dto);
        }

        return ret;
    }

    private void deslogar(){
        startActivity(new Intent(CheckList.this, MainActivity.class));
        CheckList._tela.finish();
    }


}

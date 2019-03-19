package interfaces;

/**
 * Created by danqu on 07/11/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.ViewDebug;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import estruturas.AssinaturaPassageiro;
import estruturas.Coordenada;
import estruturas.Motorista;
import estruturas.VeiculoConfig;

public class BancoDados extends SQLiteOpenHelper{

    private static final String nome_banco = "bdv_digital.db";
    private static final Integer versao = 1;

    private static final String tabelaBdv = "bdv";
    private static final String tabelaDadosConfig = "dadosConfig";
    private static final String tabelaMotorista = "motorista";
    private static final String tabelaRota = "rota";
    private static final String tabelaCheckList = "checkList";
    private static final String tabelaAssinatura = "assinatura";


    public BancoDados(Context context) {
        super(context, nome_banco, null, versao);
    }

    public BancoDados(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public BancoDados(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlTable1 =
        "create table motorista(\n"+
                "motoristaID integer primary key autoincrement,\n"+
                "matricula text,\n"+
                "nome text,\n"+
                "rg text,\n"+
                "cpf text,\n"+
                "senha text\n"+
        ");";
        sqLiteDatabase.execSQL(sqlTable1);

        String sqlTable2 =
        "create table bdv(\n" +
                "bdvID integer primary key autoincrement,\n"+
                "motoristaNome text,\n"+
                "motoristaID integer,\n"+
                "veiculo text,\n"+
                "hora_inicial text,\n"+
                "hora_final text,\n"+
                "km_inicial real,\n"+
                "km_final real,\n"+
                "km_total real,\n" +
                "km_calculado real,\n" +
                "km_cidade real,\n" +
                "km_rodovia real,\n"+
                "servico text,\n"+
                "reserva boolean,\n"+
                "placaReserva text,\n"+
                "sincronizado boolean\n"+
        ");";
        sqLiteDatabase.execSQL(sqlTable2);

        String sqlTable6 =
        "create table assinatura(\n" +
                "assinaturaID integer primary key autoincrement,\n" +
                "nome_passageiro text,\n" +
                "matricula_passageiro text,\n" +
                "observacao text,\n" +
                "avaliacao integer,\n" +
                "bdvID integer,\n" +
                "foreign key (bdvID) references bdv (bdvID)\n" +
        ");";
        sqLiteDatabase.execSQL(sqlTable6);

        String sqlTable3 =
        "create table rota(\n"+
                "coordenadaID integer primary key autoincrement,\n"+
                "longitude real,\n"+
                "latitude real,\n" +
                "altitude real,\n"+
                "comportamento real,\n" +
                "velocidade real,\n" +
                "provedor text,\n" +
                "acuracia real,\n" +
                "hora text,\n" +
                "cont integer,\n"+
                "bdvID integer,\n"+

                "foreign key (bdvID) references bdv (bdvID)\n"+

        ");";
        sqLiteDatabase.execSQL(sqlTable3);

        String sqlTable4 =
        "create table dadosConfig(\n"+
                "veiculoCartela text,\n"+
                "veiculoModelo text,\n"+
                "veiculoPlaca text,\n"+
                "km real \n"+
        ");";

        sqLiteDatabase.execSQL(sqlTable4);

        String sqlTable5 =
        "create table checkList(\n"+
                "veiculoCartela text,\n"+
                "motoristaNome text,\n"+
                "dia text,\n"+
                "horario text, \n"+
                "item1 boolean, \n"+
                "item2 boolean, \n"+
                "item3 boolean, \n"+
                "item4 boolean, \n"+
                "item5 boolean, \n"+
                "item6 boolean, \n"+
                "item7 boolean, \n"+
                "item8 boolean, \n"+
                "item9 boolean, \n"+
                "item10 boolean, \n"+
                "item11 boolean, \n"+
                "item12 boolean, \n"+
                "item13 boolean, \n"+
                "item14 boolean, \n"+
                "item15 boolean, \n"+
                "item16 boolean, \n"+
                "item17 boolean, \n"+
                "item18 boolean, \n"+
                "item19 boolean, \n"+
                "item20 boolean, \n"+
                "item21 boolean, \n"+
                "item22 boolean, \n"+
                "item23 boolean, \n"+
                "item24 boolean, \n"+
                "item25 boolean, \n"+
                "item26 boolean, \n"+
                "item27 boolean, \n"+
                "item28 boolean, \n"+
                "item29 boolean, \n"+
                "sincronizado boolean \n"+
        ");";

        sqLiteDatabase.execSQL(sqlTable5);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + tabelaBdv);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + tabelaRota);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + tabelaMotorista);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + tabelaDadosConfig);
        onCreate(sqLiteDatabase);
    }

    public static String getNome_banco() {
        return nome_banco;
    }

    public static Integer getVersao() {
        return versao;
    }

    public static String getTabelaBdv() {
        return tabelaBdv;
    }

    public static String getTabelaDadosConfig() {
        return tabelaDadosConfig;
    }

    public static String getTabelaMotorista() {
        return tabelaMotorista;
    }

    public static String getTabelaRota() {
        return tabelaRota;
    }

    public static String getTabelaCheckList() {
        return tabelaCheckList;
    }

    public static String getTabelaAssinatura() {
        return tabelaAssinatura;
    }

    public boolean insereBDV(String motoristaNome, Integer motoristaID, String veiculo, String horaInicial, String horaFinal,
                             Float kmInicial, Float kmFinal, Float kmTotal, Double kmCalculado, Boolean reserva,
                             String placaReserva, String servico){

        ContentValues valores = new ContentValues();

        valores.put("motoristaNome", motoristaNome);
        valores.put("motoristaID", motoristaID);
        valores.put("veiculo", veiculo);
        valores.put("hora_inicial", horaInicial);
        valores.put("hora_final", horaFinal);
        valores.put("km_inicial", kmInicial);
        valores.put("km_final", kmFinal);
        valores.put("km_total", kmTotal);
        valores.put("km_calculado", kmCalculado);
        valores.put("servico", servico);
        valores.put("reserva", reserva);
        valores.put("placaReserva", placaReserva);
        valores.put("sincronizado", false);

        SQLiteDatabase db = getWritableDatabase();
        return db.insert(tabelaBdv, null, valores) != -1;
    }

    public boolean insereAssinatura(ArrayList<AssinaturaPassageiro> a, Integer bdvID){
        Boolean _status = true;
        SQLiteDatabase db = getWritableDatabase();
        for(AssinaturaPassageiro index : a){
            ContentValues valores = new ContentValues();
            valores.put("nome_passageiro", index.getNomePassageiro());
            valores.put("matricula_passageiro", index.getMatriculaPassageiro());
            valores.put("observacao", index.getObservacao());
            valores.put("avaliacao", index.getAvaliacao());
            valores.put("bdvID", bdvID);

            if(db.insert(tabelaAssinatura, null, valores) == -1) _status = false;
        }

        return _status;
    }

    public boolean  insereMotorista(String matricula,  String nome, String rg, String cpf, String senha){

        Log.i("INFO: ", matricula+" - " + nome + " - " + rg + " - " + cpf + " - " + senha);

        ContentValues valores;
        valores = new ContentValues();

        valores.put("matricula", matricula);
        valores.put("nome", nome);
        valores.put("rg", rg);
        valores.put("cpf", cpf);
        valores.put("senha", senha);


        SQLiteDatabase db = getWritableDatabase();
        return db.insert(tabelaMotorista, null, valores) != -1;

    }

    public boolean logaMotorista(String matricula, String senha){


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                tabelaMotorista,
                new String[]{"motoristaID", "matricula","nome","rg","cpf"},
                new String ("matricula = ? AND senha = ?"),
                new String[]{matricula, senha},
                null,
                null,
                null
        );


        if(cursor.moveToFirst()){
            Integer _id = cursor.getInt(cursor.getColumnIndex("motoristaID"));
            String _matricula = cursor.getString(cursor.getColumnIndex("matricula"));
            String _nome = cursor.getString(cursor.getColumnIndex("nome"));
            String _rg = cursor.getString(cursor.getColumnIndex("rg"));
            String _cpf = cursor.getString(cursor.getColumnIndex("cpf"));

            Motorista.getInstance(_id, _matricula, _nome, _rg, _cpf);

            cursor.close();

            return true;
        }

        return false;
    }

    public boolean insereVeiculo(String cartela, String modelo, String placa){
        SQLiteDatabase db = getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        if(db.delete(tabelaDadosConfig, null, null)>=0){
            ContentValues valores;
            valores = new ContentValues();

            valores.put("veiculoCartela", cartela);
            valores.put("veiculoModelo", modelo);
            valores.put("veiculoPlaca", placa);

            return db.insert(tabelaDadosConfig, null, valores) != -1;

        }
        return false;
    }

    public boolean insereRota(ArrayList<Coordenada> cords, Integer bdvID){
        Boolean _status = true;
        SQLiteDatabase db = getWritableDatabase();

        for(Coordenada c : cords){
            ContentValues values = new ContentValues();

            values.put("latitude", c.getLatitude());
            values.put("longitude", c.getLongitude());
            values.put("altitude", c.getAltitude());
            values.put("comportamento", c.getComportamento());
            values.put("velocidade", c.getVelocidade());
            values.put("provedor", c.getProvedor());
            values.put("acuracia", c.getAcuracia());
            values.put("hora", c.getHora());
            values.put("cont", cords.indexOf(c));
            values.put("bdvID", bdvID);


            if(db.insert(tabelaRota,null, values) == -1) _status = false;
        }

        return _status;

    }

    public boolean setVeiculoConfig(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                tabelaDadosConfig,
                new String[]{"*"},
                null,
                null,
                null,
                null,
                null
        );

        if(cursor.moveToFirst()){
            VeiculoConfig.setInstance(
                    cursor.getString(cursor.getColumnIndex("veiculoCartela")),
                    cursor.getString(cursor.getColumnIndex("veiculoModelo")),
                    cursor.getString(cursor.getColumnIndex("veiculoPlaca"))
            );
            return true;
        }

        return false;
    }

    public boolean limpaMotoristas(){
        SQLiteDatabase db = getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        if(db.delete(tabelaMotorista, null, null)>=0){
            return true;
        }

        return false;
    }

    public void atualizaMotoristas(JSONArray mots, Context context) throws JSONException {
        JSONObject aux;
        if(this.limpaMotoristas()){
            for(int i=0; i<mots.length();++i){
                aux = mots.getJSONObject(i);
                Log.i("TESTE: ", aux.getString("matricula"));

                this.insereMotorista(
                        aux.getString("matricula"),
                        aux.getString("nome"),
                        aux.getString("rg"),
                        aux.getString("cpf"),
                        aux.getString("senha")
                );
            }
            Toast.makeText(context, "Motoristas atualizados com sucesso!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Could not add employee", Toast.LENGTH_SHORT).show();
        }
    }

    public String getJSONArrayBDVs() throws JSONException {
        JSONArray ja = new JSONArray();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                tabelaBdv,
                new String[]{"*"},
                new String ("sincronizado = ?"),
                new String[]{"0"},
                null,
                null,
                null
        );


        if(cursor.moveToFirst()){
            do {
                JSONObject jo = new JSONObject();
                jo.put("motoristaNome", cursor.getString(cursor.getColumnIndex("motoristaNome")));
                jo.put("motoristaID", cursor.getInt(cursor.getColumnIndex("motoristaID")));
                jo.put("frota_veiculo", cursor.getString(cursor.getColumnIndex("veiculo")));
                jo.put("hora_inicial", cursor.getString(cursor.getColumnIndex("hora_inicial")));
                jo.put("hora_final", cursor.getString(cursor.getColumnIndex("hora_final")));
                jo.put("km_inicial", cursor.getString(cursor.getColumnIndex("km_inicial")));
                jo.put("km_final", cursor.getString(cursor.getColumnIndex("km_final")));
                jo.put("km_total", cursor.getString(cursor.getColumnIndex("km_total")));
                jo.put("km_calculado", cursor.getString(cursor.getColumnIndex("km_calculado")));
                jo.put("reserva", cursor.getShort(cursor.getColumnIndex("reserva")));
                jo.put("placa_reserva", cursor.getString(cursor.getColumnIndex("placaReserva")));
                jo.put("servico", cursor.getString(cursor.getColumnIndex("servico")));

                jo.put("assinaturas", getJSONArrayAss( cursor.getInt(cursor.getColumnIndex("bdvID")) ));
                jo.put("rota", getJSONArrayCoord( cursor.getInt(cursor.getColumnIndex("bdvID")) ));

                //jo.put("foto", Base64.encodeToString( cursor.getBlob(cursor.getColumnIndex("foto")) , Base64.NO_WRAP));

                ja.put(jo);


            }while(cursor.moveToNext());

            cursor.close();
            Log.i("", ja.toString());
            return ja.toString();
        }

        return null;

    }

    public JSONArray getJSONArrayAss(Integer bdvID) throws JSONException {
        JSONArray ja = new JSONArray();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM assinatura WHERE bdvID = " + bdvID;
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                JSONObject jo = new JSONObject();
                jo.put("nome_passageiro", cursor.getString(cursor.getColumnIndex("nome_passageiro")));
                jo.put("matricula_passageiro", cursor.getString(cursor.getColumnIndex("matricula_passageiro")));
                jo.put("observacao", cursor.getString(cursor.getColumnIndex("observacao")));
                jo.put("avaliacao", cursor.getInt(cursor.getColumnIndex("avaliacao")));

                ja.put(jo);

            }while (cursor.moveToNext());

            cursor.close();
            return ja;
        }

        return null;
    }

    public JSONArray getJSONArrayCoord(Integer bdvID) throws JSONException {
        JSONArray ja = new JSONArray();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM rota WHERE bdvID = " + bdvID;
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                JSONObject jo = new JSONObject();
                jo.put("latitude", cursor.getDouble(cursor.getColumnIndex("latitude")));
                jo.put("longitude", cursor.getDouble(cursor.getColumnIndex("longitude")));
                jo.put("altitude", cursor.getDouble(cursor.getColumnIndex("altitude")));
                jo.put("comportamento", cursor.getFloat(cursor.getColumnIndex("comportamento")));
                jo.put("velocidade", cursor.getFloat(cursor.getColumnIndex("velocidade")));
                jo.put("provedor", cursor.getString(cursor.getColumnIndex("provedor")));
                jo.put("acuracia", cursor.getFloat(cursor.getColumnIndex("acuracia")));
                jo.put("hora", cursor.getString(cursor.getColumnIndex("hora")));
                jo.put("cont", cursor.getInt(cursor.getColumnIndex("cont")));

                ja.put(jo);

            }while (cursor.moveToNext());

            cursor.close();
            return ja;
        }

        return null;
    }

    public boolean insereCheckList(String cartela, String motorista, String dia, String horario, Boolean[] checkList){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("veiculoCartela", cartela);
        valores.put("dia", dia);
        valores.put("horario", horario);
        valores.put("item1", checkList[0]);
        valores.put("item2", checkList[1]);
        valores.put("item3", checkList[2]);
        valores.put("item4", checkList[3]);
        valores.put("item5", checkList[4]);
        valores.put("item6", checkList[5]);
        valores.put("item7", checkList[6]);
        valores.put("item8", checkList[7]);
        valores.put("item9", checkList[8]);
        valores.put("item10", checkList[9]);
        valores.put("item11", checkList[10]);
        valores.put("item12", checkList[11]);
        valores.put("item13", checkList[12]);
        valores.put("item14", checkList[13]);
        valores.put("item15", checkList[14]);
        valores.put("item16", checkList[15]);
        valores.put("item17", checkList[16]);
        valores.put("item18", checkList[17]);
        valores.put("item19", checkList[18]);
        valores.put("item20", checkList[19]);
        valores.put("item21", checkList[20]);
        valores.put("item22", checkList[21]);
        valores.put("item23", checkList[22]);
        valores.put("item24", checkList[23]);
        valores.put("item25", checkList[24]);
        valores.put("item26", checkList[25]);
        valores.put("item27", checkList[26]);
        valores.put("item28", checkList[27]);
        valores.put("item29", checkList[28]);
        valores.put("sincronizado", false);

        return db.insert(tabelaCheckList, null, valores) != -1;
    }

    public Integer ultimoID(String nomeTabela){

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT MAX("+nomeTabela+"ID) AS max_id FROM " + nomeTabela;
        Cursor cursor = db.rawQuery(query, null);

        Integer id = 0;
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(0);
            } while(cursor.moveToNext());
        }

        return id;
    }

    public Boolean atualizaStatusBDV(){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values =new ContentValues();
        values.put("sincronizado", 1);

        return db.update(tabelaBdv, values, new String ("sincronizado = ?"), new String[]{"0"}) != -1;
    }

    public Boolean checkStatusBDV(){
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM bdv WHERE sincronizado = 0";

        Cursor cursor = db.rawQuery(query, null);

        return cursor.moveToFirst();
    }

    public Bitmap getImage(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                tabelaBdv,
                new String[]{"*"},
                null,
                null,
                null,
                null,
                null
        );

        if(cursor.moveToFirst()){
            Bitmap aux;
            byte[] arrayB = cursor.getBlob(cursor.getColumnIndex("foto"));
            Log.i("TAMANHO", ""+arrayB.toString());
            ByteArrayInputStream inptStream = new ByteArrayInputStream(arrayB);
            aux = BitmapFactory.decodeStream(inptStream);

            return aux;
        }

        return null;
    }
}

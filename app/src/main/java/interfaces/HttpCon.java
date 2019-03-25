package interfaces;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.car.vale.bdvdigital.loadingSyncBDV;
import com.car.vale.bdvdigital.veiculoConfig;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method;

public class HttpCon {
    private String url;
    private Map<String, String> params;
    private RequestQueue rq;

    public HttpCon(Context context){
        this.url = "https://www.ammeletricistas.com.br/others/bdv_request.php";
        //this.url = "http://172.26.102.129/bdv_digital/app_request/bdv_request.php";
        this.rq = Volley.newRequestQueue(context);
    }

    public void CallJsonOR(final Context context){
        this.params = new HashMap<String, String>();
        this.params.put("operacao", "OPX_GET_MTR");
        CustomJsonObjectRequest cjor = new CustomJsonObjectRequest(
            Method.POST,
            this.url,
            this.params,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("Script", "onResponse: " + response);
                }
            },
            new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("ERORRR", "onResponse: " + error.getMessage());
                }
            }
        );

        cjor.setTag("tag");
        rq.add(cjor);
    }

    public void CallJsonAR(final Context context, final String msgErro){
        this.params = new HashMap<String, String>();
        this.params.put("OPX_GET_MTR", "OPX_GET_MTR");
        CustomJsonArrayRequest cjar = new CustomJsonArrayRequest(
                Method.POST,
                this.url,
                this.params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        BancoDados db = new BancoDados(context);
                        try {
                            db.atualizaMotoristas(response, context);
                            Log.i("Script", "onResponse: " + response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("ERORRR", "onResponse: " + error.getMessage());
                        Toast.makeText(context, msgErro, Toast.LENGTH_LONG).show();
                    }
                }
        );

        cjar.setTag("tag");
        rq.add(cjar);
    }

    public void CallBDVRequest(final Context context, final String msgOK, final String msgErro) throws JSONException {
        final BancoDados db = new BancoDados(context);
        this.params = new HashMap<String, String>();
        this.params.put("OPX_SET_BDV", db.getJSONArrayBDVs());
        CustomStringRequest cjar = new CustomStringRequest(
                Method.POST,
                this.url,
                this.params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("CHECK", response);
                        try {
                            if(response.compareTo("1#")>=0){
                                if(db.atualizaStatusBDV()){
                                    loadingSyncBDV._tela.finish();
                                    Toast.makeText(context, msgOK, Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Erro rede: ", e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("ERORRR", "onResponse: " + error.getMessage());
                        Toast.makeText(context, msgErro, Toast.LENGTH_LONG).show();
                    }
                }
        );

        cjar.setTag("tag");
        rq.add(cjar);
    }

    public void CallPassWordAdmRequest(final Context context, String senha, final String cartela, final String modelo, final String placa){
        final BancoDados db = new BancoDados(context);
        this.params = new HashMap<String, String>();
        this.params.put("OPX_GET_PSWRD", senha);
        CustomStringRequest cjar = new CustomStringRequest(
                Method.POST,
                this.url,
                this.params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("", response);
                        String aux[] = response.split("#");
                        try {
                            if(aux[0].equals("OK")){
                                if(db.insereVeiculo(cartela, modelo, placa)){
                                    Toast.makeText(context, "Configuração atualizada com sucesso!", Toast.LENGTH_LONG).show();
                                    veiculoConfig._tela.finish();
                                }else{
                                    Toast.makeText(context, "Erro ao atualizar configuração!", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(context, "Senha incorreta!", Toast.LENGTH_LONG).show();
                            }
                            Log.i("#Script#", "onResponse: " + response);
                        } catch (Exception e) {
                            Log.i("",e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("ERORRR", "onResponse: " + error.getMessage());
                    }
                }
        );

        cjar.setTag("tag");
        rq.add(cjar);
    }


}

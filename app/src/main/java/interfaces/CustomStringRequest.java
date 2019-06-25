package interfaces;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class CustomStringRequest extends Request<String> {

    private Response.Listener<String> response;
    private Map<String, String> params;

    public CustomStringRequest(int method, String url, Map<String, String> params, Response.Listener<String> response, Response.ErrorListener listener) {
        super(method, url, listener);

        this.params = params;
        this.response = response;
    }

    public CustomStringRequest(String url, Map<String, String> params, Response.Listener<String> response, Response.ErrorListener listener) {
        super(Method.GET, url, listener);

        this.params = params;
        this.response = response;
    }

    public Map<String, String > getParams() throws AuthFailureError {

        return this.params;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String js = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new String(js), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void deliverResponse(String response) {
        this.response.onResponse(response);
    }
}

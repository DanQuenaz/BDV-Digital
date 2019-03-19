package interfaces;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import estruturas.Configuracao;
import estruturas.Coordenada;
import estruturas.Trajeto;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

public class Localizacao implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private static Double _latitude;
    private static Double _longitude;

    public Localizacao(){
        _latitude  = 0.0;
        _longitude = 0.0;
    }

    public synchronized void callConnection(Context context){
        Log.i("LOG", "UpdateLocationActivity.callConnection()");
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    private void initLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    public void startLocationUpdate(){
        initLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,
                Localizacao.this);
    }


    public void stopLocationUpdate(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, Localizacao.this);
    }


    // LISTENERS
    @Override
    public void onConnected(Bundle bundle) {
        Log.i("LOG", "UpdateLocationActivity.onConnected(" + bundle + ")");

        Location l = LocationServices
                .FusedLocationApi
                .getLastLocation(mGoogleApiClient); // PARA JÁ TER UMA COORDENADA PARA O UPDATE FEATURE UTILIZAR

        //startLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("LOG", "UpdateLocationActivity.onConnectionSuspended(" + i + ")");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("LOG", "UpdateLocationActivity.onConnectionFailed(" + connectionResult + ")");
    }

    @Override
    public void onLocationChanged(Location location) {

        if(location.getLatitude() != _latitude || location.getLongitude() != _longitude) {
            if( calculaDistancia(_latitude, _longitude, location.getLatitude(), location.getLongitude()) >= 10.0 ) {
                Coordenada cord = new Coordenada(location, Configuracao.getRodovia());

                Trajeto.inicializa();
                Trajeto.insereCoordenada(cord);

                _latitude = location.getLatitude();
                _longitude = location.getLongitude();
            }
        }
    }

    public static Double calculaDistancia(Double la1, Double lo1, Double la2, Double lo2){

        Double d = new Double(0.0);
        Double raio = new Double(6371000.0);

        Double lat1 = toRadians(la1);
        Double lat2 = toRadians(la2);
        Double lon1 = toRadians(lo1);
        Double lon2 = toRadians(lo2);

        Double deltaLat = toRadians(la2 - la1);
        Double deltaLon = toRadians(lo2 - lo1);

        Double a = sin(deltaLat/2.0)*sin(deltaLat/2.0) + cos(lat1)*cos(lat2)*sin(deltaLon/2.0)*sin(deltaLon/2.0);
        Double c = 2.0 * atan2(sqrt(a), sqrt(1-a));

        d = c * raio;

        return d;

    }


}

package estruturas;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Coordenada {
    private Location loc;
    private String hora;
    private Boolean rodovia;

    public Coordenada(){}

    public Coordenada(Location loc, Boolean rodovia) {
        this.loc = loc;
        this.rodovia = rodovia;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        this.hora = format.format(new Date());
    }

    public Boolean getRodovia() {
        return rodovia;
    }

    public String getHora() {
        return hora;
    }

    public Double getLatitude(){
        return loc.getLatitude();
    }

    public Double getLongitude(){
        return loc.getLongitude();
    }

    public Double getAltitude(){
        return loc.getAltitude();
    }

    public Float getComportamento(){
        return loc.getBearing();
    }

    public Float getVelocidade(){
        return loc.getSpeed();
    }

    public String getProvedor(){
        return loc.getProvider();
    }

    public Float getAcuracia(){
        return loc.getAccuracy();
    }

}

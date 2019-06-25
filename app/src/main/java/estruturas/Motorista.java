package estruturas;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public final class Motorista {
    private static Integer _id;
    private static String _matricula;
    private static String _nome;
    private static String _rg;
    private static String _cpf;

    //HORA EXTRA
    private static String hora_login;
    private static String hora_logout;
    private static String hora_peimeira_rota;
    private static String hora_ultima_rota;
    private static Boolean primeira_rota;

    private Motorista(){}

    public static void getInstance(Integer id, String matricula, String nome, String rg, String cpf){
        _id = id;
        _matricula = matricula;
        _nome = nome;
        _rg = rg;
        _cpf = cpf;
        hora_login = "1970-12-12 00:00:00";
        hora_logout = "1970-12-12 00:00:00";
        hora_peimeira_rota = "1970-12-12 00:00:00";
        hora_ultima_rota = "1970-12-12 00:00:00";
        primeira_rota = true;
    }

    public static Integer get_id() {
        return _id;
    }

    public static String get_matricula() {
        return _matricula;
    }

    public static String get_nome() {
        return _nome;
    }

    public static String get_rg() {
        return _rg;
    }

    public static String get_cpf() {
        return _cpf;
    }

    public static String getHora_login() {
        return hora_login;
    }

    public static void setHora_login(String hora_login) {
        Motorista.hora_login = hora_login;
    }

    public static String getHora_logout() {
        return hora_logout;
    }

    public static void setHora_logout(String hora_logout) {
        Motorista.hora_logout = hora_logout;
    }

    public static String getHora_peimeira_rota() {
        return hora_peimeira_rota;
    }

    public static void setHora_peimeira_rota(String hora_peimeira_rota) {
        if(primeira_rota)Motorista.hora_peimeira_rota = hora_peimeira_rota;
        primeira_rota = false;
    }

    public static String getHora_ultima_rota() {
        return hora_ultima_rota;
    }

    public static void setHora_ultima_rota(String hora_ultima_rota) {
        Motorista.hora_ultima_rota = hora_ultima_rota;
    }

    public static  String getDifLogado() throws ParseException {
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date data1 = format2.parse(Motorista.hora_login);
        Date data2 = format2.parse(Motorista.hora_logout);

        long diff = data2.getTime() - data1.getTime();

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;

        String hour = diffHours+":"+diffMinutes+":"+diffSeconds;

        return hour;
    }

    public static  String getDifRota() throws ParseException {
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date data1 = format2.parse(Motorista.hora_peimeira_rota);
        Date data2 = format2.parse(Motorista.hora_ultima_rota);

        long diff = data2.getTime() - data1.getTime();

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;

        String hour = diffHours+":"+diffMinutes+":"+diffSeconds;

        return hour;
    }
}

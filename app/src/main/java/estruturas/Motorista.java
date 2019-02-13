package estruturas;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Objects;

public final class Motorista {
    private static Integer _id;
    private static String _matricula;
    private static String _nome;
    private static String _rg;
    private static String _cpf;

    private Motorista(){}

    public static void getInstance(Integer id, String matricula, String nome, String rg, String cpf){
        _id = id;
        _matricula = matricula;
        _nome = nome;
        _rg = rg;
        _cpf = cpf;
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

}

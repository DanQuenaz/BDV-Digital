package estruturas;

public final class VeiculoConfig {
    private static String veiculoCartela = null;
    private static String veiculoModelo = null;
    private static String veiculoPlaca = null;

    private VeiculoConfig(){}

    public boolean checkInstance(){
        return veiculoCartela != null;
    }

    public static void setInstance(String _cartela, String _modelo, String _placa){
        veiculoCartela = _cartela;
        veiculoModelo = _modelo;
        veiculoPlaca = _placa;
    }

    public static String getVeiculoCartela() {
        return veiculoCartela;
    }

    public static void setVeiculoCartela(String veiculoCartela) {
        VeiculoConfig.veiculoCartela = veiculoCartela;
    }

    public static String getVeiculoModelo() {
        return veiculoModelo;
    }

    public static void setVeiculoModelo(String veiculoModelo) {
        VeiculoConfig.veiculoModelo = veiculoModelo;
    }

    public static String getVeiculoPlaca() {
        return veiculoPlaca;
    }

    public static void setVeiculoPlaca(String veiculoPlaca) {
        VeiculoConfig.veiculoPlaca = veiculoPlaca;
    }
}

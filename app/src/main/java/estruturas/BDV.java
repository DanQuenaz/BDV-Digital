package estruturas;

/**
 * Created by danqu on 08/11/2018.
 */

public final class BDV {
    private static String motorista_nome;
    private static Integer motoristaID;
    private static String veiculo_cartela;
    private static String hora_inicial;
    private static String hora_final;
    private static Float km_inicial;
    private static Float km_final;
    private static String servico;
    private static byte[] foto;

    private BDV(){}

    public static String getMotorista_nome() {
        return motorista_nome;
    }

    public static void setMotorista_nome(String motorista_nome) {
        BDV.motorista_nome = motorista_nome;
    }

    public static Integer getMotoristaID() {
        return motoristaID;
    }

    public static void setMotoristaID(Integer motoristaID) {
        BDV.motoristaID = motoristaID;
    }

    public static String getVeiculo_cartela() {
        return veiculo_cartela;
    }

    public static void setVeiculo_cartela(String veiculo_cartela) {
        BDV.veiculo_cartela = veiculo_cartela;
    }

    public static String getHora_inicial() {
        return hora_inicial;
    }

    public static void setHora_inicial(String hora_inicial) {
        BDV.hora_inicial = hora_inicial;
    }

    public static String getHora_final() {
        return hora_final;
    }

    public static void setHora_final(String hora_final) {
        BDV.hora_final = hora_final;
    }

    public static Float getKm_inicial() {
        return km_inicial;
    }

    public static void setKm_inicial(Float km_inicial) {
        BDV.km_inicial = km_inicial;
    }

    public static Float getKm_final() {
        return km_final;
    }

    public static void setKm_final(Float km_final) {
        BDV.km_final = km_final;
    }

    public static Float getKm_total() {
        return km_final-km_inicial;
    }

    public static String getServico() {
        return servico;
    }

    public static void setServico(String servico) {
        BDV.servico = servico;
    }

    public static byte[] getFoto() {
        return foto;
    }

    public static void setFoto(byte[] foto) {
        BDV.foto = foto;
    }


    public static  boolean checkKM(){
        return (km_final > km_inicial);
    }



    public static void resetBDV(){
        motorista_nome = null;
        motoristaID = null;
        veiculo_cartela = null;
        hora_inicial = null;
        hora_final = null;
        km_inicial = null;
        km_final = null;
        servico = null;
    }

}

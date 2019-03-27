package estruturas;

public final class UploadStatus {
    private static Boolean _bdv;
    private static Boolean _checklist;
    private static Boolean _horaextra;
    private static Boolean _custosmotorista;

    private UploadStatus(){}

    static public void inicializa(){
        UploadStatus._bdv = false;
        UploadStatus._checklist = false;
        UploadStatus._horaextra = false;
        UploadStatus._custosmotorista = false;
    }

    public static Boolean get_bdv() {
        return _bdv;
    }

    public static void set_bdv(Boolean _bdv) {
        UploadStatus._bdv = _bdv;
    }

    public static Boolean get_checklist() {
        return _checklist;
    }

    public static void set_checklist(Boolean _checklist) {
        UploadStatus._checklist = _checklist;
    }

    public static Boolean get_horaextra() {
        return _horaextra;
    }

    public static void set_horaextra(Boolean _horaextra) {
        UploadStatus._horaextra = _horaextra;
    }

    public static Boolean get_custosmotorista() {
        return _custosmotorista;
    }

    public static void set_custosmotorista(Boolean _custosmotorista) {
        UploadStatus._custosmotorista = _custosmotorista;
    }
}

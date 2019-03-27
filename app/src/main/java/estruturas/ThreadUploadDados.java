package estruturas;

import com.car.vale.bdvdigital.loadingSyncBDV;

public class ThreadUploadDados extends Thread {
    private boolean _cont;

    public ThreadUploadDados(){
        _cont = true;
    }

    public void run(){
        while(_cont){
             if(UploadStatus.get_bdv() &&
                UploadStatus.get_checklist() &&
                UploadStatus.get_horaextra() &&
                UploadStatus.get_custosmotorista()){

                 _cont = false;
                 UploadStatus.inicializa();
                 loadingSyncBDV._tela.finish();

             }
        }
    }
}

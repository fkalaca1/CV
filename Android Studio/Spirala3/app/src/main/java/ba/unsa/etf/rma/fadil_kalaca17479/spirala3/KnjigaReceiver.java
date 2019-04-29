package ba.unsa.etf.rma.fadil_kalaca17479.spirala3;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class KnjigaReceiver extends ResultReceiver {
    private  Receiver kReceiver;

    public KnjigaReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver){
        kReceiver = receiver;
    }

    public interface Receiver{
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData){
        if(kReceiver!=null){
            kReceiver.onReceiveResult(resultCode,resultData);
        }
    }
}

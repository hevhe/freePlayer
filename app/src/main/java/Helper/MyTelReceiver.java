package Helper;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class MyTelReceiver extends BroadcastReceiver {


    PhoneStateListener listener=new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch(state){
                case TelephonyManager.CALL_STATE_IDLE:
                    System.out.println("挂断");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    System.out.println("接听");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    System.out.println("响铃:来电号码"+incomingNumber);
                    break;
            }
        }
    };


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String intentAction = intent.getAction();
        Toast.makeText(context,intentAction,Toast.LENGTH_LONG).show();
        Log.d("MainActivity", "onReceive: "+intentAction);
        if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.d("MainActivity", "call OUT:" + phoneNumber);
        }else{
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);
            tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }
}

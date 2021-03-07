package com.appsinventiv.realcaller.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.appsinventiv.realcaller.Utils.ApplicationClass;
import com.appsinventiv.realcaller.Utils.CommonUtils;

public class Receiver extends BroadcastReceiver {

    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private Context mContext;
    private Bundle mBundle;
    Intent mIntent;
    private static boolean isIncoming;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            mContext = context;
            mIntent = intent;
            mBundle = intent.getExtras();
            if (mBundle != null) {
                bundol(context, intent);
                //  getSMSDetails(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void bundol(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {

        } else {

            //Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
//            CommonUtils.showToast(number);
            if(number.length()>2){
                showDialog(number);
            }
            int state = 0;
            if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                state = TelephonyManager.CALL_STATE_IDLE;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                state = TelephonyManager.CALL_STATE_RINGING;
            }
            onCallStateChanged(context, state, number);
        }
    }

    private void showDialog(String number) {
        final Intent intent = new Intent(ApplicationClass.getInstance().getApplicationContext(), MyCustomDialog.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("phone_no",number);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                ApplicationClass.getInstance().getApplicationContext().startActivity(intent);
            }
        },2000);
    }

    public void onCallStateChanged(Context context, int state, String number) {

        if (lastState == state) {
            //No change, debounce extras
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false;
                } else {
                    isIncoming = true;
                }

                Toast.makeText(context, "OFF HOOK", Toast.LENGTH_SHORT).show();
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                } else if (isIncoming) {
                    Intent intent1 = new Intent();
                    intent1.setClassName(context.getPackageName(), "Start new Activity");
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                } else {
                    final Context cntx = context;
                    final Intent intent1 = new Intent();
                    intent1.setClassName(context.getPackageName(), "Start new Activity");
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    cntx.startActivity(intent1);
                    // Toast.makeText(context, "outgoing " + savedNumber + " Call time " + callStartTime +" Date " + new Date() , Toast.LENGTH_SHORT).show();

                }


                break;
        }


        lastState = state;
    }
}

package flynn.pro.flatears;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Map;
import java.util.Set;

import static android.text.TextUtils.isEmpty;


public class CallBroadcastReceiver extends BroadcastReceiver
{
    public static String TAG = "CLBRDRVR";


    @Override
    public void onReceive(Context context, Intent intent) {

        L.d(TAG, "Явный вызов с Добавками: " + intent.getExtras().toString());

        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String numberToCall = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            L.d(TAG, "В Добавках передан EXTRA_PHONE_NUMBER: " + numberToCall);
        }


        try {

            if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {

                String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

                if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    if (!isEmpty(number)) {
                        L.i(TAG, "ИСХОДЯЩИЙ звонок на номер " + number);
                        P.getInstance().setParam(P.CUR_TYPE, "OUTGOING");
                        P.getInstance().setParam(P.CUR_NUMB, number);
                    }
                } else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    if (!isEmpty(number)) {
                        L.i(TAG, "ВХОДЯЩИЙ звонок с номера " + number);
                        P.getInstance().setParam(P.CUR_TYPE, "INCOMING");
                        P.getInstance().setParam(P.CUR_NUMB, number);
                    }
                } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    // :: START HISTORY OBSERVER WHEN CHANGE TO IDLE -- DO NOT NEED ANYMORE
                    L.i(TAG, "Переход в состояние ожидания... ");
                }
            }



        }  catch (Exception e) {
            e.printStackTrace();
        }


        PhoneListener phoneListener = new PhoneListener(context);
        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        L.d(TAG, "Установлен Наблюдатель Состояния PhoneStateListener");



    }
}




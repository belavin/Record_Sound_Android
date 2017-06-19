package flynn.pro.flatears;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;


/**
 * Отслеживает изменение состояние телефона
 * и передаёт соответсвующие команды методу RecordService
 */
public class PhoneListener extends PhoneStateListener
{
    private Context context;
    public static String TAG = "PHNLSTNR";

    public PhoneListener(Context c) {
        L.i(TAG, "Наблюдатель Состояния собран.");
        context = c;
    }

    String[] states = {"IDLE (Ожидание)", "RINGING (Вызов)", "OFFHOOK (Ответ)"};

    public void onCallStateChanged (int state, String incomingNumber)
    {
        L.d(TAG, "Состояние изменено на " + states[state] + ". NUMB: " + incomingNumber);

        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                L.v(TAG, "Переход в состояние IDLE, останов записи");
                Boolean stopped = context.stopService(new Intent(context, RecordService.class));
                if (stopped) L.i(TAG, "Сервис записи успешно остановлен");
                    else L.w(TAG, "Ошибка останова / был остановлен ранее");

//            Intent i = context.getPackageManager().getLaunchIntentForPackage(
//            context.getPackageName());
//            //For resuming the application from the previous state
//            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//            //Uncomment the following if you want to restart the application instead of bring to front.
//            //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            context.startActivity(i);

                break;
            case TelephonyManager.CALL_STATE_RINGING:
                L.d(TAG, "Состояние RINGING, ожидание начала разговора");
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                L.d(TAG, "Состояние OFFHOOK, начало разговора, начало записи");
                Intent callIntent = new Intent(context, RecordService.class);
                callIntent.putExtra("incomingNumber", incomingNumber); // TODO :: это ещё используется ???
                ComponentName name = context.startService(callIntent);
                if (null == name) {
                    L.e(TAG, "startService вернул null ComponentName");
                } else {
                    L.i(TAG, "startService вернул " + name.flattenToString());
                }
                break;
        }
    }




}
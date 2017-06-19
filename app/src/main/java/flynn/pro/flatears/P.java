package flynn.pro.flatears;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Map;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;


public class P {

    public static final String TAG = "PREFRNCS";

    static public final String NEED_RECORD_CALLS = "PREF_RECORD_CALLS";
    static public final String NEED_SAVE_RECORDS = "PREF_SAVE_RECORDS";
    static public final String NEED_SEND_META = "PREF_SEND_META";
    static public final String NEED_LOG_TO_DB = "PREF_LOG_DB";
    static public final String NEED_LOG_TO_FILE = "PREF_LOG_FILE";

    static public final String[] boolPrefs = {
            NEED_RECORD_CALLS, NEED_SAVE_RECORDS, NEED_SEND_META, NEED_LOG_TO_DB, NEED_LOG_TO_FILE};

    static public final String AUDIO_SOURCE = "PREF_AUDIO_SOURCE";
    static public final String AUDIO_FORMAT = "PREF_AUDIO_FORMAT";
    static public final String SERVER_IP = "PREF_SERVER_IP";

    static public final String SIMID = "PREF_SIMID";
    static public final String DEVID = "PREF_DEVID";
    static public final String AN_ID = "PREF_AN_ID";

    static public final String FTP_STATE = "CUR_FTP_STATE";
    static public final String TNT_STATE = "CUR_TNT_STATE";
    static public final String NET_STATE = "CUR_NET_STATE";

    static public final String CUR_TYPE = "CUR_TYPE";
    static public final String CUR_NUMB = "CUR_NUMB";
    static public final String DEF_STOR_LOC = "DEF_STOR_LOC";
    static public final String CUR_STOR_LOC = "CUR_STOR_LOC";

    private static P sInstance;
    private final SharedPreferences prefs;

    private P() {
        prefs = getDefaultSharedPreferences(MainActivity.getContext());
    }

    public static synchronized void initializeInstance() {
        if (sInstance == null) {
            sInstance = new P();
        }
    }

    public static synchronized P getInstance() {
        if (sInstance == null) {
            try {
                throw new IllegalStateException(P.class.getSimpleName() +
                        " is not initialized, call initializeInstance(..) method first.");
            } catch (Exception e) {

                e.printStackTrace();

            }
        }
        return sInstance;
    }



    public void setState(String name, boolean state) {
        L.d(TAG, ">> ("+name+")="+state);
        prefs.edit()
                .putBoolean(name, state)
                .apply();
    }

    public boolean getState(String name) {
        boolean val = prefs.getBoolean(name, false);
        L.d(TAG, "<< ("+name+")="+val);
        return val;
    }

    public void setParam(String name, String value) {
        L.d(TAG, ">> ("+name+")="+value);
        prefs.edit()
                .putString(name, value)
                .apply();
    }

    public String getParam(String name) {
        String val;
        try {
            val = prefs.getString(name, "");
        } catch (ClassCastException e) {
            if (prefs.getBoolean(name, false))
                val = "ВКЛ";
            else val = "ВЫКЛ";
        }
        return val;
    }



    public void initPrefs() {
        L.v(TAG, "Сбор информации об устройсве...");
        TelephonyManager telemamanger = (TelephonyManager) MainActivity.getContext().
                getSystemService(Context.TELEPHONY_SERVICE);

        setParam(SIMID, telemamanger.getSimSerialNumber());

        setParam(DEVID, telemamanger.getDeviceId());
        setParam(AN_ID, android.provider.Settings.Secure.getString(MainActivity.getContext().
                getContentResolver(), android.provider.Settings.Secure.ANDROID_ID));

        L.v(TAG, "Добавление глобальных переменных...");
        setParam(CUR_TYPE, "INCOMING");
        setParam(CUR_NUMB, "89123456789");
        setParam(DEF_STOR_LOC, Environment.getExternalStorageDirectory().getPath());
        setParam(NET_STATE, "UNKNOWN");
        setParam(FTP_STATE, "UNKNOWN");
        setParam(TNT_STATE, "UNKNOWN");
        //TODO :: FLATEARS FOLDER


        L.v(TAG, "Чтение всех значений..");

        Map<String, ?> prefsMap = prefs.getAll();
        String everything = "";
        for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
            everything += "\r\n  ~ " + entry.getKey() + "\t --->  \t" + entry.getValue();
        }
        L.d(TAG, everything);
        
//        boolean isREC = (boolean) prefsMap.get(P.NEED_RECORD_CALLS);
//        if (isREC) {
//            L.i(TAG, "Запись звонков включена");
//        } else {
//            L.w(TAG, "Запись звонков выключена");
//            //return;
//        }
//
//        L.i(TAG, "MediaRecorder будет записывать из источника " + RecordService.audioSources.get(
//                Integer.parseInt((String) prefsMap.get(P.AUDIO_SOURCE))));
//
//        boolean isSave = (boolean) prefsMap.get(P.NEED_SAVE_RECORDS);
//        if (isSave) {
//            L.i(TAG, "Выгрузка файлов включена");
//        } else {
//            L.w(TAG, "Выгрузка файлов выключена");
//        }
//
//        boolean isSend = (boolean) prefsMap.get(P.NEED_SEND_META);
//        if (isSend) {
//            L.i(TAG, "Отправка метаданных включена");
//        } else {
//            L.w(TAG, "Отправка метаданных выключена");
//        }
//
//        L.i(TAG, "Адрес сервера выгрузки: " + prefsMap.get(P.SERVER_IP));

    }

}

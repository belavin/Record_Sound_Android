package flynn.pro.flatears;

import android.content.ContentValues;
import android.os.Looper;
import android.util.Log;

import java.util.Map;



public class Transmitter {
    final static String TAG = "TRNSMTTR";


    public static void transmit() {

        if (!P.getInstance().getParam(P.NET_STATE).equals("WIFIWIFI")) {
            L.d(TAG, "Сеть недоступна. Включите WiFi");
            return;
        }

        if (!P.getInstance().getParam(P.FTP_STATE).equals("ONLINE")) {
            L.d(TAG, "FTP недоступен. Проверьте параметры");
            return;
        }

        if (!RecordService.isRecording)
        new Thread(new Runnable() {
            public void run() {
                Looper.prepare();
                L.v(TAG, "Начало трэда выгрузки");

                final DatabaseHelper db = DatabaseHelper.getInstance(MainActivity.getContext());
                Map<String, String> map = db.getStates();
                int volume = map.size();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String filename = entry.getKey();
                    String status = ""+entry.getValue();
                    switch (status) {
                        case "INIT":
                        case "RECORDED":
                        case "FAILED":
                            L.v(TAG, filename + " ВЫГРУЖАЕТСЯ");
                            FTPUploader.uploadFile(filename);
                            break;
                        case "UPLOADED":
                        case "AUTHFAIL":
                        case "TELNETFAIL":
                            L.v(TAG, filename + " ИДЕНТИФИЦИРУЕТСЯ");
                            TelnetSpeakerTask myClientTask = new TelnetSpeakerTask(filename);
                            myClientTask.execute();
                            // TODO :: return if authfail
                            break;
                        case "IDENTIFIED":
                            L.v(TAG, filename + " УДАЛЯЕТСЯ");
                            FTPUploader.deleteFile(filename);
                            break;
                        default:
                            volume--;
                    }
                }
                if (volume == 0) L.v(TAG, "Ничего не случилось...");
                Looper.loop();
            }
        }).start();
        else {
            L.v(TAG, "Идёт запись. Процесс выгрузки отложен.");
        }


    }

    public static void transmit(final String filename) {
        final DatabaseHelper db = DatabaseHelper.getInstance(MainActivity.getContext());
        ContentValues cv = db.getValues(filename);
        switch (""+cv.get(DatabaseHelper.KEY_STATUS)) {
            case "RECORDED":
            case "FAILED":
                L.v(TAG, filename + " ВЫГРУЖАЕТСЯ");
                new Thread(new Runnable() {
                    public void run() {
                        FTPUploader.uploadFile(filename);
                    }
                }).start();
                break;
            case "UPLOADED":
            case "AUTHFAIL":
            case "TELNETFAIL":
                L.v(TAG, filename + " ИДЕНТИФИЦИРУЕТСЯ");
                TelnetSpeakerTask myClientTask = new TelnetSpeakerTask(filename);
                myClientTask.execute();
                break;
            case "IDENTIFIED":
                L.v(TAG, filename + " УДАЛЯЕТСЯ");
                //FTPUploader.deleteFile(filename);
                break;
            default:
                L.d(TAG, filename + " имеет статус " + cv.get(DatabaseHelper.KEY_STATUS));
        }
    }

}

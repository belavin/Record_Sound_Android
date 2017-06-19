package flynn.pro.flatears;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String TAG = NetworkUtil.TAG;
        L.i(TAG, "Обнаружено изменение состояние сети");

        if (!"android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {

            Intent i = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);


            ////NetworkUtil.updateStatus();
        }
    }
}


package flynn.pro.flatears;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;


public class NetworkUtil {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    public static String TAG = "NTWRKRVR";

    public static int getConnectivityStatus() {
        ConnectivityManager cm = (ConnectivityManager) MainActivity.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString() {
        int conn = NetworkUtil.getConnectivityStatus();
        String status = "";
        if (conn == TYPE_WIFI) {
            status = "NETWORK_STATUS_WIFI";
            P.getInstance().setParam(P.NET_STATE,"WIFI");
        } else if (conn == TYPE_MOBILE) {
            status = "NETWORK_STATUS_MOBILE";
            P.getInstance().setParam(P.NET_STATE,"GSM");
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "NETWORK_STATUS_NOT_CONNECTED";
            P.getInstance().setParam(P.NET_STATE,"OFFLINE");
        }
        return status;
    }

    public static void updateStatus() {
        L.i(TAG, "Status " + getConnectivityStatusString());
        if (P.getInstance().getParam(P.NET_STATE).equals("WIFI"))
        new was().execute("");
    }


     static class was extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            P.getInstance().setParam(P.FTP_STATE, FTPUploader.ftpStatus());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            AboutFragment.UpdateVisibleStates();
        }
    }

}

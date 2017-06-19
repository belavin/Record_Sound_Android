package flynn.pro.flatears;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class AboutFragment extends Fragment {

    public static final String TAG = "ABOUTFRG";

    public static AboutFragment newInstance() {

        return new AboutFragment();
    }

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UpdateVisibleStates();
    }

    public static void UpdateVisibleStates() {
        TextView tvStat = (TextView) ((AppCompatActivity)MainActivity.getContext()).findViewById(R.id.tvNetStat);
        if (tvStat!=null) {
            tvStat.setText("Статус сервиса: " + P.getInstance().getParam(P.NEED_RECORD_CALLS));
            tvStat.append("\r\nСтатус сети: " + P.getInstance().getParam(P.NET_STATE));
            tvStat.append("\r\nSIMID: " + P.getInstance().getParam(P.SIMID));
            tvStat.append("\r\nСтатус FTP: " + P.getInstance().getParam(P.FTP_STATE));
            tvStat.append("\r\nСтатус TELNET: " + P.getInstance().getParam(P.TNT_STATE));

        }
    }


}

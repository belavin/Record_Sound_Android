package flynn.pro.flatears;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by a.belavin on 17.05.2017.
 */

public class Preferences extends PreferenceActivity
{
    static public final String PREF_RECORD_CALLS = "PREF_RECORD_CALLS";
    static public final String PREF_AUDIO_SOURCE = "PREF_AUDIO_SOURCE";
    static public final String PREF_AUDIO_FORMAT = "PREF_AUDIO_FORMAT";
    static public final String PREF_NOTIFICATION = "NOTIFICATION_ALLOWED";
    static public final String CALLER_CONTACT= "CALLER CONTACT NUMBER";

    @Override
    @SuppressWarnings("Deprecated")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        setContentView(R.layout.fragment_settings);
    }
}

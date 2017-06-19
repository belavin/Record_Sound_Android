package flynn.pro.flatears;


import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import java.util.Arrays;


public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String TAG = P.TAG;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        addPreferencesFromResource(R.xml.preferences);
    }


    OnSharedPreferenceChangeListener myPrefListner = new OnSharedPreferenceChangeListener(){
         public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
             String val = "";
             if (Arrays.asList(P.boolPrefs).contains(key))
                 val += prefs.getBoolean(key, false);
             else val = prefs.getString(key, "");

             L.i(TAG, " Опция " + key + " изменена на " + val);

             AboutFragment.UpdateVisibleStates();
         }
     };


     @Override
     public void onResume() {
         super.onResume();
         getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(myPrefListner);

     }


     @Override
     public void onPause() {
         super.onPause();
         getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(myPrefListner);
     }

 }

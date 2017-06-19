package flynn.pro.flatears;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by a.belavin on 11.05.2017.
 */

//public class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//    public SectionsPagerAdapter(FragmentManager fm) {
//        super(fm);
//    }
//
//    //  @Override
//    public Fragment getItem(int position) {
//        // getItem is called to instantiate the fragment for the given page.
//        // Return a PlaceholderFragment (defined as a static inner class below).
//        //return PlaceholderFragment.newInstance(position + 1);
//
//        switch (position){
//
//            case 0 :
//                return AboutFragment.newInstance();
//            case 1 :
//                return SettingsFragment.newInstance();
//
//
//        }
//
//        switch (position) {
//            case 0:
//                return AboutFragment.newInstance();
//            //break;
//
//            case 1:
//                return SettingsFragment.newInstance();
//            //break;
//
//            case 2:
//                return UploadFragment.newInstance();
//            //break;
//        }
//        return PlaceholderFragment.newInstance(position + 1);
//    }
//
//    //  @Override
//    public int getCount() {
//        // Show 3 total pages.
//        return 3;
//    }
//
//    // @Override
//    public CharSequence getPageTitle(int position) {
//        switch (position) {
//            case 0:
//                return "ИНФО";
//            case 1:
//                return "ОПЦИИ";
//            case 2:
//                return "ЗАПИСИ";
//        }
//        return null;
//    }
//}

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (position == 0)
            return AboutFragment.newInstance();
        else if (position == 1)
            return SettingsFragment.newInstance();
        else if (position == 2)
            return UploadFragment.newInstance();

        else return AboutFragment.newInstance();
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "ИНФО";
            case 1:
                return "ОПЦИИ";
            case 2:
                return "ЗАПИСИ";
        }
        return null;
    }
}
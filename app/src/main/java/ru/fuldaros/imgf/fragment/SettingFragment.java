package ru.fuldaros.imgf.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import ru.fuldaros.imgf.R;
import ru.fuldaros.imgf.core.ImageFactory;
import ru.fuldaros.imgf.ui.Dialog;
import ru.fuldaros.imgf.ui.Toast;
import ru.fuldaros.imgf.util.ShellUtils;
import ru.fuldaros.imgf.util.XmlDataUtils;

/**
 * Created by fuldaros on 2016/8/12.
 */
public class SettingFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.setting);
        findPreference(getString(R.string.setting_clean_data_key)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference p1) {
                // TODO: Implement this method
                Dialog.create(getActivity())
                        .setTitle(getString(R.string.setting_clean_data_title))
                        .setMessage(getString(R.string.are_you_sure_you_want_to_do_this))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface p1, int p2) {
                                // TODO: Implement this method
                                XmlDataUtils.remove(getString(R.string.setting_data_path_key));
                                try {
                                    ShellUtils.exec(String.format("rm -r \'%s\' \'%s\' \'%s\'", ImageFactory.DATA_PATH.getPath(), ImageFactory.getApp().getFilesDir().getPath(), ImageFactory.getApp().getApplicationInfo().nativeLibraryDir));
                                    android.os.Process.killProcess(Process.myPid());
                                } catch (Exception e) {
                                    Toast.makeShortText(String.format(getString(R.string.operation_failed), e.toString()));
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                return false;
            }
        });
    }
}

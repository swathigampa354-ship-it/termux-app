package com.termux.app.fragments.settings.termux;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceDataStore;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.termux.R;
import com.termux.shared.termux.settings.preferences.TermuxAppSharedPreferences;
import com.termux.shared.termux.settings.properties.TermuxPropertyConstants;

@Keep
public class TerminalIOPreferencesFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        Context context = getContext();
        if (context == null) return;

        PreferenceManager preferenceManager = getPreferenceManager();
        preferenceManager.setPreferenceDataStore(TerminalIOPreferencesDataStore.getInstance(context));

        setPreferencesFromResource(R.xml.termux_terminal_io_preferences, rootKey);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Show the effective terminal session drawer side, which is the stored value or the
        // termux.properties `terminal-drawer-side` value if not set in preferences yet.
        Context context = getContext();
        Preference terminalDrawerSide = findPreference("terminal_drawer_side");
        if (context != null && terminalDrawerSide != null) {
            TermuxAppSharedPreferences preferences = TermuxAppSharedPreferences.build(context, true);
            String side = null;
            if (preferences != null)
                side = preferences.getTerminalDrawerSide();
            if (side == null || side.isEmpty()) {
                // When not set in the settings, the summary shows the effective value that
                // TermuxActivity is using, so always keep it updated on return to this screen.
                side = TermuxPropertyConstants.DEFAULT_IVALUE_TERMINAL_DRAWER_SIDE;
            }
            if (terminalDrawerSide instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) terminalDrawerSide;
                int index = listPreference.findIndexOfValue(side);
                CharSequence entry = null;
                if (listPreference.getEntries() != null && index >= 0 && index < listPreference.getEntries().length)
                    entry = listPreference.getEntries()[index];
                if (entry != null) {
                    terminalDrawerSide.setSummary(getString(R.string.termux_session_drawer_side_summary));
                    listPreference.setSummary(entry);
                } else {
                    terminalDrawerSide.setSummary(getString(R.string.termux_session_drawer_side_summary));
                }
            }
        }
    }

}

class TerminalIOPreferencesDataStore extends PreferenceDataStore {

    private final Context mContext;
    private final TermuxAppSharedPreferences mPreferences;

    private static TerminalIOPreferencesDataStore mInstance;

    private TerminalIOPreferencesDataStore(Context context) {
        mContext = context;
        mPreferences = TermuxAppSharedPreferences.build(context, true);
    }

    public static synchronized TerminalIOPreferencesDataStore getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new TerminalIOPreferencesDataStore(context);
        }
        return mInstance;
    }



    @Override
    public void putBoolean(String key, boolean value) {
        if (mPreferences == null) return;
        if (key == null) return;

        switch (key) {
            case "soft_keyboard_enabled":
                    mPreferences.setSoftKeyboardEnabled(value);
                break;
            case "soft_keyboard_enabled_only_if_no_hardware":
                mPreferences.setSoftKeyboardEnabledOnlyIfNoHardware(value);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        if (mPreferences == null) return false;

        switch (key) {
            case "soft_keyboard_enabled":
                return mPreferences.isSoftKeyboardEnabled();
            case "soft_keyboard_enabled_only_if_no_hardware":
                return mPreferences.isSoftKeyboardEnabledOnlyIfNoHardware();
            default:
                return false;
        }
    }

    @Override
    public void putString(String key, String value) {
        if (mPreferences == null || key == null) return;

        switch (key) {
            case "terminal_drawer_side":
                if (TermuxPropertyConstants.IVALUE_TERMINAL_DRAWER_SIDE_START.equals(value)
                    || TermuxPropertyConstants.IVALUE_TERMINAL_DRAWER_SIDE_END.equals(value))
                    mPreferences.setTerminalDrawerSide(value);
                break;
            default:
                break;
        }
    }

    @Override
    public String getString(String key, String defValue) {
        if (mPreferences == null) return defValue;

        switch (key) {
            case "terminal_drawer_side":
                return mPreferences.getTerminalDrawerSide();
            default:
                return defValue;
        }
    }
}

package com.appspot.cee_me.android.activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.appspot.cee_me.android.R;

/**
 * Standard settings fragment.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}

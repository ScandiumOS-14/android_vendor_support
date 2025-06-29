/*
 * Copyright (C) 2017 AICP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scandium.home.preferences;

import android.content.Context;
import androidx.preference.EditTextPreference;
import android.text.TextUtils;
import android.util.AttributeSet;

public class SystemSettingEditTextPreference extends EditTextPreference {
    private boolean mAutoSummary = false;

    public SystemSettingEditTextPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setPreferenceDataStore(new SystemSettingsStore(context.getContentResolver()));
    }

    public SystemSettingEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPreferenceDataStore(new SystemSettingsStore(context.getContentResolver()));
    }

    public SystemSettingEditTextPreference(Context context) {
        super(context);
        setPreferenceDataStore(new SystemSettingsStore(context.getContentResolver()));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        // This is what default ListPreference implementation is doing without respecting
        // real default value:
        //setText(restoreValue ? getPersistedString(mText) : (String) defaultValue);
        // Instead, we better do
        setText(restoreValue ? getPersistedString((String) defaultValue) : (String) defaultValue);
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        if (mAutoSummary || TextUtils.isEmpty(getSummary())) {
            setSummary(text, true);
        }
    }

    @Override
    public void setSummary(CharSequence summary) {
        setSummary(summary, false);
    }

    private void setSummary(CharSequence summary, boolean autoSummary) {
        mAutoSummary = autoSummary;
        super.setSummary(summary);
    }
}


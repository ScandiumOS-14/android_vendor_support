/*
 * Copyright (C) 2016-2019 crDroid Android Project
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
import android.util.AttributeSet;

public class GlobalSettingSeekBarPreference extends CustomSeekBarPreference {

    public GlobalSettingSeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setPreferenceDataStore(new GlobalSettingsStore(context.getContentResolver()));
    }

    public GlobalSettingSeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPreferenceDataStore(new GlobalSettingsStore(context.getContentResolver()));
    }

    public GlobalSettingSeekBarPreference(Context context) {
        super(context, null);
        setPreferenceDataStore(new GlobalSettingsStore(context.getContentResolver()));
    }
}

/*
 * Copyright (C) 2007 The Android Open Source Project
 * Copyright (C) 2018-2024 Roumen Petrov.  All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jackpal.androidterm.compat;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.TextUtils;

import com.termoneplus.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import androidx.preference.PreferenceManager;


/* NOTE: refactored path settings from TermSettings.java
 * TODO: pending removal
 */
@Deprecated
public class PathSettings {
    private static String mPrependPath = "";
    private static String mAppendPath = "";

    // extracted from SharedPreferences
    private static boolean verify_path;
    private static boolean collect_path;


    public PathSettings(Context context) {
        Resources res = context.getResources();
        verify_path = res.getBoolean(R.bool.pref_verify_path_default);
        collect_path = res.getBoolean(R.bool.pref_collect_path_default);
        extractPreferences(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public static void extractPreferences(Context context, SharedPreferences prefs) {
        Resources res = context.getResources();
        String key;

        key = res.getString(R.string.key_verify_path_preference);
        verify_path = prefs.getBoolean(key, verify_path);

        key = context.getString(R.string.key_collect_path_preference);
        collect_path = prefs.getBoolean(key, collect_path);
    }

    public static ArrayList<String> getCollectedPaths() {
        ArrayList<String> ret = new ArrayList<>(mPrependPath.length() + mAppendPath.length());
        Collections.addAll(ret, mPrependPath.split(File.pathSeparator));
        Collections.addAll(ret, mAppendPath.split(File.pathSeparator));
        return ret;
    }

    public static String getPrependPathVerified() {
        if (verify_path)
            return preservePath(mPrependPath);
        return mPrependPath;
    }

    public static String getAppendPathVerified() {
        if (verify_path)
            return preservePath(mAppendPath);
        return mAppendPath;
    }

    public static boolean usePathCollection () {
        return collect_path;
    }

    private static String preservePath(String path) {
        String[] entries = path.split(File.pathSeparator);
        StringBuilder new_path = new StringBuilder(path.length());
        for (String entry : entries) {
            if (TextUtils.isEmpty(entry)) continue;
            File dir = new File(entry);
            try {
                if (!dir.isDirectory()) continue;
            } catch (SecurityException ignore) {
                continue;
            }
            if (dir.canExecute()) {
                new_path.append(entry);
                new_path.append(File.pathSeparator);
            }
        }
        if (new_path.length() < 1) return null;
        return new_path.substring(0, new_path.length() - 1);
    }

    public void setPrependPath(ArrayList<String> paths) {
        mPrependPath = buildPath(paths);
    }

    public void setAppendPath(ArrayList<String> paths) {
        mAppendPath = buildPath(paths);
    }

    private String buildPath(ArrayList<String> paths) {
        if (paths == null || paths.size() < 1) return "";

        StringBuilder builder = new StringBuilder();
        for (String element : paths) {
            builder.append(element);
            builder.append(File.pathSeparator);
        }
        return builder.substring(0, builder.length() - 1);
    }
}

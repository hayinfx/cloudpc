/*
 * Copyright (C) 2007 The Android Open Source Project
 * Copyright (C) 2017-2024 Roumen Petrov.  All rights reserved.
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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import com.termoneplus.BuildConfig;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;


/* NOTE: refactored broadcast functionality from Term.java
 * Applications that target Android 8.0 (Oreo, API Level 26) or higher no
 * longer receive implicit broadcasts registered in their manifest.
 * Broadcast registered at run-time are excluded but we would like
 * to receive paths from all application not only from running.
 * TODO: pending removal of deprecated path collection based on broadcasts.
 */
@Deprecated
public class PathCollector {
    // NOTE: use termoneplus broadcasts!
    private static final String ACTION_PATH_APPEND_BROADCAST = BuildConfig.APPLICATION_ID + ".broadcast.APPEND_TO_PATH";
    private static final String ACTION_PATH_PREPEND_BROADCAST = BuildConfig.APPLICATION_ID + ".broadcast.PREPEND_TO_PATH";
    private static final String PERMISSION_PATH_APPEND_BROADCAST = BuildConfig.APPLICATION_ID + ".permission.APPEND_TO_PATH";
    private static final String PERMISSION_PATH_PREPEND_BROADCAST = BuildConfig.APPLICATION_ID + ".permission.PREPEND_TO_PATH";

    private int pending;
    private OnPathsReceivedListener callback;

    private static ArrayList<String> makePathListFromBundle(Bundle extras) {
        if (extras == null || extras.size() < 1)
            return null;

        ArrayList<String> ret = new ArrayList<>(extras.size());
        for (String key : extras.keySet()) {
            String dir = extras.getString(key);
            if (TextUtils.isEmpty(dir)) continue;
            ret.add(dir);
        }

        return ret;
    }

    public static void collect(Context context, OnPathsReceivedListener listener) {
        final PathCollector collector = new PathCollector();
        collector.setOnPathsReceivedListener(listener);
        collector.start(context);
    }

    private void start (Context context) {
        final PathSettings settings = new PathSettings(context);
        if (!PathSettings.usePathCollection()) {
            if (callback != null)
                callback.onPathsReceived();
            return;
        }

        pending = 2;

        BroadcastReceiver receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == null) return;

                ArrayList<String> path = makePathListFromBundle(getResultExtras(false));
                switch (action) {
                    case ACTION_PATH_PREPEND_BROADCAST:
                        settings.setPrependPath(path);
                        break;
                    case ACTION_PATH_APPEND_BROADCAST:
                        settings.setAppendPath(path);
                        break;
                    default:
                        return;
                }
                --pending;

                if (pending <= 0 && callback != null)
                    callback.onPathsReceived();
            }
        };

        Intent broadcast = new Intent(ACTION_PATH_APPEND_BROADCAST);
        broadcast.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        context.sendOrderedBroadcast(broadcast, PERMISSION_PATH_APPEND_BROADCAST,
                receiver, null, AppCompatActivity.RESULT_OK, null, null);

        broadcast = new Intent(broadcast);
        broadcast.setAction(ACTION_PATH_PREPEND_BROADCAST);
        context.sendOrderedBroadcast(broadcast, PERMISSION_PATH_PREPEND_BROADCAST,
                receiver, null, AppCompatActivity.RESULT_OK, null, null);
    }

    public static void extractPreferences(Context context, SharedPreferences prefs) {
        PathSettings.extractPreferences(context, prefs);
    }

    public void setOnPathsReceivedListener(OnPathsReceivedListener listener) {
        callback = listener;
    }

    public interface OnPathsReceivedListener {
        void onPathsReceived();
    }
}

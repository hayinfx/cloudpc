/*
 * Copyright (C) 2021-2024 Roumen Petrov.  All rights reserved.
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

package com.termoneplus;

import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


@RequiresApi(30)
public class PermissionManageExternal {
    public static final boolean active = false;

    @SuppressWarnings("SameReturnValue")
    public static boolean isGranted() {
        return false;
    }

    @SuppressWarnings({"unused", "EmptyMethod"})
    public static void request(AppCompatActivity activity, View view, int requestCode) {
    }
}

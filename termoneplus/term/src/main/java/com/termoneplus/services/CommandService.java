/*
 * Copyright (C) 2019-2024 Roumen Petrov.  All rights reserved.
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

package com.termoneplus.services;

import android.os.Process;
import android.text.TextUtils;

import com.termoneplus.BuildConfig;
import com.termoneplus.Installer;
import com.termoneplus.compat.PackageManagerCompat;
import com.termoneplus.remote.CommandCollector;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

import jackpal.androidterm.TermService;
import jackpal.androidterm.compat.PathSettings;


public class CommandService implements UnixSocketServer.ConnectionHandler {
    private static final String socket_prefix = BuildConfig.APPLICATION_ID + "-app_info-";

    private final TermService service;
    private UnixSocketServer socket;

    public CommandService(TermService service) {
        this.service = service;
        try {
            socket = new UnixSocketServer(socket_prefix + Process.myUid(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printExternalAliases(ProcessBuilder pb, PrintStream out) {
        try {
            java.lang.Process p = pb.start();

            // close process "input stream" to prevent command
            // to wait for user input.
            p.getOutputStream().close();

            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while (true) {
                String line = in.readLine();
                if (line == null) break;
                out.println(line);
            }
            out.flush();
        } catch (IOException ignore) {
        }
    }

    private boolean printExternalAliases2(File cmd, PrintStream out) {
        ArrayList<String> name = new ArrayList<>();
        try {
            ProcessBuilder pb = new ProcessBuilder(cmd.getPath(), "package");
            java.lang.Process p = pb.start();

            // close process "input stream" to prevent command
            // to wait for user input.
            p.getOutputStream().close();

            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while (true) {
                String line = in.readLine();
                if (line == null) break;
                name.add(line);
            }
        } catch (IOException ignore) {
            return false;
        }
        if (name.size() != 1) return false;

        int uid = PackageManagerCompat.getApplicationUID(service.getPackageManager(), name.get(0));
        if (uid < 0) return false;

        ProcessBuilder pb = new ProcessBuilder(cmd.getPath(), "v2", String.valueOf(uid), "aliases");
        printExternalAliases(pb, out);
        return true;
    }

    public void start() {
        if (socket == null) return;
        socket.start();
    }

    public void stop() {
        if (socket == null) return;
        socket.stop();
        socket = null;
    }

    @Override
    public void handle(InputStream basein, OutputStream baseout) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(basein));

        // Note only one command per connection!
        String line = in.readLine();
        if (TextUtils.isEmpty(line)) return;

        switch (line) {
            case "get aliases":
                printAliases(baseout);
                break;
            case "get cmd_path":
                handleCommandPath(in, baseout);
                break;
            case "get cmd_env":
                handleCommandEnvironment(in, baseout);
                break;
            case "open sysconfig":
                handleCommandConfiguration(in, baseout);
                break;
            // TODO @Deprecated
            case "legacy app_dir":
                legacyCommandDirectory(in, baseout);
                break;
        }
    }

    private void printAliases(OutputStream baseout) {
        PrintStream out = new PrintStream(baseout);

        // force interactive shell
        out.println("alias sh='sh -i'");

        printExternalAliases(out);
        if (!TextUtils.isEmpty(Installer.APPEXEC_COMMAND))
            CommandCollector.printExternalAliases(out);
        out.flush();
    }

    private void printExternalAliases(PrintStream out) {
        final Pattern pattern = Pattern.compile("libexec-(.*).so");

        for (String entry : PathSettings.getCollectedPaths()) {
            File dir = new File(entry);

            File[] cmdlist = null;
            try {
                cmdlist = dir.listFiles(file -> pattern.matcher(file.getName()).matches());
            } catch (Exception ignore) {
            }
            if (cmdlist == null) continue;

            for (File cmd : cmdlist) {
                if (printExternalAliases2(cmd, out))
                    continue;
                ProcessBuilder pb = new ProcessBuilder(cmd.getPath(), "aliases");
                printExternalAliases(pb, out);
            }
        }
    }

    private ArrayList<String> getArguments(BufferedReader in) throws IOException {
        // Note "end of line" command is required.
        ArrayList<String> args = new ArrayList<>();
        boolean eol = false;
        do {
            String line = in.readLine();
            if (TextUtils.isEmpty(line)) break;
            if ("<eol>".equals(line)) {
                eol = true;
                break;
            }
            args.add(line);
        } while (true);
        return eol ? args : null;
    }

    private void endResponse(OutputStream baseout) throws IOException {
        baseout.flush();

        PrintStream out = new PrintStream(baseout);
        out.println("<eol>");
        out.flush();
    }

    private void handleCommandPath(BufferedReader in, OutputStream out) throws IOException {
        if (TextUtils.isEmpty(Installer.APPEXEC_COMMAND)) return;

        ArrayList<String> args = getArguments(in);
        if (args == null) return;

        CommandCollector.writeCommandPath(service.getApplicationContext(), args, out);
        endResponse(out);
    }

    private void handleCommandEnvironment(BufferedReader in, OutputStream out) throws IOException {
        ArrayList<String> args = getArguments(in);
        if (args == null) return;

        CommandCollector.writeCommandEnvironment(service.getApplicationContext(), args, out);
        endResponse(out);
    }

    private void handleCommandConfiguration(BufferedReader in, OutputStream out) throws IOException {
        ArrayList<String> args = getArguments(in);
        if (args == null) return;

        CommandCollector.openCommandConfiguration(service.getApplicationContext(), args, out);
    }

    // TODO @Deprecated
    private void legacyCommandDirectory(BufferedReader in, OutputStream out) throws IOException {
        ArrayList<String> args = getArguments(in);
        if (args == null) return;

        CommandCollector.legacyCommandDirectory(service.getApplicationContext(), args, out);
    }
}

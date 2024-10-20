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

#include <memory.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include "appinfo.h"

/* TODO deprecated.
 * Unix like build process sets installation directories at build time.
 * Legacy command asks application where a directory is installed and
 * redirect to installed allocations.
 * Note that build uses simplified directory structure with minimal
 * changes to "prefix". Also sysconfdir may not be accessible unless
 * application share "user id" with terminal.
 */
char *get2_app_etcdir(const char *cmd);

char *get2_app_bindir(const char *cmd);

char *get2_app_libexecdir(const char *cmd);


static char *
get2_app_dir(const char *cmd, const char *dir) {
    char msg[128];
    int msgres;
    char pathname[PATH_MAX + 1]; /* plus eof */
    int sock;
    size_t len, res;

    msgres = snprintf(msg, sizeof(msg), "legacy app_dir\n%s\n%s\n<eol>\n", cmd, dir);
    if (msgres < 0 || msgres >= sizeof(msg))
        return NULL;

    sock = open_appsocket();
    if (sock == -1) return NULL;

    if (!write_msg(sock, msg)) {
        res = 0;
        goto done;
    }

    len = sizeof(pathname);
    memset(&pathname, 0, len);
    res = atomicio(read, sock, pathname, len);

    done:
    close(sock);

    if (res >= sizeof(pathname)) return NULL;
    if (res <= 1) return NULL; /* at least eol */
    if (pathname[--res] != '\n') return NULL;
    pathname[res] = '\0';

    return strdup(pathname);
}

char *
get2_app_etcdir(const char *cmd) {
    return get2_app_dir(cmd, "etc");
}

char *
get2_app_bindir(const char *cmd) {
    return get2_app_dir(cmd, "bin");
}

char *
get2_app_libexecdir(const char *cmd) {
    return get2_app_dir(cmd, "libexec");
}

//
// ========================================================================
// Copyright (c) 1995 Mort Bay Consulting Pty Ltd and others.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v. 2.0 which is available at
// https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
// which is available at https://www.apache.org/licenses/LICENSE-2.0.
//
// SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
// ========================================================================
//

package org.eclipse.jetty.setuid.internal;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import org.eclipse.jetty.setuid.Group;
import org.eclipse.jetty.setuid.Passwd;
import org.eclipse.jetty.setuid.RLimit;

/**
 * <p>Class is for changing user and groupId, it can also be used to retrieve user information
 * by using {@code getpwuid(uid)} or {@code getpwnam(username)} of both linux and unix systems.</p>
 */
public interface LibC extends Library
{
    LibC INSTANCE = Native.load("c", LibC.class);

    int umask(int mask);

    int setuid(int uid);

    int setgid(int gid);

    int setgroups(int[] gids);

    Passwd getpwnam(String name);

    Passwd getpwuid(int uid);

    Group getgrnam(String name);

    Group getgrgid(int gid);

    int getrlimit(int resource, RLimit rLimit);

    int setrlimit(int resource, RLimit rlimit);

    /**
     * <p>Compile and run the following C program to get the {@code RLIMIT_NOFILE} value of you OS of choice.</p>
     * <pre>{@code
     * #include <stdio.h>
     * #include <sys/resource.h>
     *
     * int main()
     * {
     *   printf("RLIMIT_NOFILE = %d\n", RLIMIT_NOFILE);
     *   return 0;
     * }
     * }</pre>
     */
    class Constants
    {
        public static final int RLIMIT_NOFILE;
        static
        {
            if (Platform.isMac())
                RLIMIT_NOFILE = 8;
            else if (Platform.isSolaris())
                RLIMIT_NOFILE = 5;
            else if (Platform.isAIX())
                RLIMIT_NOFILE = 6;
            else if (Platform.isLinux())
                RLIMIT_NOFILE = 7;
            else
                RLIMIT_NOFILE = Integer.MIN_VALUE;
        }
    }
}

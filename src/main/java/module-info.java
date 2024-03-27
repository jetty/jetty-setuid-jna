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

module org.eclipse.jetty.setuid.jna
{
    requires com.sun.jna;
    requires org.eclipse.jetty.server;
    requires org.eclipse.jetty.util;
    requires org.slf4j;

    // needed to allow internal classes to use com.sun.jna
    opens org.eclipse.jetty.setuid.internal;

    exports org.eclipse.jetty.setuid;
}
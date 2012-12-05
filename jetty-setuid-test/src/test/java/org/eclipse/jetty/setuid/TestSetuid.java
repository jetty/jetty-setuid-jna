//
//  ========================================================================
//  Copyright (c) 1995-2012 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.setuid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.junit.Test;

public class TestSetuid
{

    @Test
    public void testSetuid() throws Exception
    {

        try
        {
            /*
             * This is a test of the -VERSION based loading mechanism, the jetty.lib logic in SetUID looks in a directory of jetty.lib and tries to load the
             * file ending in the VERISON.so which is the mechanism used by default in jetty-distro now
             */
            File lib = new File(MavenTestingUtils.getTargetDir(),"distro");
            String libPath = lib.getCanonicalPath();
            System.setProperty("jetty.lib",libPath);

            try
            {
                SetUID.getpwnam("TheQuickBrownFoxJumpsOverToTheLazyDog");
                assertTrue(false);
            }
            catch (SecurityException se)
            {
                assertTrue(true);
            }

            try
            {
                SetUID.getpwuid(-9999);
                assertTrue(false);
            }
            catch (SecurityException se)
            {
                assertTrue(true);
            }

            // get the passwd info of root
            Passwd passwd1 = SetUID.getpwnam("root");
            // get the roots passwd info using the aquired uid
            Passwd passwd2 = SetUID.getpwuid(passwd1.getPwUid());

            assertEquals(passwd1.getPwName(),passwd2.getPwName());
            assertEquals(passwd1.getPwPasswd(),passwd2.getPwPasswd());
            assertEquals(passwd1.getPwUid(),passwd2.getPwUid());
            assertEquals(passwd1.getPwGid(),passwd2.getPwGid());
            assertEquals(passwd1.getPwGecos(),passwd2.getPwGecos());
            assertEquals(passwd1.getPwDir(),passwd2.getPwDir());
            assertEquals(passwd1.getPwShell(),passwd2.getPwShell());

            try
            {
                SetUID.getgrnam("TheQuickBrownFoxJumpsOverToTheLazyDog");
                assertTrue(false);
            }
            catch (SecurityException se)
            {
                assertTrue(true);
            }

            try
            {
                SetUID.getgrgid(-9999);
                assertTrue(false);
            }
            catch (SecurityException se)
            {
                assertTrue(true);
            }

            // get the group using the roots groupid
            Group gr1 = SetUID.getgrgid(passwd1.getPwGid());
            // get the group name using the aquired name
            Group gr2 = SetUID.getgrnam(gr1.getGrName());

            assertEquals(gr1.getGrName(),gr2.getGrName());
            assertEquals(gr1.getGrPasswd(),gr2.getGrPasswd());
            assertEquals(gr1.getGrGid(),gr2.getGrGid());

            // search and check through membership lists
            if (gr1.getGrMem() != null)
            {
                assertEquals(gr1.getGrMem().length,gr2.getGrMem().length);
                for (int i = 0; i < gr1.getGrMem().length; i++)
                {
                    assertEquals(gr1.getGrMem()[i],gr2.getGrMem()[i]);
                }
            }

        }
        catch (Throwable e)
        {
            e.printStackTrace();
            assertTrue(false);
        }
    }

}

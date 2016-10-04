package org.jvnet.hudson.plugins.triggers.startup;

/*
 * The MIT License
 *
 * Copyright (c) 2016, Schneider Electric
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import com.cloudbees.hudson.plugins.folder.Folder;
import hudson.model.FreeStyleProject;
import org.jvnet.hudson.test.HudsonTestCase;
import hudson.slaves.DumbSlave;


public class HudsonComputerListenerTest extends HudsonTestCase {
    public void testRootJobConnect() throws Exception {
        // Create job with startup trigger
        FreeStyleProject job = createFreeStyleProject("job");
        job.addTrigger(new HudsonStartupTrigger("slave0", null, null, "ON_CONNECT"));

        // Create slave which node name will be slave0
        createOnlineSlave();

        // Wait for the completion of the build
        waitUntilNoActivity();

        assertTrue(job.getLastSuccessfulBuild().number == 1);
    }

    public void testRootJobOnline() throws Exception {
        // Create job with startup trigger
        FreeStyleProject job = createFreeStyleProject("job");
        job.addTrigger(new HudsonStartupTrigger("slave0", null, null, "ON_ONLINE"));

        // Create slave which node name will be slave0
        DumbSlave slave = createOnlineSlave();

        // Wait for the completion of the build (there should be none)
        waitUntilNoActivity();
        assertTrue(job.getLastSuccessfulBuild() == null);

        // Cycle node offline then back online
        slave.toComputer().setTemporarilyOffline(true, null);
        slave.toComputer().setTemporarilyOffline(false, null);
        waitUntilNoActivity();
        assertTrue(job.getLastSuccessfulBuild().number == 1);
    }

    public void testFolderJob() throws Exception {
        // Create one level folder and a job with startup trigger
        Folder folder = this.hudson.createProject(Folder.class, "folder1");
        FreeStyleProject job = folder.createProject(FreeStyleProject.class, "job");
        job.addTrigger(new HudsonStartupTrigger("slave0", null, null, null));

        // Create slave which node name will be slave0
        createOnlineSlave();

        // Wait for the completion of the build
        waitUntilNoActivity();

        assertTrue(job.getLastSuccessfulBuild().number == 1);
    }

    public void testSubFolderJob() throws Exception {
        // Create two level folder and a job with startup trigger
        Folder folder1 = this.hudson.createProject(Folder.class, "folder1");
        Folder folder2 = folder1.createProject(Folder.class, "folder2");

        FreeStyleProject job = folder2.createProject(FreeStyleProject.class, "job");
        job.addTrigger(new HudsonStartupTrigger("slave0", null, null, null));


        // Create slave which node name will be slave0
        createOnlineSlave();

        // Wait for the completion of the build
        waitUntilNoActivity();

        assertTrue(job.getLastSuccessfulBuild().number == 1);
    }
}

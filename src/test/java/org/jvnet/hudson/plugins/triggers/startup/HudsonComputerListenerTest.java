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
import hudson.model.Computer;
import hudson.model.FreeStyleProject;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@WithJenkins
class HudsonComputerListenerTest {

    @Test
    void testRootJobConnect(JenkinsRule j) throws Exception {
        // Create job with startup trigger
        FreeStyleProject job = j.createProject(FreeStyleProject.class, "job");
        job.addTrigger(new HudsonStartupTrigger("slave0", null, null, "ON_CONNECT"));

        // Create agent which node name will be slave0
        j.createOnlineSlave();

        // Wait for the completion of the build
        j.waitUntilNoActivity();

        assertEquals(1, job.getLastSuccessfulBuild().number);
    }

    @Test
    void testRootWorkflowConnect(JenkinsRule j) throws Exception {
        // Create Workflow job with startup trigger
        WorkflowJob job = j.jenkins.createProject(WorkflowJob.class, "job");
        job.setDefinition(new CpsFlowDefinition("echo 'Hello World'", true));
        job.addTrigger(new HudsonStartupTrigger("slave0", null, null, "ON_CONNECT"));

        // Create agent which node name will be slave0
        j.createOnlineSlave();

        // Wait for the completion of the build
        j.waitUntilNoActivity();
        assertEquals(1, job.getLastSuccessfulBuild().number);
    }

    @Test
    void testRootJobDisabled(JenkinsRule j) throws Exception {
        // Create job with startup trigger
        FreeStyleProject job = j.createProject(FreeStyleProject.class, "job");
        job.addTrigger(new HudsonStartupTrigger("slave0", null, null, "ON_CONNECT"));
        job.disable();

        // Create agent which node name will be slave0
        j.createOnlineSlave();

        // Wait for the completion of the build
        j.waitUntilNoActivity();

        assertNull(job.getLastSuccessfulBuild());
    }

    @Test
    void testRootJobOnline(JenkinsRule j) throws Exception {
        // Create job with startup trigger
        FreeStyleProject job = j.createProject(FreeStyleProject.class, "job");
        job.addTrigger(new HudsonStartupTrigger("slave0", null, null, "ON_ONLINE"));

        // Create agent which node name will be slave0
        Computer computer = Objects.requireNonNull(j.createOnlineSlave().toComputer(), "Slave must be non-null");

        // Wait for the completion of the build (there should be none)
        j.waitUntilNoActivity();
        assert (job.getLastSuccessfulBuild() == null);

        // Cycle node offline then back online
        computer.setTemporarilyOffline(true, null);
        computer.setTemporarilyOffline(false, null);
        j.waitUntilNoActivity();
        assertEquals(1, job.getLastSuccessfulBuild().number);
    }

    @Test
    void testFolderJob(JenkinsRule j) throws Exception {
        // Create one level folder and a job with startup trigger
        Folder folder = j.createProject(Folder.class, "folder1");
        FreeStyleProject job = folder.createProject(FreeStyleProject.class, "job");
        job.addTrigger(new HudsonStartupTrigger("slave0", null, null, null));

        // Create agent which node name will be slave0
        j.createOnlineSlave();

        // Wait for the completion of the build
        j.waitUntilNoActivity();

        assertEquals(1, job.getLastSuccessfulBuild().number);
    }

    @Test
    void testSubFolderJob(JenkinsRule j) throws Exception {
        // Create two level folder and a job with startup trigger
        Folder folder1 = j.createProject(Folder.class, "folder1");
        Folder folder2 = folder1.createProject(Folder.class, "folder2");

        FreeStyleProject job = folder2.createProject(FreeStyleProject.class, "job");
        job.addTrigger(new HudsonStartupTrigger("slave0", null, null, null));


        // Create agent which node name will be slave0
        j.createOnlineSlave();

        // Wait for the completion of the build
        j.waitUntilNoActivity();

        assertEquals(1, job.getLastSuccessfulBuild().number);
    }
}

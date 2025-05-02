/*
 * The MIT License
 *
 * Copyright (c) 2016, SAP SE
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
package org.jvnet.hudson.plugins.triggers.startup;

import hudson.model.FreeStyleProject;
import hudson.model.Label;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WithJenkins
class LabelExpressionTest {

    @Test
    void testBasicExpression(JenkinsRule j) throws Exception {
        // Create job with startup trigger
        FreeStyleProject job = j.createFreeStyleProject("job");
        job.addTrigger(new HudsonStartupTrigger("slave0 && DUMMY", null, null, null));

        // Create agent which node name will be slave0
        j.createOnlineSlave(Label.get("DUMMY"));

        // Wait for the completion of the build
        j.waitUntilNoActivity();

        assertEquals(1, job.getLastSuccessfulBuild().number);
    }

    @Test
    void testMultiLabels(JenkinsRule j) throws Exception {
        // Create job with startup trigger
        FreeStyleProject job = j.createFreeStyleProject("job");
        job.addTrigger(new HudsonStartupTrigger("slave0 DUMMY", null, null, null));

        // Create agent which node name will be slave0
        j.createOnlineSlave(Label.get("DUMMY"));

        // Wait for the completion of the build
        j.waitUntilNoActivity();

        assertEquals(1, job.getLastSuccessfulBuild().number);
    }
}
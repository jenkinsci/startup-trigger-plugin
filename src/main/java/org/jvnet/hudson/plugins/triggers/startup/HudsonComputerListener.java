/**
 * The MIT License
 * Copyright (c) 2015 Ash Lux, Gregory Boissinot and all contributors
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jvnet.hudson.plugins.triggers.startup;

import hudson.Extension;
import hudson.model.*;
import hudson.slaves.ComputerListener;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * @author Gregory Boissinot
 */
@Extension
public class HudsonComputerListener extends ComputerListener implements Serializable {

    @Override
    public void onOnline(Computer c, TaskListener listener) throws IOException, InterruptedException {
        Node node = c.getNode();
        if (node != null) {
            listener.getLogger().println("[StartupTrigger] - Scanning jobs for node " + getNodeName(node));
            List<TopLevelItem> items = Hudson.getInstance().getItems();
            for (TopLevelItem item : items) {
                processAndScheduleIfNeeded(item, c, listener);
            }
        }
    }

    private String getNodeName(Node node) {
        String nodeName = node.getNodeName();
        if ("".equals(nodeName)) {
            return "master";
        }
        return nodeName;
    }

    private void processAndScheduleIfNeeded(TopLevelItem item, Computer c, TaskListener listener) {

        if (!(item instanceof AbstractProject)) {
            return;
        }
        AbstractProject<?, ?> project = (AbstractProject) item;

        HudsonStartupTrigger startupTrigger = project.getTrigger(HudsonStartupTrigger.class);
        if (startupTrigger == null) {
            return;
        }

        Node node = c.getNode();
        if (node == null) {
            return;
        }

        HudsonStartupService startupService = new HudsonStartupService();
        if (startupService.has2Schedule(startupTrigger, node)) {
            listener.getLogger().print("[StartupTrigger] - Scheduling " + project.getName());
            project.scheduleBuild(startupTrigger.getQuietPeriod(), new HudsonStartupCause(node));
        }
    }

}

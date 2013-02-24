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
            project.scheduleBuild(startupTrigger.getQuietPeriod(), new HudsonStartupCause());
        }
    }

}

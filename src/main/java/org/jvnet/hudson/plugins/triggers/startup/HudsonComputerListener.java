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

        listener.getLogger().print("[StartupTrigger[] - Scanning jobs for slave " + c.getName());
        List<TopLevelItem> items = Hudson.getInstance().getItems();
        for (TopLevelItem item : items) {
            if (item instanceof AbstractProject) {
                AbstractProject project = (AbstractProject) item;
                HudsonStartupTrigger startupTrigger = (HudsonStartupTrigger) project.getTrigger(HudsonStartupTrigger.class);
                if (startupTrigger != null) {
                    String triggerLabel = startupTrigger.getLabel();
                    Node node = c.getNode();
                    if (node != null) {
                        String nodeLabel = node.getLabelString();
                        if (nodeLabel != null) {

                            if (triggerLabel == null) {
                                if (nodeLabel == "") {
                                    listener.getLogger().print("[StartupTrigger[] - Scheduling " + project.getName());
                                    project.scheduleBuild(0, new HudsonStartupCause());
                                    continue;
                                }
                            }

                            //Check node label (multiple node can have the same label)
                            if (triggerLabel.equalsIgnoreCase(nodeLabel)) {
                                listener.getLogger().print("[StartupTrigger[] - Scheduling " + project.getName());
                                project.scheduleBuild(0, new HudsonStartupCause());
                                continue;
                            }

                            //Check node name
                            if (triggerLabel.equalsIgnoreCase(node.getNodeName())) {
                                listener.getLogger().print("[StartupTrigger[] - Scheduling " + project.getName());
                                project.scheduleBuild(0, new HudsonStartupCause());
                                continue;
                            }

                        }

                    }
                }
            }
        }


    }
}



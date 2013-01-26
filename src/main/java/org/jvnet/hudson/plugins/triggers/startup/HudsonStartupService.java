package org.jvnet.hudson.plugins.triggers.startup;

import hudson.model.Node;

import java.io.Serializable;

/**
 * @author Gregory Boissinot
 */
public class HudsonStartupService implements Serializable {

    public boolean has2Schedule(HudsonStartupTrigger startupTrigger, Node jobNode) {

        if (startupTrigger == null) {
            throw new NullPointerException("A startupTrigger object has to be set.");
        }
        if (jobNode == null) {
            throw new NullPointerException("A node object has to be set.");
        }

        String triggerLabel = startupTrigger.getLabel();

        return has2Schedule(triggerLabel, jobNode);
    }

    private boolean has2Schedule(String triggerLabel, Node jobNode) {

        String jobNodeName = jobNode.getNodeName();

        if (triggerLabel == null) { //Jobs on master has to schedule
            return isMaster(jobNodeName);
        }

        if (triggerLabel.equalsIgnoreCase("master")) { //User set 'master' string, Jobs on master has to schedule
            return isMaster(jobNodeName);
        }

        if (triggerLabel.equalsIgnoreCase(jobNodeName)) { //Match exactly node name
            return true;
        }

        return triggerLabel.equalsIgnoreCase(jobNode.getLabelString());  //Match node label
    }

    private boolean isMaster(String nodeName) {
        //Master node name is "", slave node name is never empty
        return nodeName.equals("");
    }

}

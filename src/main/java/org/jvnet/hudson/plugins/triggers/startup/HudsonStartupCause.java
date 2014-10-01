package org.jvnet.hudson.plugins.triggers.startup;

import hudson.model.Cause;

public class HudsonStartupCause extends Cause {

    @Override
    public String getShortDescription(Node node) {
        return "Started due to the start of the node " + node.getDisplayName();
    }
}

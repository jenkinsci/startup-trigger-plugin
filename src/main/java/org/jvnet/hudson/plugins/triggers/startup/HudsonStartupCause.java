package org.jvnet.hudson.plugins.triggers.startup;

import hudson.model.Cause;

public class HudsonStartupCause extends Cause {

    Node node;

    public HudsonStartupCause(Node node)
    {
        this.node = node
    }

    @Override
    public String getShortDescription() {
        return "Started due to the start of the node " + node.getDisplayName();
    }
}

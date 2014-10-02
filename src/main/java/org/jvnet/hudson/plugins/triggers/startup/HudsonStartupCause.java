package org.jvnet.hudson.plugins.triggers.startup;

import hudson.model.Cause;
import hudson.model.Node;

public class HudsonStartupCause extends Cause {

    private Node node;

    public HudsonStartupCause(Node node) {
        this.node = node;
    }

    @Override
    public String getShortDescription() {
        return "Started due to the start of the node " + node.getDisplayName();
    }
}

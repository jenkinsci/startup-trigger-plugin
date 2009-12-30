package org.jvnet.hudson.plugins.triggers.startup;

import hudson.model.Cause;

public class HudsonStartupCause
    extends Cause
{
    @Override
    public String getShortDescription()
    {
        return "Started due to Hudson startup.";
    }
}

package org.jvnet.hudson.plugins.triggers.startup;

import hudson.model.Item;
import hudson.triggers.TriggerDescriptor;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

public class HudsonStartupDescriptor
    extends TriggerDescriptor

{
    protected HudsonStartupDescriptor() {
        super(HudsonStartupTrigger.class);
    }

    @Override
    public boolean isApplicable( Item item )
    {
        return true;
    }

    @Override
    public String getDisplayName()
    {
        return "Build when Hudson first starts";
    }

    @Override
    public HudsonStartupTrigger newInstance( StaplerRequest req, JSONObject formData )
        throws FormException
    {
        return new HudsonStartupTrigger();
    }
}

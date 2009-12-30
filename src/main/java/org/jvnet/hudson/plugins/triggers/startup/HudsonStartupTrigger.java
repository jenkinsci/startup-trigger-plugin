package org.jvnet.hudson.plugins.triggers.startup;

import hudson.Extension;
import hudson.model.BuildableItem;
import hudson.triggers.Trigger;

/**
 * Triggers a build when Hudson first starts
 *
 * @author Ash Lux
 */
public class HudsonStartupTrigger
    extends Trigger<BuildableItem>
{
    @Extension
    public static final HudsonStartupDescriptor DESCRIPTOR = new HudsonStartupDescriptor();

    @Override
    public void start( BuildableItem project, boolean newInstance )
    {
        super.start( project, newInstance );

        // do not schedule build when trigger was just added to the job
        if ( !newInstance )
        {
            project.scheduleBuild( new HudsonStartupCause() );
        }
    }
}

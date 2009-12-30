package org.jvnet.hudson.plugins.triggers.startup;

import hudson.model.BuildableItem;
import hudson.model.Cause;
import org.junit.Test;
import org.mockito.Matchers;

import static org.mockito.Mockito.*;

public class HudsonStartupTriggerTest
{
    @Test
    public void testStart_shouldNotScheduleBuildWhenTriggerAdded()
    {
        BuildableItem project = mock( BuildableItem.class );

        HudsonStartupTrigger hudsonStartupTrigger = new HudsonStartupTrigger();
        hudsonStartupTrigger.start( project, true );

        verify( project, never() ).scheduleBuild( Matchers.<Cause>any() );
    }

    @Test
    public void testStart_shouldScheduleBuildWhenHudsonStarts()
    {
        BuildableItem project = mock( BuildableItem.class );

        HudsonStartupTrigger hudsonStartupTrigger = new HudsonStartupTrigger();
        hudsonStartupTrigger.start( project, false );

        verify( project, only() ).scheduleBuild( isA( HudsonStartupCause.class ) );
    }
}

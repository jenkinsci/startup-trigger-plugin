/**
 * The MIT License
 * Copyright (c) 2015 Ash Lux, Gregory Boissinot and all contributors
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jvnet.hudson.plugins.triggers.startup;

import hudson.Extension;
import hudson.model.*;
import hudson.slaves.ComputerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jvnet.jenkins.plugins.nodelabelparameter.NodeParameterValue;

/**
 * @author Gregory Boissinot
 */
@Extension
public class HudsonComputerListener extends ComputerListener {

    private final HudsonStartupService startupService = new HudsonStartupService();

    @Override
    public void onTemporarilyOnline(Computer c) {
        Node node = c.getNode();
        if (node != null) {
            List<AbstractProject> jobs = Hudson.getInstance().getAllItems(AbstractProject.class);
            for (AbstractProject job : jobs) {
                HudsonStartupTrigger startupTrigger = (HudsonStartupTrigger) job.getTrigger(HudsonStartupTrigger.class);
                if (!startupTrigger.getRunOnChoice().equals("ON_CONNECT")) {
                    processAndScheduleIfNeeded(job, c, null, startupTrigger);
                }
            }
        }
    }

    @Override
    public void onOnline(Computer c, TaskListener listener) throws IOException, InterruptedException {
        Node node = c.getNode();
        if (node != null) {
            listener.getLogger().println("[StartupTrigger] - Scanning jobs for node " + getNodeName(node));
            List<AbstractProject> jobs = Hudson.getInstance().getAllItems(AbstractProject.class);
            for (AbstractProject job : jobs) {
                HudsonStartupTrigger startupTrigger = (HudsonStartupTrigger) job.getTrigger(HudsonStartupTrigger.class);
                if (!startupTrigger.getRunOnChoice().equals("ON_ONLINE")) {
                    processAndScheduleIfNeeded(job, c, listener, startupTrigger);
                }
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

    private static ParametersAction getDefaultParameters(AbstractProject project) {
        ParametersDefinitionProperty property = (ParametersDefinitionProperty) project.getProperty(ParametersDefinitionProperty.class);

        if (property == null) {
            return new ParametersAction();
        }

        List<ParameterValue> parameters = new ArrayList<ParameterValue>();
        for (ParameterDefinition pd : property.getParameterDefinitions()) {
            ParameterValue param = pd.getDefaultParameterValue();
            if (param != null) {
                parameters.add(param);
            }
        }

        return new ParametersAction(parameters);
    }

    private void processAndScheduleIfNeeded(AbstractProject project, Computer c, TaskListener listener, HudsonStartupTrigger startupTrigger) {
        if (startupTrigger == null) {
            return;
        }

        Node node = c.getNode();
        if (node == null) {
            return;
        }

        if (startupService.has2Schedule(startupTrigger, node) && !project.isDisabled() ) {
            if (listener != null) {
                listener.getLogger().println("[StartupTrigger] - Scheduling " + project.getName());
            }

            ParametersAction scheduleParameters = getDefaultParameters(project);
            if(startupTrigger.getNodeParameterName() != null) {
                ParameterValue nodeNameParameter = new NodeParameterValue(startupTrigger.getNodeParameterName(), "", node.getNodeName());
                scheduleParameters = scheduleParameters.merge(new ParametersAction(nodeNameParameter));
            }

            project.scheduleBuild(startupTrigger.getQuietPeriod(), new HudsonStartupCause(node), scheduleParameters);
        }
    }

}

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

        String labelString = jobNode.getLabelString();
        if (labelString == null) {
            return false;
        }

        if (triggerLabel.equalsIgnoreCase(labelString)) { //Match node label
            return true;
        }

        return labelString.contains(triggerLabel);
    }

    private boolean isMaster(String nodeName) {
        //Master node name is "", slave node name is never empty
        return nodeName.equals("");
    }

}

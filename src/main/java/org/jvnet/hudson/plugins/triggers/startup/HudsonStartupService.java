/*
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

import hudson.model.Label;
import hudson.model.Node;
import jenkins.model.Jenkins;

/**
 * @author Gregory Boissinot
 */
public class HudsonStartupService {

    public boolean has2Schedule(HudsonStartupTrigger startupTrigger, Node jobNode) {

        if (startupTrigger == null) {
            throw new NullPointerException("A startupTrigger object has to be set.");
        }
        if (jobNode == null) {
            throw new NullPointerException("A node object has to be set.");
        }

        String triggerLabelString = startupTrigger.getLabel();

        return has2Schedule(triggerLabelString, jobNode);
    }

    private boolean has2Schedule(String triggerLabelListString, Node jobNode) {

        if (triggerLabelListString == null) { //Jobs on master has to schedule
            return isMaster(jobNode.getNodeName());
        }

        Jenkins jenkins = Jenkins.get();

        String[] triggerLabelList = triggerLabelListString.split("[ ]|[, ]|[,]");
        for (String triggerLabelString : triggerLabelList) {

            Label triggerLabel = jenkins.getLabel(triggerLabelString);
            if (triggerLabel != null && triggerLabel.contains(jobNode)) {
                return true;
            }
        }

        return false;
    }

    private boolean isMaster(String nodeName) {
        //Master node name is "", slave node name is never empty
        return nodeName.equals("");
    }

}

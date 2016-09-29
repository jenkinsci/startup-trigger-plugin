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

import antlr.ANTLRException;
import hudson.Extension;
import hudson.Util;
import hudson.model.BuildableItem;
import hudson.model.Item;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;
import static org.apache.commons.lang.BooleanUtils.toBooleanDefaultIfNull;


/**
 * Enables the current job to be restarted when Jenkins nodes start
 * (The user is able to set a label to restrict some Jenkins nodes).
 * Without any specified label, the job is restarted when the master Jenkins
 * instances starts
 *
 * @author Ash Lux
 * @author Gregory Boissinot
 */
public class HudsonStartupTrigger extends Trigger<BuildableItem> {

    private String label;

    private int quietPeriod;

    private String nodeParameterName;

    private Boolean runOnOnline = false;

    private Boolean runOnConnect = true;

    @DataBoundConstructor
    public HudsonStartupTrigger(String label, String quietPeriod, String nodeParameterName, Boolean runOnConnect, Boolean runOnOnline) throws ANTLRException {
        super();
        this.label = Util.fixEmpty(label);
        String givenQuietPeriod = Util.fixEmpty(quietPeriod);
        if (givenQuietPeriod == null) {
            this.quietPeriod = 0;
        } else {
            this.quietPeriod = Integer.parseInt(quietPeriod);
        }
        this.nodeParameterName = Util.fixEmpty(nodeParameterName);
        this.runOnConnect = toBooleanDefaultIfNull(runOnConnect, true);
        this.runOnOnline = toBooleanDefaultIfNull(runOnOnline, false);
    }

    public String getLabel() {
        return label;
    }

    public int getQuietPeriod() {
        return quietPeriod;
    }

    public String getNodeParameterName() {
        return nodeParameterName;
    }

    public Boolean getRunOnOnline() { return runOnOnline; }

    public Boolean getRunOnConnect() { return runOnConnect; }

    @Override
    public void start(BuildableItem project, boolean newInstance) {
        //DO NOTHING HERE. RELIES ON EXTERNAL LISTENER
    }

    @Extension
    public static class HudsonStartupDescriptor extends TriggerDescriptor {

        @Override
        public boolean isApplicable(Item item) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Build when job nodes start";
        }
    }
}

# Startup Trigger Plugin
The [Startup Trigger](https://wiki.jenkins-ci.org/display/JENKINS/Startup+Trigger) plugin allows you to trigger a build when Jenkins nodes (master/slave) start.
## Configuration
This plugin requires a jenkins restart (it will not work using install without restart). After installation you will find a new Build Trigger "Build when job nodes start" tickbox is available in the job configuration of each job.
### Restricted node Label
By default this value is blank and the job will start whenever the Jenkins master starts.  You can specify a node or a space separated list of nodes like:
```
LABEL_A LABEL_B
```
Or a label expression:
```
LABEL_A && LABEL_B
```
This plugin will trigger builds when any node matching this/these labels is started.
### Quiet period
Default to zero, allows you to specify the [Quiet Period](https://jenkins.io/blog/2010/08/11/quiet-period-feature/) before scheduling the job.
## Advanced Configuration
### Node parameter name
The parameter name for Startup Trigger to use when specifying which node to run on.  Useful if the default NodeLabelParameter value is a label and you want to run the job on the node that triggered the job's execution.
### Trigger build on:
This pulldown provides a few options for what types of startup events should trigger builds:
  - Run on initial connection (Default)
    - This will trigger when a node first connects to Jenkins (JNLP, SSH, DCOM, etc..)
  - Run when node brought online
    - This will trigger when a node is "Temporarily Reconnected".  For example: Node was previously "Marked as offline" and has now been brought online.
  - Both
    - Use this value to specify that both events should trigger build execution.

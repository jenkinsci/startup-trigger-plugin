# Startup Trigger Plugin maintained by [@ejpenney](https://www.github.com/ejpenney)

[![Build Status][build-status]](https://ci.jenkins.io/job/Plugins/job/startup-trigger-plugin/job/master/)
[![Contributors][contributors]](https://github.com/jenkinsci/startup-trigger-plugin/graphs/contributors)
[![Jenkins Plugin][jenkins-plugin-shield]][jenkins-plugin]
[![GitHub release][github-release]](https://github.com/jenkinsci/startup-trigger-plugin/releases/latest)
[![Jenkins Plugin Installs][installs]][jenkins-plugin]
[![License][license-shield]](LICENSE.md)
[![Project Maintenance][maintenance-shield]](https://github.com/jenkinsci/startup-trigger-plugin/commits/master)
[![GitHub Activity][commits-shield]](https://github.com/jenkinsci/startup-trigger-plugin/commits/master)

The [Startup Trigger](https://plugins.jenkins.io/startup-trigger-plugin) plugin allows you to trigger a build when Jenkins nodes (master/slave) start.

## Support

Hey dude! Help me out for a couple of :beers: or a :coffee:!

[![coffee](https://www.buymeacoffee.com/assets/img/custom_images/black_img.png)](https://www.buymeacoffee.com/ejpenney)

## Configuration

This plugin requires a jenkins restart (it will not work using install without restart). After installation you will find a new Build Trigger "Build when job nodes start" tickbox is available in the job configuration of each job.

### Restricted node Label

By default this value is blank and the job will start (once) whenever the Jenkins master starts.  Alternatively, you can specify a node label or a space separated list of nodes labels like:

    LABEL_A LABEL_B

Or a label expression:

    LABEL_A && LABEL_B

This plugin will trigger builds when any node matching this/these labels is started.  This includes when the master is restarted, as each matching node reconnects the job will be re-executed.

### Quiet period

Defaults to zero, allows you to specify the [Quiet Period](https://jenkins.io/blog/2010/08/11/quiet-period-feature/) before scheduling the job.

## Advanced Configuration

### Node parameter name

The parameter name for Startup Trigger to use when specifying which node triggered the job.  Useful if the default NodeLabelParameter value is a label and you want to run the job on the node that triggered the job's execution.

This can also reference a StringParameter, if instead you want the name of the started node to be handed to a job as an argument.

### Trigger build on

This pulldown provides a few options for what types of startup events should trigger builds:

- Run on initial connection (Default)
  - This will trigger when a node first connects to Jenkins (JNLP, SSH, DCOM, etc..)
- Run when node brought online
  - This will trigger when a node is "Temporarily Reconnected".  For example: Node was previously "Marked as offline" and has now been brought online.
- Both
  - Use this value to specify that both events should trigger build execution

## Development Environment

This plugin seems to build successfully in all versions of Maven.  Previously releases only worked with 3.3.9, but this is no longer supported. Next release (2.9.4) will be attempted with 3.8.2.  Startup-Trigger does not build with JDK-16, but does work with [JDK-15](https://download.java.net/java/GA/jdk15.0.2/0d1cfde4252546c6931946de8db48ee2/7/GPL/openjdk-15.0.2_windows-x64_bin.zip).

[build-status]: https://ci.jenkins.io/job/Plugins/job/startup-trigger-plugin/job/master/badge/icon
[contributors]: https://img.shields.io/github/contributors/jenkinsci/startup-trigger-plugin.svg
[jenkins-plugin-shield]: https://img.shields.io/jenkins/plugin/v/startup-trigger-plugin.svg
[jenkins-plugin]: https://plugins.jenkins.io/startup-trigger-plugin
[github-release]: https://img.shields.io/github/release/jenkinsci/startup-trigger-plugin.svg
[installs]: https://img.shields.io/jenkins/plugin/i/startup-trigger-plugin.svg?color=blue
[commits-shield]: https://img.shields.io/github/commit-activity/y/jenkinsci/startup-trigger-plugin.svg
[license-shield]: https://img.shields.io/github/license/jenkinsci/startup-trigger-plugin.svg
[maintenance-shield]: https://img.shields.io/maintenance/yes/2021.svg

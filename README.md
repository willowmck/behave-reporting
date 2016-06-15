[![Build Status](https://img.shields.io/travis/willowmck/behave-reporting.svg?label=Travis%20bulid)](https://travis-ci.org/willowmck/behave-reporting)
[![Build Status](https://img.shields.io/appveyor/ci/willowmck/behave-reporting/master.svg?label=AppVeyor%20build)](https://ci.appveyor.com/project/willowmck/behave-reporting/history)

[![Coverage Status](https://img.shields.io/codecov/c/github/willowmck/behave-reporting/master.svg?label=Unit%20tests%20coverage)](https://codecov.io/github/willowmck/behave-reporting)
[![SonarQube coverage](https://img.shields.io/sonar/http/nemo.sonarqube.org/org.orangesoft:behave-reporting/coverage.svg?label=All%20tests%20coverage)](http://nemo.sonarqube.org/overview/coverage?id=org.orangesoft%3Abehave-reporting)
[![SonarQube tech debt](https://img.shields.io/sonar/http/nemo.sonarqube.org/org.orangesoft:behave-reporting/tech_debt.svg?label=Sonarqube%20tech%20debt)](http://nemo.sonarqube.org/overview/debt?id=org.orangesoft%3Abehave-reporting)
[![Coverity](https://scan.coverity.com/projects/6166/badge.svg?label=Coverity%20analysis)](https://scan.coverity.com/projects/willowmck-behave-reporting)
[![Codacy](https://api.codacy.com/project/badge/grade/7f206992ed364f0896490057fdbdaa2e)](https://www.codacy.com/app/willowmck/behave-reporting)
[![Maven Dependencies](https://www.versioneye.com/user/projects/55c5301d653762001a0035ed/badge.svg)](https://www.versioneye.com/user/projects/55c5301d653762001a0035ed?child=summary)

[![Maven Central](https://img.shields.io/maven-central/v/org.orangesoft/behave-reporting.svg)](http://search.maven.org/#search|gav|1|g%3A%22org.orangesoft%22%20AND%20a%3A%22behave-reporting%22)
[![License](https://img.shields.io/badge/license-GNU%20LGPL%20v2.1-blue.svg)](https://raw.githubusercontent.com/willowmck/behave-reporting/master/LICENCE)

# Publish pretty [behave](http://pythonhosted.org/behave/) reports

This is a Java report publisher primarily created to publish behave reports on the Jenkins build server. It publishes pretty html reports showing the results of behave runs. It has been split out into a standalone package so it can be used for Jenkins and maven command line as well as any other packaging that might be useful.

## Background

Behave is a test automation tool following the principles of Behavioural Driven Design and living documentation. Specifications are written in a concise human readable form and executed in continuous integration.

This project allows you to publish the results of a behave run as pretty html reports. In order for this to work you must generate a behave json report. The project converts the json report into an overview html linking to separate feature file htmls with stats and results.

## Install

Add a maven dependency to your pom
```xml
<dependency>
    <groupId>org.orangesoft</groupId>
    <artifactId>behave-reporting</artifactId>
    <version>(check version above)</version>
</dependency>
```

Read this if you need further [detailed install and configuration]
(https://github.com/willowmck/behave-reports-plugin/wiki/Detailed-Configuration) instructions for using the Jenkins version of this project

## Usage
```java
File reportOutputDirectory = new File("target");
List<String> jsonFiles = new ArrayList<>();
jsonFiles.add("behave-report-1.json");
jsonFiles.add("behave-report-2.json");

String jenkinsBasePath = "";
String buildNumber = "1";
String projectName = "behave";
boolean skippedFails = true;
boolean pendingFails = false;
boolean undefinedFails = true;
boolean missingFails = true;
boolean runWithJenkins = false;
boolean parallelTesting = false;

Configuration configuration = new Configuration(reportOutputDirectory, projectName);
// optionally only if you need
configuration.setStatusFlags(skippedFails, pendingFails, undefinedFails, missingFails);
configuration.setParallelTesting(parallelTesting);
configuration.setJenkinsBasePath(jenkinsBasePath);
configuration.setRunWithJenkins(runWithJenkins);
configuration.setBuildNumber(buildNumber);

ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
reportBuilder.generateReports();
```
`skippedFails` means the build will be failed if any steps are in skipped status and similar happens for other flags. This only applies when running with Jenkins.
runWithJenkins means put in the links back to Jenkins in the report.

There is a feature overview page:

![feature overview page]
(https://github.com/willowmck/behave-reporting/raw/master/.README/feature-overview.png)

And there are also feature specific results pages:

![feature specific page passing]
(https://github.com/willowmck/behave-reporting/raw/master/.README/feature-passed.png)

And useful information for failures:

![feature specific page passing]
(https://github.com/willowmck/behave-reporting/raw/master/.README/feature-failed.png)

If you have tags in your behave features you can see a tag overview:

![Tag overview]
(https://github.com/willowmck/behave-reporting/raw/master/.README/tag-overview.png)

And you can drill down into tag specific reports:

![Tag report]
(https://github.com/willowmck/behave-reporting/raw/master/.README/tag-report.png)


## Code quality

Once you developed your new feature or improvement you should test it by providing several unit or integration tests.

![codecov.io](https://codecov.io/gh/willowmck/behave-reporting/branch/master/graphs/tree.svg)


## Contribution

Interested in contributing to the behave-reporting?  Great!  Start [here]
(https://github.com/willowmck/behave-reporting).

## Micronaut Layered Images Proof-Of-Concept

This repository demonstrates building layered images with Micronaut.

# Prerequisites

1. A GraalVM build with the [Micronaut Hack patch applied, PR 18329](pull-requests/18329/overview)
2. JDK 21 installed

# Prepare the SDK repository

The custom Micronaut build will use a temporary repository containing the snapshot GraalVM SDK.
To generate this repository, do the following:

```bash
cd graal/sdk
mx build
mx maven-deploy --licenses UPL,BSD-new,GPLv2-CPE foo file:///tmp/repo
```

This will create a Maven repository with the artifacts under `/tmp/repo`.

# Deploy a custom Micronaut build

1. `git clone git@github.com:micronaut-projects/micronaut-core.git`
2. `cd micronaut-core`
3. `git checkout cc/layered-images-4.5`
4. `export JAVA_HOME=/path/to/your/java-21` and `export GRAALVM_HOME=/path/to/your/patched/graalvm-jdk`
5. `./gradlew pTML`

The last step will build Micronaut 4.5.3 with a patch which allows running with layered images and publish the artifacts in the local Maven repository.
That custom version is named `4.5.3-li`.

# Run the build

Now everything should be available for this project to run.
Make sure to:

1. `export JAVA_HOME=/path/to/your/java-21`
2. `export GRAALVM_HOME=/path/to/your/patched/graalvm-jdk`

Then you should be able to run the application _without_ layered images support by running:

`./gradlew nativeRun`

And _with_ layered images support by running:

`./gradlew nativeCompile --use-layers nativeRun`

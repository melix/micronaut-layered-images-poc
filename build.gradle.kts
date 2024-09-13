plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.4.2"
    id("io.micronaut.aot") version "4.4.2"
}

version = "0.1"
group = "micronaut.layered.images"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    runtimeOnly("org.slf4j:slf4j-simple")
}

configurations.all {
    resolutionStrategy {
        dependencySubstitution.all {
            if (requested is ModuleComponentSelector) {
                val mcs = requested as ModuleComponentSelector
                if (!mcs.module.contains("bom")) {
                    if (mcs.group == "io.micronaut") {
                        // Use custom Micronaut Core build (with patch for layered images)
                        useTarget("${mcs.group}:${mcs.module}:4.5.3-li")
                    }
                }
            }
        }
    }
}


application {
    mainClass = "app.layer.test.Application"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

graalvmNative.toolchainDetection = false

micronaut {
    coreVersion = "2.5.3-li"
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("app.layer.test.*")
    }
    aot {
        // Please review carefully the optimizations enabled below
        // Check https://micronaut-projects.github.io/micronaut-aot/latest/guide/ for more details
        optimizeServiceLoading = false
        convertYamlToJava = false
        precomputeOperations = true
        cacheEnvironment = true
        optimizeClassLoading = true
        deduceEnvironment = true
        optimizeNetty = true
        replaceLogbackXml = true
    }
}


graalvmNative.binaries.all {
    buildArgs.add("-J-Xmx16g")
    buildArgs.addAll("-J-ea", "-J-esa")
    buildArgs.add("--initialize-at-run-time=io.netty")
    buildArgs.addAll(listOf("--add-exports", "org.graalvm.nativeimage.builder/com.oracle.svm.core.layeredimagesingleton=ALL-UNNAMED"))
    buildArgs.addAll(listOf("--add-exports", "org.graalvm.nativeimage.builder/com.oracle.svm.core.imagelayer=ALL-UNNAMED"))
}

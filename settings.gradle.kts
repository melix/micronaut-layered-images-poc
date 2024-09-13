import me.champeau.gradle.igp.gitRepositories

pluginManagement {
    plugins {
        id("org.graalvm.buildtools.native")
    }
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("me.champeau.includegit") version "0.1.6"
}

rootProject.name="micronaut-layered-images"

gitRepositories {
    include("native-build-tools") {
        uri = "https://github.com/graalvm/native-build-tools.git"
        branch = "cc/layered-images"
    }
}

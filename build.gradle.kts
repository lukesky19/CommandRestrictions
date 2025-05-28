plugins {
    java
    id("com.gradleup.shadow") version "8.3.0"
}

group = "com.github.lukesky19"
version = "1.0.0.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven("https://raw.githubusercontent.com/TheBlackEntity/PlugMan/repository/") {
        name = "PlugmanX"
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
    compileOnly("com.github.lukesky19:SkyLib:1.2.0.0")
    compileOnly("com.rylinaux:PlugMan:2.3.3")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

tasks.jar {
    manifest {
        attributes["paperweight-mappings-namespace"] = "mojang"
    }

    archiveClassifier.set("")
}

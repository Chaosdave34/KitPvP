import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
    `java-library`
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.lombok") version "1.9.23"

    id("io.papermc.paperweight.userdev") version "1.6.0"
    id("xyz.jpenilla.run-paper") version "2.2.4" // Adds runServer and runMojangMappedServer tasks for testing
    id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.1.1" // Generates plugin.yml based on the Gradle config

    // Shades and relocates dependencies into our plugin jar. See https://imperceptiblethoughts.com/shadow/introduction/
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.freefair.lombok") version "8.6"
}

group = "io.github.chaosdave34"
version = "1.0.0-SNAPSHOT"
description = "KitPvP Paper Plugin"

java {
    // Configure the java toolchain. This allows gradle to auto-provision JDK 21 on systems that only have JDK 11 installed for example.
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

// 1)
// For 1.20.5+ when you don't care about supporting spigot
paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

// 2)
// For 1.20.4 or below, or when you care about supporting Spigot on 1.20.5+
// Configure reobfJar to run when invoking the build task
/*
tasks.assemble {
  dependsOn(tasks.reobfJar)
}
 */

repositories {
    mavenLocal()

    maven {
        url = uri("https://maven.pkg.github.com/Chaosdave34/GHUtils")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
        }
    }
}

dependencies {
    paperweight.paperDevBundle("1.20.5-R0.1-SNAPSHOT")
    // paperweight.foliaDevBundle("1.20.5-R0.1-SNAPSHOT")
    // paperweight.devBundle("com.example.paperfork", "1.20.5-R0.1-SNAPSHOT")

    implementation("io.github.chaosdave34", "ghutils", "0.3.0-SNAPSHOT")
}

tasks {
    compileJava {
        // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
        // See https://openjdk.java.net/jeps/247 for more information.
        options.release = 21
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
        val props = mapOf(
            "name" to project.name,
            "version" to project.version,
            "description" to project.description,
            "apiVersion" to "1.20"
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    // Only relevant when going with option 2 above
    /*
    reobfJar {
      // This is an example of how you might change the output location for reobfJar. It's recommended not to do this
      // for a variety of reasons, however it's asked frequently enough that an example of how to do it is included here.
      outputJar = layout.buildDirectory.file("libs/PaperweightTestPlugin-${project.version}.jar")
    }
     */

    shadowJar {
        // helper function to relocate a package into our package
        fun relocate(pkg: String) = relocate(pkg, group + rootProject.name + "dependency.$pkg")

        // relocate("io.github.chaosdave34.ghutils")
    }
}

// Configure plugin.yml generation
// - name, version, and description are inherited from the Gradle project.
bukkitPluginYaml {
    name = project.name
    version = project.version.toString()
    main = "io.github.chaosdave34.kitpvp.KitPvp"
    description = project.description
    load = BukkitPluginYaml.PluginLoadOrder.STARTUP
    authors.addAll("Chaosdave34", "palul")
    apiVersion = "1.20"
}
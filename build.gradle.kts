import xyz.jpenilla.resourcefactory.bukkit.Permission

plugins {
    `java-library`
    kotlin("jvm") version "2.0.0"

    id("io.papermc.paperweight.userdev") version "1.7.1"
    id("xyz.jpenilla.run-paper") version "2.3.0" // Adds runServer and runMojangMappedServer tasks for testing
    id("xyz.jpenilla.resource-factory-paper-convention") version "1.1.1" // Generates plugin.yml based on the Gradle config

    // Shades and relocates dependencies into our plugin jar. See https://imperceptiblethoughts.com/shadow/introduction/
    id("io.github.goooler.shadow") version "8.1.7"
}

group = "io.github.chaosdave34"
version = "1.0.0-SNAPSHOT"
description = "KitPvP Paper Plugin"

java {
    // Configure the java toolchain. This allows gradle to auto-provision JDK 21 on systems that only have JDK 11 installed for example.
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

// 1)
// For >=1.20.5 when you don't care about supporting spigot
paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

// 2)
// For 1.20.4 or below, or when you care about supporting Spigot on >=1.20.5
// Configure reobfJar to run when invoking the build task
/*
tasks.assemble {
  dependsOn(tasks.reobfJar)
}
 */

dependencies {
    paperweight.paperDevBundle("1.21-R0.1-SNAPSHOT")
    // paperweight.foliaDevBundle("1.21-R0.1-SNAPSHOT")
    // paperweight.devBundle("com.example.paperfork", "1.21-R0.1-SNAPSHOT")
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    compileJava {
        // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
        // See https://openjdk.java.net/jeps/247 for more information.
        options.release = 21
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
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
        fun reloc(pkg: String) = relocate(pkg, "io.papermc.paperweight.testplugin.dependency.$pkg")
    }
}

// Configure plugin.yml generation
// - name, version, and description are inherited from the Gradle project.
paperPluginYaml {
    main = "io.github.chaosdave34.kitpvp.KitPvp"
    authors.addAll("Chaosdave34", "palul")
    apiVersion = "1.21"
    bootstrapper = "io.github.chaosdave34.kitpvp.PluginBootstrap"

    permissions {
        register("bukkit.command.version") {
            default = Permission.Default.OP
        }
        register("bukkit.command.plugins") {
            default = Permission.Default.OP
        }
        register("bukkit.command.help") {
            default = Permission.Default.OP
        }
        register("minecraft.command.msg") {
            default = Permission.Default.OP
        }
        register("minecraft.command.teammsg") {
            default = Permission.Default.OP
        }
        register("minecraft.command.me") {
            default = Permission.Default.OP
        }
        register("minecraft.command.trigger") {
            default = Permission.Default.OP
        }
        register("minecraft.command.help") {
            default = Permission.Default.OP
        }
    }
}
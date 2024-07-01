import xyz.jpenilla.resourcefactory.bukkit.Permission

plugins {
    `java-library`
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.lombok") version "2.0.0"

    id("io.papermc.paperweight.userdev") version "1.7.1"
    id("xyz.jpenilla.run-paper") version "2.3.0" // Adds runServer and runMojangMappedServer tasks for testing
    id("xyz.jpenilla.resource-factory-paper-convention") version "1.1.1" // Generates plugin.yml based on the Gradle config

    // Shades and relocates dependencies into our plugin jar. See https://imperceptiblethoughts.com/shadow/introduction/
    //id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.github.goooler.shadow") version "8.1.7" // fork for java 21 support

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
    paperweight.paperDevBundle("1.21-R0.1-SNAPSHOT")
    // paperweight.foliaDevBundle("1.21-R0.1-SNAPSHOT")
    // paperweight.devBundle("com.example.paperfork", "1.21-R0.1-SNAPSHOT")
    implementation("io.github.chaosdave34", "ghutils", "0.5.0-SNAPSHOT")
}

tasks {
    assemble {
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

    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
        val props = mapOf(
            "name" to project.name,
            "version" to project.version,
            "description" to project.description,
            "apiVersion" to "1.21"
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

        relocate("io.github.chaosdave34.ghutils")
    }
}

// Configure plugin.yml generation
// - name, version, and description are inherited from the Gradle project.
paperPluginYaml {
    main = "io.github.chaosdave34.kitpvp.KitPvp"
    authors.addAll("Chaosdave34", "palul")
    apiVersion = "1.21"
    bootstrapper = "io.github.chaosdave34.kitpvp.PluginBootstrap"

//    commands {
//        register("spawn") {
//            description = "Teleports you back to the spawn"
//            usage = "/<command>"
//        }
//
//        register("msg") {
//            description = "Send a player a message"
//            usage = "/<command> <player> <message>"
//        }
//
//        register("bounty") {
//            description = "Place a bounty on a player"
//            usage = "/<command> <player> <bounty>"
//        }
//
//        register("loop") {
//            description = "Loop command"
//            usage = "/<command> <amount> <period> <cmd>"
//            permission = "kitpvp.loop"
//        }
//
//        register("customitem") {
//            description = "Give yourself a custom item"
//            usage = "/<command> <item_id> <amount>"
//            permission = "kitpvp.customitem"
//        }
//
//        register("gommemode") {
//            description = "Sets you into gomme mode"
//            usage = "/<command>"
//            permission = "kitpvp.gommemode"
//        }
//
//        register("addexperience") {
//            description = "Adds experience to the player"
//            usage = "/<command> <player> <amount>"
//            permission = "kitpvp.addexperience"
//        }
//
//        register("addcoins") {
//            description = "Adds coins to the player"
//            usage = "/<command> <player> <amount>"
//            permission = "kitpvp.addcoins"
//        }
//    }
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
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotest.multiplatform)
    alias(libs.plugins.dokka)
    alias(libs.plugins.gitSemVer)
    alias(libs.plugins.kotlin.qa)
    alias(libs.plugins.multiJvmTesting)
    alias(libs.plugins.taskTree)

    alias(libs.plugins.graphqlServer)
    alias(libs.plugins.ktor)
    alias(libs.plugins.graphqlClient)

    application
}

repositories {
    google()
    mavenCentral()
}

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(IR) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.apollo.runtime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.bundles.ktor.server)
                implementation(libs.bundles.graphql.server)
                implementation(libs.logback)
            }
        }
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                implementation(libs.apollo.runtime)
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.letsplot.js)
            }
        }
        val jsTest by getting
    }
}

application {
    mainClass.set("GraphQLServerLauncherKt")
}

tasks.named<Copy>("jvmProcessResources") {
    val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
    from(jsBrowserDistribution)
}

tasks.named<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.named<Jar>("jvmJar"))
}

apollo {
    service("service") {
        generateKotlinModels.set(true)
        packageName.set("gql.client")
        schemaFiles.from(file("src/commonMain/resources/graphql/schema.graphqls"))
        srcDir("src/commonMain/resources/graphql")
        outputDirConnection {
            connectToKotlinSourceSet("commonMain")
        }
    }
}

ktlint {
    filter {
        exclude { element -> element.file.path.contains("generated/") }
    }
}

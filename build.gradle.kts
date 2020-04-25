import com.github.sherter.googlejavaformatgradleplugin.GoogleJavaFormat
import io.freefair.gradle.plugins.lombok.tasks.GenerateLombokConfig
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED

buildscript {
    repositories {
        mavenCentral()
    }
}

repositories {
    mavenCentral()
}

plugins {
    `maven-publish`
    checkstyle
    id("com.github.sherter.google-java-format") version Version.google_format_gradle_plugin
    id("io.freefair.lombok") version Version.lombok_gradle_plugin
    signing
}

subprojects {
    group = "com.slalom"
    version = Version.project

    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "com.github.sherter.google-java-format")
    apply(plugin = "checkstyle")
    apply(plugin = "io.freefair.lombok")
    apply(plugin = "signing")

    repositories {
        mavenCentral()
    }

    dependencies {
        "testImplementation"(platform("org.junit:junit-bom:${Version.junit}"))
        "testImplementation"(group = "org.assertj", name = "assertj-core", version = Version.assertj)
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = Version.java
        targetCompatibility = Version.java
        options.compilerArgs.add("-Xlint:unchecked")
        options.isDeprecation = true
    }

    tasks.withType<Test> {
        useJUnitPlatform()

        testLogging {
            events = mutableSetOf(PASSED, FAILED, SKIPPED)
            exceptionFormat = FULL
        }
    }

    tasks.withType<Checkstyle> {
        val googleJavaFormatTask = tasks.withType<GoogleJavaFormat>()
        dependsOn(googleJavaFormatTask)
    }

    tasks.withType<GenerateLombokConfig> {
        enabled = false
    }

    checkstyle {
        toolVersion = Version.checkstyle_gradle_plugin
        isIgnoreFailures = false
        maxErrors = 0
        maxWarnings = 0
    }

    project.afterEvaluate {
        publishing {
            publications {
                create<MavenPublication>("maven") {
                    from(components["java"])

                    groupId = project.group.toString()
                    artifactId = project.name
                    version = project.version.toString()

                    pom {
                        name.set(project.name)
                        description.set(project.description)
                        url.set(GitHub.url)
                        licenses {
                            license {
                                name.set(License.MIT.name)
                                url.set(License.MIT.url)
                            }
                        }
                        developers {
                            developer {
                                id.set(Developer.carl.id)
                                name.set(Developer.carl.name)
                                email.set(Developer.carl.email)
                                organization.set(Developer.carl.organization.name)
                                organizationUrl.set(Developer.carl.organization.url)
                            }
                        }
                        scm {
                            connection.set(GitHub.connection)
                            developerConnection.set(GitHub.developerConnection)
                            url.set(GitHub.url)
                        }
                    }
                }
            }
        }
        signing {
            sign(publishing.publications["maven"])
        }
    }
}
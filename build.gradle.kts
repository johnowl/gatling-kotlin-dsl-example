plugins {
    kotlin("jvm") version "1.6.10"
    id("io.gatling.gradle") version "3.7.6.1"
}

group = "com.johnowl"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}
plugins {
    kotlin("jvm") version "1.6.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.reactivex:rxjava:1.2.7")
    implementation("org.mongodb:mongodb-driver-rx:1.3.1")
    implementation("io.reactivex:rxnetty-common:0.5.2")
    implementation("io.reactivex:rxnetty-http:0.5.3")
    implementation("io.netty:netty-all:4.1.65.Final")
    implementation("org.slf4j", "slf4j-api", "1.7.25")
    implementation("org.slf4j", "slf4j-log4j12", "1.7.25")
}
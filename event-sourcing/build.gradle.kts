import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.10"
    `java-library`
}
group = "me.khlyting"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("io.reactivex", "rxjava", "1.2.7")
    implementation("io.reactivex", "rxnetty-common", "0.5.2")
    implementation("io.reactivex", "rxnetty-http", "0.5.2")
    implementation("io.reactivex", "rxnetty-tcp", "0.5.2")

    implementation("io.netty", "netty-all", "4.1.42.Final")
    implementation("org.mongodb", "mongodb-driver-rx", "1.5.0")
    implementation("org.slf4j", "slf4j-api", "1.7.25")
    implementation("org.slf4j", "slf4j-log4j12", "1.7.25")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}

kotlin {
    sourceSets["main"].apply {
        kotlin.srcDir("main")
    }
    sourceSets["test"].apply {
        kotlin.srcDir("test")
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

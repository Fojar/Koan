plugins {
    kotlin("jvm") version "1.4.10"
    application
}

repositories {
    jcenter()
}

sourceSets {
    kotlin.sourceSets["main"].kotlin.srcDir("src")
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

application {
    mainClassName = "koan.MainKt"
}
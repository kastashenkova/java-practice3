plugins {
    id("java")
    id("org.example.network-plugin")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Zip>("packageHometask") {
    from(projectDir)
    include(
        "src/**",
        "buildSrc/src/**",
        "buildSrc/build.gradle.kts",
        "buildSrc/settings.gradle.kts",
        "build.gradle.kts",
        "settings.gradle.kts",
        "README.md",
        "gradlew",
        "gradlew.bat",
        "gradle/**"
    )
    archiveFileName.set("${project.name}.zip")
    destinationDirectory.set(layout.buildDirectory.dir("archives"))
}

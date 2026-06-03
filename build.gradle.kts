import java.net.ServerSocket
import java.io.File

plugins {
    id("java")
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

abstract class GenerateTcpServerTask : DefaultTask() {
    @get:Input
    val portNumber: Provider<String> = project.providers.gradleProperty("port").orElse("8089")

    @get:OutputFile
    val outputFile = project.file("src/main/java/org/example/network/TcpServer.java")

    @TaskAction
    fun generate() {
        val resolvedPort = portNumber.get()
        outputFile.parentFile.mkdirs()
        outputFile.writeText("""
                package org.example.network;

                import java.net.ServerSocket;
                import java.net.Socket;

                public class TcpServer {
                     public static void main(String[] args) throws Exception {
                          try (ServerSocket serverSocket = new ServerSocket($resolvedPort)) {
                            while (true) {
                                Socket socket = serverSocket.accept();
                            }
                          } catch (IOException e) {
                            throw new RuntimeException("Error accepting connection", e);
                          }
                    }
                }
            """.trimIndent())
    }
}

@UntrackedTask(because = "Network state is changeable")
abstract class CheckPortTask : DefaultTask() {
    @get:Input
    val portNumber: Provider<String> = project.providers.gradleProperty("port").orElse("8089")

    @TaskAction
    fun check() {
        val port = portNumber.get().toInt()
        try {
            val serverSocket = ServerSocket(port)
            serverSocket.close()
            println("Port $port is available")
        } catch (e: Exception) {
            println("Port $port is in use")
        }
    }
}

class NetworkPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("generateTcpServer", GenerateTcpServerTask::class.java)
        project.tasks.register("checkPort", CheckPortTask::class.java)
    }
}

apply<NetworkPlugin>()

tasks.register<Zip>("packageHometask") {
    from(projectDir)
    include(
        "src/**",
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
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
    @TaskAction
    fun generate() {
        val dir = project.file("src/main/java/org/example/network")
        dir.mkdirs()
        val file = File(dir, "TcpServer.java")
        if (!file.exists()) {
            file.writeText("""
                package org.example.network;

                import java.net.ServerSocket;
                import java.net.Socket;

                public class TcpServer {
                     public static void main(String[] args) throws Exception {
                          try (ServerSocket serverSocket = new ServerSocket(8089)) {
                            while (true) {
                                Socket socket = serverSocket.accept();
                            }
                          } catch (Exception e) {
                            throw new RuntimeException("Error accepting connection", e);
                          }
                    }
                }
            """.trimIndent())
        }
    }
}

abstract class CheckPortTask : DefaultTask() {
    @TaskAction
    fun check() {
        try {
            val serverSocket = ServerSocket(8089)
            serverSocket.close()
            println("Port 8089 is available")
        } catch (e: Exception) {
            println("Port 8089 is in use")
        }
    }
}

class NetworkPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("generateTcpServer", GenerateTcpServerTask::class.java)
        project.tasks.register("checkDefaultPort", CheckPortTask::class.java)
    }
}

apply<NetworkPlugin>()

tasks.register<Zip>("packageHometask") {
    from(projectDir)
    include("src/**", "build.gradle.kts", "settings.gradle.kts", "README.md")
    archiveFileName.set("${project.name}.zip")
    destinationDirectory.set(layout.buildDirectory.dir("archives"))
}
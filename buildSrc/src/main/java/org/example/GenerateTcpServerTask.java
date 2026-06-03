package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

public abstract class GenerateTcpServerTask extends DefaultTask {

    @Input
    public abstract Property<String> getPortNumber();

    @OutputFile
    public abstract RegularFileProperty getOutputFile();

    public GenerateTcpServerTask() {
        getPortNumber().convention(
                getProject().getProviders().gradleProperty("port").orElse("8089")
        );
        getOutputFile().convention(
                getProject().getLayout().getProjectDirectory().file("src/main/java/org/example/network/TcpServer.java")
        );
    }

    @TaskAction
    public void generate() throws IOException {
        String resolvedPort = getPortNumber().get();
        File file = getOutputFile().get().getAsFile();
        String code = """
                package org.example.network;
                
                import java.net.ServerSocket;
                import java.io.IOException;
                
                public class TcpServer {
                     public static void main(String[] args) {
                          try (ServerSocket serverSocket = new ServerSocket(%s)) {
                            System.out.println("Server started on port " + %s);
                            while (true) {
                                serverSocket.accept();
                            }
                          } catch (IOException e) {
                            throw new RuntimeException("Error accepting connection", e);
                          }
                    }
                }
                """.formatted(resolvedPort, resolvedPort);
        Files.writeString(file.toPath(), code);
    }
}

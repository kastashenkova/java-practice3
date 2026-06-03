package org.example;

import java.net.ServerSocket;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.UntrackedTask;

@UntrackedTask(because = "Network state is changeable")
public abstract class CheckPortTask extends DefaultTask {

    @Input
    public abstract Property<String> getPortNumber();

    public CheckPortTask() {
        getPortNumber().convention(
                getProject().getProviders().gradleProperty("port").orElse("8089")
        );
    }

    @TaskAction
    public void check() {
        int port = Integer.parseInt(getPortNumber().get());
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Port " + port + " is available");
        } catch (Exception e) {
            System.out.println("Port " + port + " is in use");
        }
    }
}

package org.example;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class NetworkPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getTasks().register("generateTcpServer", GenerateTcpServerTask.class);
        project.getTasks().register("checkPort", CheckPortTask.class);
    }
}

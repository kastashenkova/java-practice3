plugins {
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("networkPlugin") {
            id = "org.example.network-plugin"
            implementationClass = "org.example.NetworkPlugin"
        }
    }
}
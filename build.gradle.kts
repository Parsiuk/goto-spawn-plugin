plugins {
    java
}

group = "com.gotospawn"
version = "1.0.2"

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.1.1.build.+")
}

tasks.processResources {
    expand("version" to project.version)
}

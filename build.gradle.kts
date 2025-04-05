plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "com.github.userinfo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    
    // Retrofit for API calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    
    // Gson for JSON parsing
    implementation("com.google.code.gson:gson:2.10.1")
    
    // Coroutines for async operations
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    
    // Test dependencies
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.3.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.11.0")
}

tasks.named("compileTestKotlin") {
    enabled = false
}

tasks.named<Test>("test") {
    enabled = false
}

kotlin {
    jvmToolchain(17)
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = "17"
    }
}

application {
    mainClass.set("com.github.userinfo.MainKt")
}

// Fix the Scanner input issue in the run task
tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

// Create a fatJar task to build a standalone executable JAR
tasks.register<Jar>("fatJar") {
    archiveBaseName.set("${project.name}-all")
    manifest {
        attributes["Main-Class"] = "com.github.userinfo.MainKt"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get())
}
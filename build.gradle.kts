plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.apache.poi:poi:5.2.0")
    implementation("org.apache.poi:poi-ooxml:5.2.0")
    implementation("org.jxls:jxls-jexcel:1.0.9")
    implementation("org.dhatim:fastexcel-reader:0.15.3")
    implementation("org.dhatim:fastexcel:0.15.3")
}

tasks.test {
    useJUnitPlatform()
}
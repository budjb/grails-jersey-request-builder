grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global")
    log "warn"
    repositories {
        grailsCentral()
        mavenCentral()
    }
    dependencies {
        // We use the jaxrs plugin, which uses jersey 1.8.
        // When the jaxrs plugin updates, we'll need to update this version
        // so that the core library version matches.
        compile 'com.sun.jersey:jersey-client:1.8'
        compile 'com.sun.jersey:jersey-server:1.8'
        compile 'com.sun.jersey:jersey-core:1.8'
    }

    plugins {
        build(":tomcat:$grailsVersion",
              ":release:2.0.3",
              ":rest-client-builder:1.0.2") {
            export = false
        }
    }
}

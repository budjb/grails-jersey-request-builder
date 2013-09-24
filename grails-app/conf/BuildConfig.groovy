grails.project.work.dir = 'target'

grails.project.dependency.resolution = {

    inherits 'global'
    log 'warn'

    repositories {
        grailsCentral()
        mavenLocal()
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
        build ':release:2.2.1', ':rest-client-builder:1.0.3', {
            export = false
        }
    }
}

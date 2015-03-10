grails.project.work.dir = 'target'

grails.project.dependency.resolution = {

    inherits 'global'
    log 'warn'

    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
        mavenRepo "https://repo.grails.org/grails/plugins"
    }

    dependencies {
        // We use the jaxrs plugin, which uses jersey 1.8.
        // When the jaxrs plugin updates, we'll need to update this version
        // so that the core library version matches.
        compile 'com.sun.jersey:jersey-client:1.8'
        compile 'com.sun.jersey:jersey-server:1.8'
        compile 'com.sun.jersey:jersey-core:1.8'
        test(
            'org.mockito:mockito-all:1.9.5',
            'com.cyrusinnovation:mockito-groovy-support:1.3',
        ) {
            export = false
        }
    }

    plugins {
        build ':release:2.2.1', {
            export = false
        }
        test(
            ":functional-test:2.0.RC1",
            ":tomcat:$grailsVersion",
            ":spock:0.7",
            ":code-coverage:1.2.7"
        ) {
            export = false
        }
    }
}

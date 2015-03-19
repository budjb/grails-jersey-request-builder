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
        compile 'com.sun.jersey:jersey-client:1.19'
        compile 'com.sun.jersey:jersey-core:1.19'

        test('org.objenesis:objenesis:2.1') {
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

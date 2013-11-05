class JerseyRequestBuilderGrailsPlugin {
    def version = "1.1.3"
    def grailsVersion = "2.0 > *"
    def title = "Jersey Request Builder Plugin"
    def author = "Bud Byrd"
    def authorEmail = "bud.byrd@gmail.com"
    def description = 'Provides the Jersey client library and a builder to simplify use of the library.'
    def documentation = "http://budjb.github.io/grails-jersey-request-builder/doc/manual/index.html"
    def license = "APACHE"
    def issueManagement = [system: 'GITHUB', url: 'https://github.com/budjb/grails-jersey-request-builder/issues']
    def scm = [url: 'https://github.com/budjb/grails-jersey-request-builder']
    def pluginExcludes = [
        'web-app/**',
        'grails-app/controllers/**',
        'grails-app/conf/UrlMappings.groovy',
        'src/groovy/com/budjb/requestbuilder/RequestBuilderTest.groovy',
        'test/**'
    ]
}

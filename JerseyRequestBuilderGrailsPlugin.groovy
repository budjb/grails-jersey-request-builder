/*
 * Copyright 2013 Bud Byrd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class JerseyRequestBuilderGrailsPlugin {
    def version = "1.2.0"
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
        'grails-app/controllers/**',
        'src/groovy/com/budjb/requestbuilder/RequestBuilderTest.groovy',
        'src/docs/**'
    ]
}

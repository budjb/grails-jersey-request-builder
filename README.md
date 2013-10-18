Jersey Request Builder Plugin
===================================

Usage
-----
The Jersey Request Builder plugin provides a convenient helper class to build and process
web requests utilizing the Jersey library.

Basic usage is as follows:

    def response = new RequestBuilder().get {
        uri = 'http://example.com'
        query = [
            'param1': 'value1',
            'param2': 'value2'
        ]
        headers = [
            'Content-type': 'text/plain'
        ]
    }

At a minimum, the uri is required. GET, POST, PUT, and DELETE HTTP methods are provided.
The following options are support by the RequestBuilder class:

* connectionTimeout: Timeout for the client to make a connection to the remote host.
* readTimeout: Timeout for the client to finish the request before giving up.
* uri: Full URL for destination of the request.
* query: A simple map of named query parameters.
* headers: A simple map of named headers.
* form: A convenience map to build a form request.
* contentType: The content-type of the request.
* accept: The content-type the client expects to receive from the response.
* cookies: A simple map of named cookie parameters to send with the request.
* convertJson: If true, and the response sends a JSON-compatible response type, automatically attempt to convert
  the response as the return.
* binaryResponse: Returns the raw byte array of the response instead of converting it to a string or
  attempting to convert to JSON.
* skipStatusCheck: If false (default), does not check the HTTP status code.  If true, a non-200 HTTP status code
  will throw a correctly named exception for the status code.
* rawClientResponse: If true (default is false), the RequestBuilder will return the Jersey ClientResponse object without
  doing any processing work on it.
* followRedirects: If true (default), automatically follow redirects sent as a response.
* body: Body of the request to send.  If a list or map is given, it is automatically converted to a JSON string.
* debug: If true (default is false), the request is logged at the debug level to a log4j logger.
* ignoreInvalidSSL: If true (default is false), the SSL trust store is not used when making the request.
  This avoids having to add untrusted certs to the Java certs trust file.
* useBasicAuth: If true (default is false), send HTTP basic auth with the request.
* basicAuthUserName: If using basic auth, set this to the username to send.
* basicAuthPassword: If using basic auth, set this to the password to send.


Changelog
---------
**1.1.2**
* Removed logic to set an all-trusting SSLContext globally when the ignoreInvalidSSL option is used.
  This prevents a race condition where requests made at the same time an SSL-ignoring request builder
  is running will also ignore SSL.

**1.1.1**
* Plugin has been renamed to make it less generic.

**1.1.0**
* Minor revision bump in preparation for public release.

**1.0.19**
* Added the raw response object to HTTP exceptions.

**1.0.18**
* Added query parameter support to UriBuilder.

**1.0.17**
* Fixed bug in UriBuilder that did not respect existing URL parts when passed via 'base'.

**1.0.16**
* Cleaned up UriBuilder a bit to support premade query string in RequestBuilder.

**1.0.15**
* Added flag that instructs Jersey whether to automatically follow redirects or not.
  Note that if a redirect status is encountered and not followed, an exception of
  the appropriate HTTP status code will be thrown.
* In the case that a response has a JSON content type but the body is empty,
  null will be returned as the response when autoconversion of JSON is enabled.

**1.0.14**
* Added UriBuilder and integrated it into the RequestBuilder class. UriBuilder
  can be used independently from RequestBuilder.

**1.0.13**
* Added Jersey Server as a dependency of the plugin. It's not actually required for
  the plugin to work, however, when an application shares a tomcat server that _is_
  using the server library, issues occur that require server to be added to the
  project.

**1.0.12**
* Added basic HTTP auth support.

**1.0.11**
* Made request builder object stateful for reuse.
* Added connection and read timeouts.

**1.0.10**
* Added logging using log4j of the request and response. See the "debug" option.

**1.0.9**
* Fix another silly bug related to JSON conversion.
* Added lists to the JSON conversion process.

**1.0.8**
* Fix order of operations problem with JSON conversion.

**1.0.7**
* Also set the content-type for map to JSON conversions.

**1.0.6**
* Did a check for maps in the body of a request, and convert them to JSON if maps are found.

**1.0.5**
* Added cookie support.
* Changed closure delegation mode to owner first, should make your code work more intuitively.
* Added sub-type exceptions for http status codes.  All inherit from ResponseStatusException.

**1.0.4**
* Added SSL cleanup code, fixes a bug where SSL certs are always ignored.

**1.0.3**
* Added ability to ignore invalid certs.

**1.0.2**
* Added content-type.

**1.0.1**
* Initial release.

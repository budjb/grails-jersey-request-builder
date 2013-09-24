package com.budjb.requestbuilder

import com.budjb.requestbuilder.httpexception.*
import com.sun.jersey.api.client.ClientResponse

/**
 * Exception thrown when a non-200 status is returned from the web service.
 */
class ResponseStatusException extends Exception {
    /**
     * HTTP status code
     */
    int status

    /**
     * Content of the response
     */
    Object content

    /**
     * Actual response object
     */
    ClientResponse response

    /**
     * The text from the <code>LoggingFilter</code>.
     */
    String logText

    /**
     * List of exception types with their status code number.
     */
    protected static final Map<Integer, Class> httpStatusCodes = [
        300: HttpMultipleChoicesException,
        301: HttpMovedPermanentlyException,
        302: HttpFoundException,
        303: HttpSeeOtherException,
        304: HttpNotModifiedException,
        305: HttpUseProxyException,
        307: HttpTemporaryRedirectException,
        400: HttpBadRequestException,
        401: HttpUnauthorizedException,
        402: HttpPaymentRequiredException,
        403: HttpForbiddenException,
        404: HttpNotFoundException,
        405: HttpMethodNotAllowedException,
        406: HttpNotAcceptableException,
        407: HttpProxyAuthenticationRequiredException,
        408: HttpRequestTimeoutException,
        409: HttpConflictException,
        410: HttpGoneException,
        411: HttpLengthRequiredException,
        412: HttpPreconditionFailedException,
        413: HttpRequestEntityTooLargeException,
        414: HttpRequestUriTooLongException,
        415: HttpUnsupportedMediaTypeException,
        416: HttpRequestedRangeNotSatisfiableException,
        417: HttpExpectationFailedException,
        422: HttpUnprocessableEntityException,
        500: HttpInternalServerErrorException,
        501: HttpNotImplementedException,
        502: HttpBadGatewayException,
        503: HttpServiceUnavailableException,
        504: HttpGatewayTimeoutException,
        505: HttpHttpVersionNotSupportedException
    ]

    /**
     * Constructor
     *
     * @param status
     * @param content
     * @param logText
     */
    ResponseStatusException(int status, content, ClientResponse response, String logText) {
        super("Status ${status} received.")

        this.status = status
        this.content = content
        this.response = response
        this.logText = logText
    }

    /**
     * Returns the proper exception type for the given HTTP status code.
     *
     * @param status
     * @param content
     * @param logText
     *
     * @return
     */
    static ResponseStatusException build(int status, content, ClientResponse response, String logText) {
        if (!httpStatusCodes.containsKey(status)) {
            return new ResponseStatusException(status, content, response, logText)
        }
        return httpStatusCodes[status].newInstance(status, content, response, logText)
    }
}

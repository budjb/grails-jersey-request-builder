package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpRequestTimeoutException extends ResponseStatusException {
    public HttpRequestTimeoutException(int status, Object content, String logText) {
        super(status, content, logText)
    }
}

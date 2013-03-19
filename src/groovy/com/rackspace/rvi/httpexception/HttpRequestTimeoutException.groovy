package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpRequestTimeoutException extends ResponseStatusException {
    public HttpRequestTimeoutException(int status, Object content) {
        super(status, content)
    }
}

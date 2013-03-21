package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpPaymentRequiredException extends ResponseStatusException {
    public HttpPaymentRequiredException(int status, Object content, String logText) {
        super(status, content, logText)
    }
}

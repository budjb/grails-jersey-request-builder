package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpPaymentRequiredException extends ResponseStatusException {
    public HttpPaymentRequiredException(int status, Object content) {
        super(status, content)
    }
}

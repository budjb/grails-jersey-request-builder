package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpNotModifiedException extends ResponseStatusException {
    public HttpNotModifiedException(int status, Object content, String logText) {
        super(status, content, logText)
    }
}

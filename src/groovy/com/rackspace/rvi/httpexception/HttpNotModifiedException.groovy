package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpNotModifiedException extends ResponseStatusException {
    public HttpNotModifiedException(int status, Object content) {
        super(status, content)
    }
}

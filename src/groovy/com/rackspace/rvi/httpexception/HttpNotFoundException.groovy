package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpNotFoundException extends ResponseStatusException {
    public HttpNotFoundException(int status, Object content) {
        super(status, content)
    }
}

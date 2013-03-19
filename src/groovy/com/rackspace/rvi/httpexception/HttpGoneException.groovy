package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpGoneException extends ResponseStatusException {
    public HttpGoneException(int status, Object content) {
        super(status, content)
    }
}

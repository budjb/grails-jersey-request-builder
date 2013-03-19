package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpLengthRequiredException extends ResponseStatusException {
    public HttpLengthRequiredException(int status, Object content) {
        super(status, content)
    }
}

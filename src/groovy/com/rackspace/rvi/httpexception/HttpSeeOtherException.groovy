package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpSeeOtherException extends ResponseStatusException {
    public HttpSeeOtherException(int status, Object content) {
        super(status, content)
    }
}

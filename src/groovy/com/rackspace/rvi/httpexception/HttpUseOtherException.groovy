package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpUseOtherException extends ResponseStatusException {
    public HttpUseOtherException(int status, Object content) {
        super(status, content)
    }
}

package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpUseOtherException extends ResponseStatusException {
    public HttpUseOtherException(int status, Object content, String logText) {
        super(status, content, logText)
    }
}

package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpBadGatewayException extends ResponseStatusException {
    public HttpBadGatewayException(int status, Object content) {
        super(status, content)
    }
}

package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpGatewayTimeoutException extends ResponseStatusException {
    public HttpGatewayTimeoutException(int status, Object content) {
        super(status, content)
    }
}

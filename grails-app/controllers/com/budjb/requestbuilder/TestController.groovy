package com.budjb.requestbuilder

class TestController {
    static allowedMethods = [
        testBasicGet:    'GET',
        testBasicPost:   'POST',
        testBasicPut:    'PUT',
        testBasicDelete: 'DELETE',
    ]

    def testBasicGet = {
        render "The quick brown fox jumps over the lazy dog."
    }

    def testBasicPost = {
        render request.reader.text
    }

    def testBasicPut = {
        render request.reader.text
    }

    def testBasicDelete = {
        render "Please don't hurt me!"
    }

    def testAccept = {
        String accept = request.getHeader("Accept")

        switch (accept) {
            case "application/json":
                render text: '{"foo":"bar"}', contentType: 'application/json'
                break
            case "text/plain":
                render text: 'I am plain text.', contentType: 'text/plain'
                break
            case "text/xml":
                render text: '<foo>bar</foo>', contentType: 'text/xml'
                break
            default:
                render text: "I can't handle ${accept}", contentType: 'text/plain', status: 406
        }
    }

    def testReadTimeout = {
        sleep(5000)
        render "Hello, world!"
    }

    def testRedirect = {
        redirect action: 'testBasicGet', permanent: true
    }
}

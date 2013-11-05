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
}

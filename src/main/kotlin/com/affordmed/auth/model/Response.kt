package com.affordmed.auth.model


/**
 * @author Udhaya
 * Created on 03-03-2023
 */

sealed class Response {
    class Success(val cookie: String) : Response()
    class Fails(val msg: String, val longMessage: String): Response()
}

package com.affordmed.auth.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController


/**
 * @author Udhaya
 * Created on 03-03-2023
 */

@RestController
class TestController {

    @GetMapping("/hello")
    fun testFunction(@RequestHeader(required = false) cookie: String? = null): ResponseEntity<String> {
        if (!isValidCookies(cookie))
            return ResponseEntity("Login to access this resource", HttpStatus.UNAUTHORIZED)
        return ResponseEntity("HelloWorld", HttpStatus.OK)
    }
}
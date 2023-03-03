package com.affordmed.auth.controller

import com.affordmed.auth.model.Request
import com.affordmed.auth.model.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * @author Udhaya
 * Created on 03-03-2023
 */

//Username and Password hash pair
private val dataset = mutableMapOf<String, String>()

//Valid active cookies (user, cookie)
private val cookies = mutableMapOf<String, String>()
fun isValidCookies(cookie: String?): Boolean {
    return cookie in cookies.values
}

@RestController
@RequestMapping("/auth/")
class AuthenticationController {
    private val encoder by lazy {
        BCryptPasswordEncoder(10)
    }

    private fun hash(password: String) = encoder.encode(password)

    @PostMapping("/login")
    fun login(@RequestBody request: Request): ResponseEntity<Response> {
        if (request.username !in dataset)
            return ResponseEntity(
                Response.Fails(
                    "Invalid Credentials",
                    "Passed username is not registered with us please signup"
                ), HttpStatus.NOT_FOUND
            )
        val password = dataset[request.username]
        if (encoder.matches(request.password, password)) {
            val cookie = UUID.randomUUID().toString()
            cookies[request.username] = cookie
            return ResponseEntity(Response.Success(cookie), HttpStatus.OK)
        }
        return ResponseEntity(
            Response.Fails("Invalid Credentials", "Password doesn't match"),
            HttpStatus.NOT_ACCEPTABLE
        )
    }

    @PostMapping("/sign-up")
    fun signup(@RequestBody request: Request): ResponseEntity<Response> {
        if (request.username in dataset)
            return ResponseEntity(Response.Fails("Bad username", "Username is already exist"), HttpStatus.CONFLICT)
        val hashedPassword = hash(request.password)
        dataset[request.username] = hashedPassword //Store hashed password so that even data breached it will be safe
        val cookie = UUID.randomUUID().toString()
        cookies[request.username] = cookie
        return ResponseEntity(Response.Success(cookie), HttpStatus.OK)
    }
}
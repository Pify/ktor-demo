package com.simpledeeds

import com.simpledeeds.entity.LoginRequest
import com.simpledeeds.entity.LoginResponse
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.features.*
import io.ktor.gson.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val client = HttpClient(Apache) {

        install(DefaultHeaders)
        install(ContentNegotiation) {
            gson {

            }
        }

        fun Route.testGetFunction() {
            get("/test") {
                call.respondText("This is GET route")
            }

        }

        fun Route.testPostFunction() {
            post("/test") {
                call.respondText("this is POST route")
            }
        }

        fun Route.postWithParameter() {
            post("/test2") {
                val parameters = call.receiveParameters()

                val param1 = parameters["param1"] ?: "DEFAULT"
                val param2 = parameters["param2"] ?: "DEFAULT"

                call.respondText("This is a test POST request with parameter values $param1 and $param2")
            }
        }

        fun Route.login() {
            post("/login") {
                val loginRequest = call.receive<LoginRequest>()

                if (loginRequest.username == "admin" && loginRequest.password == "adminpw") {
                    call.respond(LoginResponse(true, "Login successful"))
                } else {
                    call.respond(LoginResponse(false, "Credentials are invalid!"))
                }
            }
        }

        routing {
            testGetFunction()
            testPostFunction()
            postWithParameter()
            login()
        }
    }
}


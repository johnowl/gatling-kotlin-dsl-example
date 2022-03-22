package com.johnowl.simulations

import io.gatling.javaapi.core.*
import io.gatling.javaapi.http.*
import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.http.HttpDsl.*
import org.slf4j.LoggerFactory
import java.time.Duration

class BasicSimulation : Simulation() {

    private val logger = LoggerFactory.getLogger(BasicSimulation::class.java)

    private val protocol = http
        .baseUrl("http://localhost:8080")
        .contentTypeHeader("application/json")

    private val scn = scenario("Faz login e valida token gerado")
        .exec(http("Gera token com credencial válida")
            .post("/auth/v1/tokens")
            .body(ElFileBody("bodies/credentials.json"))
            .check(jsonPath("$.token").find().saveAs("token"))
        )
        .pause(Duration.ofMillis(50))
        .exec { session ->
            logger.info("token"+ session["token"])
            session
        }
        .exec(http("Valida token")
            .post("/auth/v1/tokens/validate")
            .body(ElFileBody("bodies/token.json"))
            .check(status().`is`(200))
        )
        .pause(Duration.ofMillis(100))

    init {
        setUp(scn.injectOpen(
            nothingFor(Duration.ofSeconds(5)),
            rampUsers(3800).during(Duration.ofMinutes(1))
        )).protocols(protocol)
    }


}
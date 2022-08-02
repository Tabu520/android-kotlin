package com.taipt.plugins

import com.taipt.authenticate
import com.taipt.data.user.UserDataSource
import com.taipt.getSecretInfo
import com.taipt.security.hashing.HashingService
import com.taipt.security.token.TokenConfig
import com.taipt.security.token.TokenService
import com.taipt.signIn
import com.taipt.signUp
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {

    routing {
        signIn(userDataSource, hashingService, tokenService, tokenConfig)
        signUp(hashingService, userDataSource)
        authenticate()
        getSecretInfo()
    }
}

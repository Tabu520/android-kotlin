package com.taipt.security.token

interface TokenService {

    fun generate(config: TokenConfig, vararg claims: TokenClaim): String
}
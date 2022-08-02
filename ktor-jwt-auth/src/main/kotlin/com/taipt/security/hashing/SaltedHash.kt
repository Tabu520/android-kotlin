package com.taipt.security.hashing

data class SaltedHash(
    val hash: String,
    val salt: String
)

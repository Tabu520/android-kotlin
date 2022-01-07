package com.avenue.baseframework.core.models.enums

enum class APIResponseConstant(val status: Int) {

    SUCCESS(1),
    NO_API(1),
    FAIL(0),
    REFRESH_TOKEN_EXPIRED(417),
    TOKEN_EXPIRED(407)
}
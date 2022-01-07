package com.avenue.baseframework.core.models.enums

enum class RefreshTokenConstant(val status: Int) {

    TRUE(1),
    FALSE(0),
    REFRESH_TOKEN_EXPIRED(2);
}
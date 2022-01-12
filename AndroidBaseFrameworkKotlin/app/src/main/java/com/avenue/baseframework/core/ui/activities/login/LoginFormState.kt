package com.avenue.baseframework.core.ui.activities.login

class LoginFormState {

    var usernameError: Int? = null
        private set
    var passwordError: Int? = null
        private set
    var isDataValid = false
        private set

    constructor(isDataValid: Boolean) {
        if (isDataValid) {
            usernameError = 0
            passwordError = 0
        } else {
            usernameError = 1
            passwordError = 1
        }
        this.isDataValid = isDataValid
    }

    constructor(usernameError: Int?, passwordError: Int?) : this(false) {
        this.usernameError = usernameError
        this.passwordError = passwordError
        this.isDataValid = false
    }

}
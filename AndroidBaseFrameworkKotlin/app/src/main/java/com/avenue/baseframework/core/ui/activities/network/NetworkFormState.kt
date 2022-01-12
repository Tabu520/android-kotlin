package com.avenue.baseframework.core.ui.activities.network

class NetworkFormState {

    var hostError: Int? = null
        private set
    var portError: Int? = null
        private set
    var portWebError: Int? = null
        private set
    var isDataValid = false
        private set

    constructor(isDataValid: Boolean) {
        hostError = null
        portError = null
        portWebError = null
        this.isDataValid = isDataValid
    }

    constructor(hostError: Int?, portError: Int?) : this(false) {
        this.hostError = hostError
        this.portError = portError
        isDataValid = false
    }
}

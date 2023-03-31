package com.example.leaderboard.exception

class ApplicationException : Exception {
    val errorType: ErrorType

    constructor(errorType: ErrorType, message: String?, cause: Throwable?) : super(message, cause) {
        this.errorType = errorType
    }

    constructor(errorType: ErrorType, message: String?) : super(message) {
        this.errorType = errorType
    }

    companion object {
        private const val serialVersionUID = 7718828512143293558L
    }
}

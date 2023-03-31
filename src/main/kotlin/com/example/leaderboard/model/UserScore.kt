package com.example.leaderboard.model

import com.example.leaderboard.exception.ApplicationException
import com.example.leaderboard.exception.ErrorType

class UserScore(var username: String, var score: Long) : Comparable<UserScore> {
    override fun compareTo(other: UserScore): Int {
        return if (this.score > other.score) {
            1
        } else if (this.score < other.score) {
            -1
        } else {
            return username.compareTo(other.username)
        }
    }

    override fun toString(): String {
        return "ScoreRequest(" +
            "username=" + username + ", " +
            "score=" + score + ")"
    }

    @Throws(ApplicationException::class)
    fun validateData() {
        if (username.isBlank()) {
            throw ApplicationException(ErrorType.INVALID_SCORE_INPUT, "Username should not be empty")
        }
    }
}

package com.example.leaderboard.model

class ScoreRequest {
    var username: String? = null
    var score: Long? = 0

    override fun toString(): String {
        return "ScoreRequest(" +
            "username=" + username + ", " +
            "score=" + score + ")"
    }
}

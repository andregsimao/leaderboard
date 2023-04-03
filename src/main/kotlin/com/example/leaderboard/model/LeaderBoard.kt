package com.example.leaderboard.model

import java.util.concurrent.PriorityBlockingQueue

class LeaderBoard {
    var yearAndMonth: String? = null
    private val userScores = PriorityBlockingQueue<UserScore>()

    constructor()

    constructor(yearAndMonth: String) {
        this.yearAndMonth = yearAndMonth
    }

    fun buildLeaderBoardList(leaderBoardSize: Int): List<UserScore> {
        val size = leaderBoardSize.coerceAtMost(userScores.size)
        return userScores.toList().sortedDescending().subList(0, size)
    }

    fun addUserScore(userScore: UserScore) {
        userScores.add(userScore)
    }

    fun trimLeaderboard(maxSize: Int): Int {
        var numRemovals = 0
        while (userScores.size > maxSize) {
            userScores.poll()
            numRemovals++
        }
        return numRemovals
    }
}

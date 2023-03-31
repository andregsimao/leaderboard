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
        return userScores.toList().subList(0, leaderBoardSize).sortedDescending()
    }

    fun addUserScore(userScore: UserScore) {
        userScores.add(userScore)
    }

    fun cleanLeaderboard(maxSize: Int): Int {
        var numRemovals = 0
        while (userScores.size > maxSize) {
            userScores.remove(userScores.last())
            numRemovals++
        }
        return numRemovals
    }
}

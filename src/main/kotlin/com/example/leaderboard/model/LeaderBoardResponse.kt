package com.example.leaderboard.model

class LeaderBoardResponse private constructor() {
    var message: String? = null
    var referenceMonth: String? = null
    var leaderBoard: List<UserScore> = ArrayList()

    override fun toString(): String {
        val leaderBoardConcatenated = java.lang.String.join(
            ",",
            leaderBoard.map { it.toString() },
        )
        return "LeaderBoardResponse(" +
            "message=" + message + ", " +
            "referenceMonth=" + referenceMonth + ", " +
            "leaderBoard=" + leaderBoardConcatenated + ")"
    }

    companion object {
        fun buildLeaderBoardResponse(leaderBoard: LeaderBoard, size: Int): LeaderBoardResponse {
            val response = LeaderBoardResponse()
            response.leaderBoard = leaderBoard.buildLeaderBoardList(size)
            response.referenceMonth = leaderBoard.yearAndMonth
            response.message = "Leaderboard built with success"
            return response
        }

        fun buildLeaderBoardErrorResponse(errorMessage: String): LeaderBoardResponse {
            val response = LeaderBoardResponse()
            response.message = "Error to get the leaderboard: $errorMessage"
            return response
        }
    }
}

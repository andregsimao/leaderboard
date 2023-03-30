package com.example.leaderboard.model

class LeaderBoardResponse private constructor() {
    var message: String? = null

    private var leaderBoard: List<UserScore> = ArrayList()

    override fun toString(): String {
        val leaderBoardConcatenated = java.lang.String.join(
            ",",
            leaderBoard.map { it.toString() },
        )
        return "LeaderBoardResponse(" +
            "message=" + message + ", " +
            "leaderBoard=" + leaderBoardConcatenated + ")"
    }

    companion object {
        fun buildLeaderBoardResponse(leaderBoard: List<UserScore>): LeaderBoardResponse {
            val leaderBoardConcatenated = java.lang.String.join(
                ", ",
                leaderBoard.map { it.toString() },
            )
            val response = LeaderBoardResponse()
            response.leaderBoard = leaderBoard
            response.message = "Leaderboard built with success: $leaderBoardConcatenated"
            return response
        }

        fun buildLeaderBoardErrorResponse(errorMessage: String): LeaderBoardResponse {
            val response = LeaderBoardResponse()
            response.message = "Error to get the leaderboard: $errorMessage"
            return response
        }
    }
}
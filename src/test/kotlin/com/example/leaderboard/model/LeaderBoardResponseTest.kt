package com.example.leaderboard.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class LeaderBoardResponseTest {

    @Test
    fun `should build all-time leaderboard response successfully`() {
        val userScore1 = UserScore("username1", 200)
        val userScore2 = UserScore("username2", 100)
        val userScores = listOf(userScore1, userScore2)
        val response = LeaderBoardResponse.buildLeaderBoardAllTimeResponse(userScores)
        assertNull(response.referenceMonth)
        assertEquals("Leaderboard built with success: $userScore1, $userScore2", response.message)
        assertEquals(userScore1, response.leaderBoard[0])
        assertEquals(userScore2, response.leaderBoard[1])
        assertEquals(2, response.leaderBoard.size)
    }

    @Test
    fun `should build monthly leaderboard response successfully`() {
        val userScore1 = UserScore("username1", 200)
        val userScore2 = UserScore("username2", 100)
        val userScores = listOf(userScore1, userScore2)
        val month = "202307"
        val response = LeaderBoardResponse.buildLeaderBoardResponse(userScores, month)
        assertEquals(month, response.referenceMonth)
        assertEquals("Leaderboard built with success: $userScore1, $userScore2", response.message)
        assertEquals(userScore1, response.leaderBoard[0])
        assertEquals(userScore2, response.leaderBoard[1])
        assertEquals(2, response.leaderBoard.size)
    }

    @Test
    fun `should build leaderboard error response`() {
        val errorMessage = "error-message-stub"
        val response = LeaderBoardResponse.buildLeaderBoardErrorResponse(errorMessage)
        assertNull(response.referenceMonth)
        assertEquals("Error to get the leaderboard: $errorMessage", response.message)
        assertEquals(0, response.leaderBoard.size)
    }
}

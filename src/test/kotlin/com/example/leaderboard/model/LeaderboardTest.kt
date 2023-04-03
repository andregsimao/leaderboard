package com.example.leaderboard.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LeaderboardTest {
    @Test
    fun `should build leaderboard successfully when there is less user scores than the size of the leaderboard`() {
        val leaderBoard = LeaderBoard()
        leaderBoard.addUserScore(UserScore("user 1", 1))
        leaderBoard.addUserScore(UserScore("user 2", 3))
        leaderBoard.addUserScore(UserScore("user 3", 4))
        leaderBoard.addUserScore(UserScore("user 4", 2))

        val resultLeaderBoard = leaderBoard.buildLeaderBoardList(10)

        assertEquals(4, resultLeaderBoard.size)
        assertEquals("user 3", resultLeaderBoard[0].username)
        assertEquals("user 2", resultLeaderBoard[1].username)
        assertEquals("user 4", resultLeaderBoard[2].username)
        assertEquals("user 1", resultLeaderBoard[3].username)
    }

    @Test
    fun `should build leaderboard successfully when there is more user scores than the size of the leaderboard`() {
        val leaderBoard = LeaderBoard()
        leaderBoard.addUserScore(UserScore("user 1", 1))
        leaderBoard.addUserScore(UserScore("user 2", 3))
        leaderBoard.addUserScore(UserScore("user 3", 4))
        leaderBoard.addUserScore(UserScore("user 4", 2))
        leaderBoard.addUserScore(UserScore("user 5", 5))

        val resultLeaderBoard = leaderBoard.buildLeaderBoardList(3)

        assertEquals(3, resultLeaderBoard.size)
        assertEquals("user 5", resultLeaderBoard[0].username)
        assertEquals("user 3", resultLeaderBoard[1].username)
        assertEquals("user 2", resultLeaderBoard[2].username)
    }

    @Test
    fun `should build empty leaderboard successfully when there is no user scores`() {
        val leaderBoard = LeaderBoard()

        val resultLeaderBoard = leaderBoard.buildLeaderBoardList(3)

        assertEquals(0, resultLeaderBoard.size)
    }

    @Test
    fun `should add user score successfully`() {
        val leaderBoard = LeaderBoard()
        leaderBoard.addUserScore(UserScore("user 1", 1))
        leaderBoard.addUserScore(UserScore("user 2", 4))
        leaderBoard.addUserScore(UserScore("user 3", 3))

        val resultLeaderBoard = leaderBoard.buildLeaderBoardList(3)

        assertEquals(3, resultLeaderBoard.size)

        assertEquals("user 2", resultLeaderBoard[0].username)
        assertEquals(4, resultLeaderBoard[0].score)

        assertEquals("user 3", resultLeaderBoard[1].username)
        assertEquals(3, resultLeaderBoard[1].score)

        assertEquals("user 1", resultLeaderBoard[2].username)
        assertEquals(1, resultLeaderBoard[2].score)
    }

    @Test
    fun `should not remove user scores when there is less or equal than the limit`() {
        val leaderBoard = LeaderBoard()
        leaderBoard.addUserScore(UserScore("user 1", 1))
        leaderBoard.addUserScore(UserScore("user 2", 4))
        leaderBoard.addUserScore(UserScore("user 3", 3))

        leaderBoard.trimLeaderboard(4)
        var resultLeaderBoard = leaderBoard.buildLeaderBoardList(3)
        assertEquals(3, resultLeaderBoard.size)

        leaderBoard.trimLeaderboard(3)
        resultLeaderBoard = leaderBoard.buildLeaderBoardList(3)

        assertEquals(3, resultLeaderBoard.size)

        assertEquals("user 2", resultLeaderBoard[0].username)
        assertEquals(4, resultLeaderBoard[0].score)

        assertEquals("user 3", resultLeaderBoard[1].username)
        assertEquals(3, resultLeaderBoard[1].score)

        assertEquals("user 1", resultLeaderBoard[2].username)
        assertEquals(1, resultLeaderBoard[2].score)
    }

    @Test
    fun `should remove user scores with less points`() {
        val leaderBoard = LeaderBoard()
        leaderBoard.addUserScore(UserScore("user 1", 4))
        leaderBoard.addUserScore(UserScore("user 2", 1))
        leaderBoard.addUserScore(UserScore("user 3", 2))

        val numRemovals = leaderBoard.trimLeaderboard(2)
        val resultLeaderBoard = leaderBoard.buildLeaderBoardList(3)

        assertEquals(2, resultLeaderBoard.size)
        assertEquals(1, numRemovals)
        assertEquals("user 1", resultLeaderBoard[0].username)
        assertEquals("user 3", resultLeaderBoard[1].username)
    }

    @Test
    fun `should remove user scores using lexicographic order`() {
        var leaderBoard = LeaderBoard()
        leaderBoard.addUserScore(UserScore("user 1", 1))
        leaderBoard.addUserScore(UserScore("user 2", 1))

        val numRemovals = leaderBoard.trimLeaderboard(1)
        var resultLeaderBoard = leaderBoard.buildLeaderBoardList(1)

        assertEquals(1, resultLeaderBoard.size)
        assertEquals(1, numRemovals)
        assertEquals("user 2", resultLeaderBoard[0].username)

        leaderBoard = LeaderBoard()
        leaderBoard.addUserScore(UserScore("user 2", 1))
        leaderBoard.addUserScore(UserScore("user 1", 1))

        leaderBoard.trimLeaderboard(1)
        resultLeaderBoard = leaderBoard.buildLeaderBoardList(1)

        assertEquals(1, resultLeaderBoard.size)
        assertEquals("user 2", resultLeaderBoard[0].username)
    }

    @Test
    fun `should remove any user score when score and username are equal`() {
        val leaderBoard = LeaderBoard()
        leaderBoard.addUserScore(UserScore("user 1", 1))
        leaderBoard.addUserScore(UserScore("user 1", 1))

        leaderBoard.trimLeaderboard(1)
        val resultLeaderBoard = leaderBoard.buildLeaderBoardList(1)

        assertEquals(1, resultLeaderBoard.size)
        assertEquals("user 1", resultLeaderBoard[0].username)
    }
}

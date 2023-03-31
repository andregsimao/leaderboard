package com.example.leaderboard.service

import com.example.leaderboard.exception.ApplicationException
import com.example.leaderboard.exception.ErrorType
import com.example.leaderboard.model.UserScore
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.LocalDateTime

class ScoreServiceTest {
    private lateinit var scoreService: ScoreService

    @BeforeEach
    fun setup() {
        scoreService = ScoreService()
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " "])
    fun `should not add new user score when the user score data is invalid`(userName: String) {
        val userScore = UserScore(userName, -2)
        val currentDate = LocalDateTime.now()
        val expectedMessage = "Username should not be empty"
        val exception = assertThrows<ApplicationException> {
            scoreService.addNewUserScore(userScore, currentDate)
        }
        Assertions.assertEquals(ErrorType.INVALID_SCORE_INPUT, exception.errorType)
        Assertions.assertEquals(expectedMessage, exception.errorType)
    }
}

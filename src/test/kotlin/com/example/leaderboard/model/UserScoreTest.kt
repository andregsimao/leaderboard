package com.example.leaderboard.model

import com.example.leaderboard.exception.ApplicationException
import com.example.leaderboard.exception.ErrorType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class UserScoreTest {
    @ParameterizedTest
    @ValueSource(strings = ["", " "])
    fun `should throw exception when validate blank username`(userName: String) {
        val score = UserScore(userName, 100)
        val expectedMessage = "Username should not be empty"
        val exception = Assertions.assertThrows(ApplicationException::class.java) {
            score.validateData()
        }
        assertEquals(expectedMessage, exception.message)
        assertEquals(ErrorType.INVALID_SCORE_INPUT, exception.errorType)
    }

    @ParameterizedTest
    @ValueSource(strings = ["stub-message", " st ub mes sage "])
    fun `should not throw exception when validate blank username`(userName: String) {
        val score = UserScore(userName, 0)
        score.validateData()
    }
}

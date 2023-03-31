package com.example.leaderboard.controller

import com.example.leaderboard.exception.ApplicationException
import com.example.leaderboard.exception.ErrorType
import com.example.leaderboard.model.UserScore
import com.example.leaderboard.service.ScoreService
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.mockk
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@ExtendWith(SpringExtension::class)
@WebMvcTest(ScoreController::class)
internal class ScoreControllerTest {

    @TestConfiguration
    class ControllerTestConfig {
        @Bean
        fun scoreService() = mockk<ScoreService>()
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var scoreService: ScoreService

    @Test
    @Throws(Exception::class)
    fun `should answer with status 200 when it is possible to insert new user score`() {
        val userScore = UserScore("username", 100)
        val expectedSuccessMessage = "Success adding new Score: ScoreRequest(username=\"${userScore.username}\"" +
            ", score=\"${userScore.score}\")"
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/leaderboard/add-new-user")
                .content(asJsonString(userScore))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", Matchers.`is`(expectedSuccessMessage)))
    }

    @ParameterizedTest
    @EnumSource(ErrorType::class)
    @Throws(
        Exception::class,
    )
    fun `should answer with proper error status when it is not possible to insert new user score`(
        errorType: ErrorType,
    ) {
        val userScore = UserScore("username", 100)
        val exceptionMessage = "exception message"
        val exception = ApplicationException(errorType, exceptionMessage)
        val expectedMessage = "Error to get the leaderboard: $exceptionMessage"
        val resultMatcher = status().isBadRequest
        Mockito.`when`(scoreService.addNewUserScore(userScore, any())).thenThrow(exception)
        mockMvc.perform(
            MockMvcRequestBuilders.post("/leaderboard/add-new-user")
                .content(asJsonString(userScore))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(resultMatcher)
            .andExpect(jsonPath("$", Matchers.`is`(expectedMessage)))
    }

    @Test
    @Throws(Exception::class)
    fun `should return the all-time leaderboard successfully`() {
        val userScore1 = UserScore("username1", 200)
        val userScore2 = UserScore("username2", 100)
        val expectedLeaderBoard = listOf(userScore1, userScore2)
        val expectedSuccessMessage = "Leaderboard built with success: $userScore1, $userScore2"
        Mockito.`when`(scoreService.getAllTimeLeaderBoard()).thenReturn(expectedLeaderBoard)
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/leaderboard/all-time-leaderboard")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.leaderBoard[0].username", Matchers.`is`(userScore1.username)))
            .andExpect(jsonPath("$.leaderBoard[1].username", Matchers.`is`(userScore2.username)))
            .andExpect(jsonPath("$.leaderBoard[0].score", Matchers.`is`(userScore1.score)))
            .andExpect(jsonPath("$.leaderBoard[1].score", Matchers.`is`(userScore2.score)))
            .andExpect(jsonPath("$.message", Matchers.`is`(expectedSuccessMessage)))
    }

    @ParameterizedTest
    @EnumSource(ErrorType::class)
    @Throws(
        Exception::class,
    )
    fun `should return error status when it is not possible to get all-time leaderboard`(
        errorType: ErrorType,
    ) {
        val exceptionMessage = "exception-stub"
        val expectedMessage = "Internal Server Error while getting all time leaderboard: $exceptionMessage"
        Mockito.`when`(scoreService.getAllTimeLeaderBoard())
            .thenThrow(java.lang.Exception(exceptionMessage))
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/leaderboard/all-time-leaderboard")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON),
        ).andExpect(status().isInternalServerError)
            .andExpect(jsonPath("$", Matchers.`is`(expectedMessage)))
    }

    @Test
    @Throws(Exception::class)
    fun `should return the monthly leaderboard successfully`() {
        val userScore1 = UserScore("username1", 200)
        val userScore2 = UserScore("username2", 100)
        val expectedLeaderBoard = listOf(userScore1, userScore2)
        val dateFormat = DateTimeFormatter.ofPattern("yyyyMM")
        val expectedMonth = LocalDateTime.now().format(dateFormat)
        val expectedSuccessMessage = "Leaderboard built with success: $userScore1, $userScore2"
        Mockito.`when`(scoreService.getMonthlyLeaderBoard(any()))
            .thenReturn(Pair(expectedLeaderBoard, expectedMonth))
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/leaderboard/monthly-leaderboard")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.referenceMonth", Matchers.`is`(expectedMonth)))
            .andExpect(jsonPath("$.leaderBoard[0].username", Matchers.`is`(userScore1.username)))
            .andExpect(jsonPath("$.leaderBoard[1].username", Matchers.`is`(userScore2.username)))
            .andExpect(jsonPath("$.leaderBoard[0].score", Matchers.`is`(userScore1.score)))
            .andExpect(jsonPath("$.leaderBoard[1].score", Matchers.`is`(userScore2.score)))
            .andExpect(jsonPath("$.message", Matchers.`is`(expectedSuccessMessage)))
    }

    @ParameterizedTest
    @EnumSource(ErrorType::class)
    @Throws(
        Exception::class,
    )
    fun `should return error status when it is not possible to get monthly leaderboard`(
        errorType: ErrorType,
    ) {
        val exceptionMessage = "exception-stub"
        val expectedMessage = "Internal Server Error while getting monthly leaderboard: $exceptionMessage"
        Mockito.`when`(scoreService.getMonthlyLeaderBoard(any()))
            .thenThrow(java.lang.Exception(exceptionMessage))
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/leaderboard/monthly-leaderboard")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON),
        ).andExpect(status().isInternalServerError)
            .andExpect(jsonPath("$", Matchers.`is`(expectedMessage)))
    }

    companion object {
        fun asJsonString(obj: Any?): String {
            return try {
                ObjectMapper().writeValueAsString(obj)
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }
}

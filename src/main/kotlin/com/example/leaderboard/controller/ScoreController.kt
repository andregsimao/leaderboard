package com.example.leaderboard.controller

import com.example.leaderboard.exception.ApplicationException
import com.example.leaderboard.model.LeaderBoardResponse
import com.example.leaderboard.model.UserScore
import com.example.leaderboard.service.ScoreService
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.ws.rs.Consumes
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@RestController
@RequestMapping("\${leaderboard.mapping.api.signature}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class ScoreController @Autowired constructor(scoreService: ScoreService) {
    private val scoreService: ScoreService

    @Value("\${leaderboard.size}")
    private val leaderBoardSize: Int = 10

    init {
        this.scoreService = scoreService
    }

    @PostMapping("add-new-user")
    fun addNewUserScore(@RequestBody userScore: UserScore): ResponseEntity<String> {
        return try {
            log.info("[ScoreController:addNewUser] Receiving request for adding new score: $userScore")
            scoreService.addNewUserScore(userScore)
            val successMessage = "Success adding new Score: $userScore"
            log.info("[ScoreController:addNewUser] $successMessage")
            ResponseEntity<String>(successMessage, HttpStatus.OK)
        } catch (e: ApplicationException) {
            val errorMessage = "Exception while adding new user score for input $userScore: ${e.message}"
            log.error("[ScoreController:addNewUser] $errorMessage")
            ResponseEntity<String>(errorMessage, HttpStatus.BAD_REQUEST)
        } catch (e: Throwable) {
            val errorMessage = "Internal Server Error while adding new user score for input $userScore: ${e.message}"
            log.error("[ScoreController:addNewUser] $errorMessage")
            ResponseEntity<String>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("all-time-leaderboard")
    fun getAllTimeLeaderBoard(): ResponseEntity<LeaderBoardResponse> {
        return try {
            log.info("[ScoreController:getAllTimeLeaderBoard] Receiving request for getting all time leaderboard")
            val allTimeLeaderBoard = scoreService.getAllTimeLeaderBoard()
            val leaderBoardResponse = LeaderBoardResponse.buildLeaderBoardResponse(allTimeLeaderBoard, leaderBoardSize)
            log.info("[ScoreController:getAllTimeLeaderBoard] Success getting all time leaderboard: $leaderBoardResponse")
            ResponseEntity<LeaderBoardResponse>(leaderBoardResponse, HttpStatus.OK)
        } catch (e: Throwable) {
            val errorMessage = "Internal Server Error while getting all time leaderboard: ${e.message}"
            log.error("[ScoreController:getAllTimeLeaderBoard] $errorMessage")
            val errorResponse = LeaderBoardResponse.buildLeaderBoardErrorResponse(errorMessage)
            ResponseEntity<LeaderBoardResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("monthly-leaderboard")
    fun getMonthlyLeaderBoard(): ResponseEntity<LeaderBoardResponse> {
        return try {
            log.info("[ScoreController:getMonthlyLeaderBoard] Receiving request for getting monthly leaderboard")
            val leaderBoardAndReferenceMonth = scoreService.getMonthlyLeaderBoard()
            val leaderBoardResponse = LeaderBoardResponse
                .buildLeaderBoardResponse(leaderBoardAndReferenceMonth, leaderBoardSize)

            log.info("[ScoreController:getMonthlyLeaderBoard] Success getting monthly leaderboard: $leaderBoardResponse")
            ResponseEntity<LeaderBoardResponse>(leaderBoardResponse, HttpStatus.OK)
        } catch (e: Throwable) {
            val errorMessage = "Internal Server Error while getting monthly leaderboard: ${e.message}"
            log.error("[ScoreController:getMonthlyLeaderBoard] $errorMessage")
            val errorResponse = LeaderBoardResponse.buildLeaderBoardErrorResponse(errorMessage)
            ResponseEntity<LeaderBoardResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}

package com.example.leaderboard.controller

import com.example.leaderboard.exception.ApplicationException
import com.example.leaderboard.exception.ErrorType
import com.example.leaderboard.model.ScoreRequest
import com.example.leaderboard.service.ScoreService
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import javax.ws.rs.Consumes
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@RestController
@RequestMapping("\${leaderboard.mapping.api.signature}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class ScoreController @Autowired constructor(scoreService: ScoreService) {
    private val scoreService: ScoreService

    init {
        this.scoreService = scoreService
    }

    @PostMapping("add-new-user")
    fun addNewUserScore(@RequestBody scoreRequest: ScoreRequest): ResponseEntity<String> {
        return try {
            log.info("[ScoreController:addNewUser] Receiving request for adding new score: $scoreRequest")
            val currentDate = LocalDateTime.now()
            scoreService.addNewUserScore(scoreRequest, currentDate)
            val successMessage = "Success adding new Score: $scoreRequest"
            log.info("[ScoreController:addNewUser] $successMessage")
            ResponseEntity<String>(successMessage, HttpStatus.OK)
        } catch (e: ApplicationException) {
            val errorMessage = "Exception while adding new user score for input $scoreRequest: ${e.message}"
            log.error("[ScoreController:addNewUser] $errorMessage")
            val httpStatus = getHttpStatusFromException(e.errorType)
            ResponseEntity<String>(errorMessage, httpStatus)
        } catch (e: Throwable) {
            val errorMessage = "Internal Server Error while adding new user score for input $scoreRequest: ${e.message}"
            log.error("[ScoreController:addNewUser] $errorMessage")
            ResponseEntity<String>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    private fun getHttpStatusFromException(errorType: ErrorType): HttpStatus {
        // TODO add new cases depending on errorType
        return HttpStatus.BAD_REQUEST
    }
}

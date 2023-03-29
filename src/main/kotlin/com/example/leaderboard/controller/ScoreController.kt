package com.example.leaderboard.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.ws.rs.Consumes
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@RestController
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class ScoreController {

    @PostMapping("/add-new-user")
    fun addNewUserScore(test: Int): String {
        return "leaderboard"
    }
}

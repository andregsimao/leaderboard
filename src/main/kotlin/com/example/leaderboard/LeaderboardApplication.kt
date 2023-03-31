package com.example.leaderboard

import com.example.leaderboard.configuration.LeaderBoardsBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class LeaderboardApplication

fun main(args: Array<String>) {
    runApplication<LeaderboardApplication>(*args)
}

@Bean
fun getLeaderBoardsBean(): LeaderBoardsBean {
    return LeaderBoardsBean()
}

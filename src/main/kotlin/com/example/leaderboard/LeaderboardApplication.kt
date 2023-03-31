package com.example.leaderboard

import com.example.leaderboard.configuration.LeaderBoardsBean
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class LeaderboardApplication

fun main(args: Array<String>) {
    runApplication<LeaderboardApplication>(*args)
}

@Bean
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
fun getLeaderBoardsBean(): LeaderBoardsBean {
    return LeaderBoardsBean()
}

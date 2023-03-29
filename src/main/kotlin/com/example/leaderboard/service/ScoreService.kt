package com.example.leaderboard.service

import com.example.leaderboard.exception.ApplicationException
import com.example.leaderboard.model.ScoreRequest
import jdk.jshell.spi.ExecutionControl.NotImplementedException
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Slf4j
@Service
class ScoreService @Autowired constructor(redisTemplate: StringRedisTemplate) {
    private val redisTemplate: StringRedisTemplate

    init {
        this.redisTemplate = redisTemplate
    }

    @Throws(ApplicationException::class)
    fun addNewUserScore(scoreRequest: ScoreRequest, currentDate: LocalDateTime?) {
        throw NotImplementedException("add new user method not implemented yet")
    }
}

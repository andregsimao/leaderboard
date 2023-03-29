package com.example.leaderboard.service

import com.example.leaderboard.exception.ApplicationException
import com.example.leaderboard.exception.ErrorType
import com.example.leaderboard.model.UserScore
import jdk.jshell.spi.ExecutionControl.NotImplementedException
import lombok.extern.slf4j.Slf4j
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToLong

@Slf4j
@Service
class ScoreService @Autowired constructor(redisTemplate: StringRedisTemplate) {
    private val redisTemplate: StringRedisTemplate

    @Value("\${leaderboard.size}")
    private val leaderBoardSize: Long = 10

    companion object {
        private const val LEADER_BOARD_ALL_TIME_KEY = "leaderboard|alltime"
        private const val LEADER_BOARD_MONTHLY_PREFIX = "leaderboard"
    }

    init {
        this.redisTemplate = redisTemplate
    }

    @Throws(ApplicationException::class)
    fun addNewUserScore(userScore: UserScore, currentDate: LocalDateTime) {
        userScore.validateData()
        updateLeaderBoardRedis(
            key = LEADER_BOARD_ALL_TIME_KEY,
            value = userScore.username,
            score = userScore.score.toDouble(),
        )
        updateLeaderBoardRedis(
            key = getLeaderBoardMonthlyKey(currentDate),
            value = userScore.username,
            score = userScore.score.toDouble(),
        )
    }

    @Throws(ApplicationException::class)
    fun getAllTimeLeaderBoard(): List<UserScore> {
        return getLeaderBoard(LEADER_BOARD_ALL_TIME_KEY)
    }

    @Throws(ApplicationException::class)
    fun getMonthlyLeaderBoard(currentDate: LocalDateTime): List<UserScore> {
        val leaderBoardMonthlyKey = getLeaderBoardMonthlyKey(currentDate)
        return getLeaderBoard(leaderBoardMonthlyKey)
    }

    @Throws(ApplicationException::class)
    private fun getLeaderBoard(key: String): List<UserScore> {
        val userScores = mutableListOf<UserScore>()
        val usernames = redisTemplate.opsForZSet().range(key, 0, leaderBoardSize)
            ?: throw ApplicationException(ErrorType.REDIS_ERROR, "Error to get data from memory in Redis")
        for (username in usernames) {
            val userScore = getUserScoreWithUsername(key, username)
            userScores.add(userScore)
        }
        throw NotImplementedException("getLeaderBoard not implemented yet")
    }

    private fun getUserScoreWithUsername(key: String, username: String): UserScore {
        val score = redisTemplate.opsForZSet().score(key, username)
            ?: throw ApplicationException(ErrorType.REDIS_ERROR, "Error to get data from memory in Redis")
        return UserScore(username, score.roundToLong())
    }

    private fun updateLeaderBoardRedis(key: String, value: String, score: Double) {
        val returnStatus = redisTemplate.opsForZSet().add(key, value, score)
        log.info(
            "[ScoreService:addNewUserScore] Data Inserted/Updated Successfully with status $returnStatus for " +
                "key: $key, value: $value, and score: $score",
        )
        throw NotImplementedException("buildUserScore not implemented yet")
    }

    private fun getLeaderBoardMonthlyKey(date: LocalDateTime): String {
        val yearAndMonth = getMonthAndYearFromDate(date)
        return "$LEADER_BOARD_MONTHLY_PREFIX|$yearAndMonth"
    }

    private fun getMonthAndYearFromDate(date: LocalDateTime): String {
        val dateFormat = DateTimeFormatter.ofPattern("yyyyMM")
        return date.format(dateFormat)
    }
}

package com.example.leaderboard.service

import com.example.leaderboard.exception.ApplicationException
import com.example.leaderboard.exception.ErrorType
import com.example.leaderboard.model.UserScore
import lombok.extern.slf4j.Slf4j
import org.hibernate.query.sqm.tree.SqmNode.log
import org.redisson.api.RedissonClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Slf4j
@Service
class ScoreService @Autowired constructor(redissonClient: RedissonClient) {

    private val redissonClient: RedissonClient

    @Value("\${leaderboard.size}")
    private val leaderBoardSize: Int = 10

    companion object {
        private const val LEADER_BOARD_ALL_TIME_KEY = "leaderboard|alltime"
        private const val LEADER_BOARD_MONTHLY_PREFIX = "leaderboard"
    }

    init {
        this.redissonClient = redissonClient
    }

    @Throws(ApplicationException::class)
    fun addNewUserScore(userScore: UserScore, currentDate: LocalDateTime) {
        userScore.validateData()
        val leaderBoardMonthlyKey = getLeaderBoardMonthlyKey(currentDate)
        updateLeaderBoardRedis(leaderBoardMonthlyKey, userScore)
        updateLeaderBoardRedis(LEADER_BOARD_ALL_TIME_KEY, userScore)
    }

    @Throws(ApplicationException::class)
    fun getAllTimeLeaderBoard(): List<UserScore> {
        return getLeaderBoard(LEADER_BOARD_ALL_TIME_KEY)
    }

    @Throws(ApplicationException::class)
    fun getMonthlyLeaderBoard(currentDate: LocalDateTime): Pair<List<UserScore>, String> {
        val yearAndMonth = getMonthAndYearFromDate(currentDate)
        val leaderBoardMonthlyKey = getLeaderBoardMonthlyKey(currentDate)
        return Pair(getLeaderBoard(leaderBoardMonthlyKey), yearAndMonth)
    }

    @Throws(ApplicationException::class)
    private fun getLeaderBoard(key: String): List<UserScore> {
        val userScores = mutableListOf<UserScore>()
        val priorityQueue = redissonClient.getPriorityBlockingQueue<UserScore>(key)
        for (i in 0 until leaderBoardSize) {
            val userScore = priorityQueue.elementAtOrNull(i)
                ?: throw ApplicationException(ErrorType.NOT_ENOUGH_SCORES, "There is not enough scores to get leaderboard yet")
            userScores.add(userScore)
        }
        return userScores
    }

    private fun updateLeaderBoardRedis(key: String, userScore: UserScore) {
        val priorityQueue = redissonClient.getPriorityBlockingQueue<UserScore>(key)

        priorityQueue.add(userScore)
        log.info(
            "[ScoreService:addNewUserScore] Data Inserted/Updated Successfully " +
                "for key: $key, value: ${userScore.username} and score: ${userScore.score}",
        )
        if (priorityQueue.size > leaderBoardSize) {
            priorityQueue.pollLastAndOfferFirstTo(key)
        }
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

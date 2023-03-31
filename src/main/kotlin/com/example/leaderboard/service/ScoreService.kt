package com.example.leaderboard.service

import com.example.leaderboard.configuration.LeaderBoardsBean
import com.example.leaderboard.exception.ApplicationException
import com.example.leaderboard.exception.ErrorType
import com.example.leaderboard.model.UserScore
import lombok.extern.slf4j.Slf4j
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.PriorityBlockingQueue

@Slf4j
@Service
class ScoreService @Autowired constructor(leaderBoardsBean: LeaderBoardsBean) {

    private val leaderBoardsBean: LeaderBoardsBean

    @Value("\${leaderboard.size}")
    private val leaderBoardSize: Long = 10

    companion object {
        private const val LEADER_BOARD_ALL_TIME_KEY = "leaderboard|alltime"
        private const val LEADER_BOARD_MONTHLY_PREFIX = "leaderboard"
    }

    init {
        this.leaderBoardsBean = leaderBoardsBean
    }

    @Throws(ApplicationException::class)
    fun addNewUserScore(userScore: UserScore, currentDate: LocalDateTime) {
        userScore.validateData()
        updateLeaderBoardRedis(
            key = LEADER_BOARD_ALL_TIME_KEY,
            value = userScore.username,
            score = userScore.score,
        )
        updateLeaderBoardRedis(
            key = getLeaderBoardMonthlyKey(currentDate),
            value = userScore.username,
            score = userScore.score,
        )
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
        val leaderBoard = leaderBoardsBean.getLeaderBoard(key)
        if (hasNotEnouthScores(leaderBoard)) {
            throw ApplicationException(ErrorType.NOT_ENOUGH_SCORES, "There is not enough values to get leaderboard yet")
        }
        return leaderBoard.toList().sortedDescending()
    }

    private fun hasNotEnouthScores(priorityQueues: PriorityBlockingQueue<UserScore>): Boolean {
        return priorityQueues.size < leaderBoardSize
    }

    private fun updateLeaderBoardRedis(key: String, value: String, score: Long) {
        val leaderBoard = leaderBoardsBean.getLeaderBoard(key)
        leaderBoard.add(UserScore(value, score))
        log.info("[ScoreService:updateLeaderBoardRedis] Adding from key: $key: value: $value, and score: $score")
        removeAdditionalElements(key, leaderBoard)
    }

    private fun removeAdditionalElements(key: String, leaderBoard: PriorityBlockingQueue<UserScore>) {
        while (leaderBoard.size > leaderBoardSize) {
            val userScore = leaderBoard.last()
            leaderBoard.remove(userScore)
            log.info(
                "[ScoreService:updateLeaderBoardRedis] Removing from key: $key: value: " +
                    "${userScore.username}, and score: ${userScore.score}",
            )
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

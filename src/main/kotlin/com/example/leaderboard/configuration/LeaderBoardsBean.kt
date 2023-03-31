package com.example.leaderboard.configuration

import com.example.leaderboard.model.LeaderBoard
import org.hibernate.query.sqm.tree.SqmNode
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class LeaderBoardsBean {
    private val leaderBoardsMap: MutableMap<String, LeaderBoard> = HashMap()

    companion object {
        private const val LEADER_BOARD_ALL_TIME_KEY = "leaderboard|alltime"
        private const val LEADER_BOARD_MONTHLY_PREFIX = "leaderboard|"
    }

    init {
        getMonthlyLeaderBoard()
        getAlltimeLeaderBoard()
    }

    fun getMonthlyLeaderBoard(): LeaderBoard {
        val yearAndMonth = getYearAndMonthFromDate(LocalDateTime.now())
        return getMonthlyLeaderBoard(yearAndMonth)
    }

    fun getMonthlyLeaderBoard(yearAndMonth: String): LeaderBoard {
        val key = "$LEADER_BOARD_MONTHLY_PREFIX$yearAndMonth"
        if (leaderBoardsMap[key] == null) {
            SqmNode.log.info("[CleanerScheduler:getMonthlyLeaderBoard] Creating new monthly leaderboard for month $yearAndMonth")
            leaderBoardsMap[key] = LeaderBoard(yearAndMonth)
        }
        return leaderBoardsMap[key]!!
    }

    fun getAlltimeLeaderBoard(): LeaderBoard {
        if (leaderBoardsMap[LEADER_BOARD_ALL_TIME_KEY] == null) {
            SqmNode.log.info("[CleanerScheduler:getAlltimeLeaderBoard] Creating new all-time leaderboard")
            leaderBoardsMap[LEADER_BOARD_ALL_TIME_KEY] = LeaderBoard()
        }
        return leaderBoardsMap[LEADER_BOARD_ALL_TIME_KEY]!!
    }

    fun createNextMonthlyLeaderBoard() {
        val nextMonth = LocalDateTime.now().plusMonths(1)
        getMonthlyLeaderBoard(getYearAndMonthFromDate(nextMonth))
    }

    private fun getYearAndMonthFromDate(date: LocalDateTime): String {
        val dateFormat = DateTimeFormatter.ofPattern("yyyyMM")
        return date.format(dateFormat)
    }
}

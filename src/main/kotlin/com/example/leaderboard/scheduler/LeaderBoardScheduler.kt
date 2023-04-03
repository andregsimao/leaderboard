package com.example.leaderboard.scheduler

import com.example.leaderboard.configuration.LeaderBoardsBean
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class LeaderBoardScheduler @Autowired constructor(leaderBoardsBean: LeaderBoardsBean) {

    @Value("\${leaderboard.size}")
    private val leaderBoardSize: Int = 10

    private val leaderBoardsBean: LeaderBoardsBean

    init {
        this.leaderBoardsBean = leaderBoardsBean
    }

    companion object {
        private const val TIME_TO_CLEAN_MILLIS = 2L
        private const val TIME_TO_CREATE_MONTHLY_LEADER_BOARDS_DAYS = 7L
    }

    @Scheduled(fixedRate = TIME_TO_CLEAN_MILLIS)
    fun trimMonthlyLeaderboard() {
        val monthLeaderBoard = leaderBoardsBean.getMonthlyLeaderBoard()
        val numDeleted = monthLeaderBoard.trimLeaderboard(leaderBoardSize)
        if (numDeleted > 0) {
            log.info(
                "[CleanerScheduler:trimMonthlyLeaderboard] $numDeleted scores deleted from " +
                    "leaderboard of month ${monthLeaderBoard.yearAndMonth}.",
            )
        }
    }

    @Scheduled(fixedRate = TIME_TO_CLEAN_MILLIS)
    fun trimAlltimeLeaderboard() {
        val alltimeLeaderBoard = leaderBoardsBean.getAlltimeLeaderBoard()
        val numDeleted = alltimeLeaderBoard.trimLeaderboard(leaderBoardSize)
        if (numDeleted > 0) {
            log.info(
                "[CleanerScheduler:trimAlltimeLeaderboard] Cleaning unnecessary values from " +
                    "all time leaderboard ${alltimeLeaderBoard.yearAndMonth}.",
            )
        }
    }

    @Scheduled(fixedRate = TIME_TO_CREATE_MONTHLY_LEADER_BOARDS_DAYS * 24 * 3600 * 1000)
    fun createMonthlyLeaderBoards() {
        log.info("[CleanerScheduler:createMonthlyLeaderBoards] Checking if creating leaderboards is necessary")
        leaderBoardsBean.createNextMonthlyLeaderBoard()
    }
}

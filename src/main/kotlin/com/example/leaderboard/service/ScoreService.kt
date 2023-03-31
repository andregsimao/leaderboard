package com.example.leaderboard.service

import com.example.leaderboard.configuration.LeaderBoardsBean
import com.example.leaderboard.exception.ApplicationException
import com.example.leaderboard.model.LeaderBoard
import com.example.leaderboard.model.UserScore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ScoreService @Autowired constructor(leaderBoardsBean: LeaderBoardsBean) {

    private val leaderBoardsBean: LeaderBoardsBean

    init {
        this.leaderBoardsBean = leaderBoardsBean
    }

    @Throws(ApplicationException::class)
    fun addNewUserScore(userScore: UserScore) {
        userScore.validateData()
        val monthlyLeaderBoard = leaderBoardsBean.getMonthlyLeaderBoard()
        val alltimeLeaderBoard = leaderBoardsBean.getAlltimeLeaderBoard()
        monthlyLeaderBoard.addUserScore(userScore)
        alltimeLeaderBoard.addUserScore(userScore)
    }

    @Throws(ApplicationException::class)
    fun getAllTimeLeaderBoard(): LeaderBoard {
        return leaderBoardsBean.getAlltimeLeaderBoard()
    }

    @Throws(ApplicationException::class)
    fun getMonthlyLeaderBoard(): LeaderBoard {
        return leaderBoardsBean.getMonthlyLeaderBoard()
    }

    @Throws(ApplicationException::class)
    fun getMonthlyLeaderBoard(yearAndMonth: String): LeaderBoard {
        return leaderBoardsBean.getMonthlyLeaderBoard(yearAndMonth)
    }
}

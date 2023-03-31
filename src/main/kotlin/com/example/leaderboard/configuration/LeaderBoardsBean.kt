package com.example.leaderboard.configuration

import com.example.leaderboard.model.UserScore
import org.springframework.stereotype.Component
import java.util.concurrent.PriorityBlockingQueue

@Component
class LeaderBoardsBean {
    val leaderBoardsMap: MutableMap<String, PriorityBlockingQueue<UserScore>> = HashMap()

    fun getLeaderBoard(key: String): PriorityBlockingQueue<UserScore> {
        if (leaderBoardsMap[key] == null) {
            leaderBoardsMap[key] = PriorityBlockingQueue()
        }
        return leaderBoardsMap[key]!!
    }
}

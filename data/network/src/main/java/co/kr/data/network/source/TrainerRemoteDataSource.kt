package co.kr.data.network.source

import co.kr.data.network.model.trainer.ActiveTraineesResponse
import co.kr.data.network.model.trainer.DailyPtSessionsResponse
import co.kr.data.network.model.trainer.MonthlyPtSessionCountsResponse
import co.kr.data.network.model.trainer.PtSessionRequest
import co.kr.data.network.service.ApiService
import co.kr.data.network.util.networkHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrainerRemoteDataSource @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun getMonthlyPtSessionCounts(
        year: Int,
        month: Int,
    ): MonthlyPtSessionCountsResponse = networkHandler {
        apiService.getMonthlyPtSessionCounts(
            year = year,
            month = month,
        )
    }

    suspend fun getDailyPtSessions(
        date: String,
    ): DailyPtSessionsResponse = networkHandler {
        apiService.getDailyPtSessions(date)
    }

    suspend fun getActiveTrainees(): ActiveTraineesResponse = networkHandler {
        apiService.getActiveTrainees()
    }

    suspend fun postPtSession(request: PtSessionRequest) = networkHandler {
        apiService.postPtSession(request)
    }

    suspend fun putCompletePtSession(ptSessionId: String) = networkHandler {
        apiService.putCompletePtSession(ptSessionId)
    }
}

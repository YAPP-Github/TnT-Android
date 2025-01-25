package co.kr.data.network.source

import co.kr.data.network.service.TnTService
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("UnusedPrivateProperty")
@Singleton
class TnTDataSource @Inject constructor(
    private val tntService: TnTService,
)

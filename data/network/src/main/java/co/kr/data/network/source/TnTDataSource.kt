package co.kr.data.network.source

import co.kr.data.network.service.TnTService
import javax.inject.Inject

@Suppress("UnusedPrivateProperty")
internal class TnTDataSource @Inject constructor(
    private val tntService: TnTService,
)

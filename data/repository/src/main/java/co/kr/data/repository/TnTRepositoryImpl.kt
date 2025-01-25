package co.kr.data.repository

import co.kr.data.network.source.TnTDataSource
import co.kr.data.storage.source.SessionDataSource
import co.kr.tnt.domain.repository.TnTRepository
import javax.inject.Inject

@Suppress("UnusedPrivateProperty")
internal class TnTRepositoryImpl @Inject constructor(
    private val tnTDataSource: TnTDataSource,
    private val sessionDataSource: SessionDataSource,
) : TnTRepository

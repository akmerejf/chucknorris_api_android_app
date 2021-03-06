package com.akmere.chuck_norris_api_android.domain.usecase

import com.akmere.chuck_norris_api_android.data.model.ChuckNorrisJoke
import com.akmere.chuck_norris_api_android.data.repository.ChuckNorrisJokeRepositoryContract
import io.reactivex.Single
import javax.inject.Inject

class GetChuckNorrisRandomJoke @Inject constructor(private val repo: ChuckNorrisJokeRepositoryContract){

    operator fun invoke(category: String): Single<Result<ChuckNorrisJoke>> {
        val result: Single<Result<ChuckNorrisJoke>>
        val source = repo.findByCategory(category = category)
        result = source.map {
            Result.success(it)
        }.doOnError {
            Result.failure<Throwable>(it)
        }
        return result
    }

}
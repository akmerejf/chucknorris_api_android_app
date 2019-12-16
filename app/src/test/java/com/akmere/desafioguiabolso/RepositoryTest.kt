package com.akmere.desafioguiabolso

import android.accounts.NetworkErrorException
import com.akmere.desafioguiabolso.data.repository.ChuckNorrisJokeRepositoryContract
import com.akmere.desafioguiabolso.data.repository.ChuckNorrisRepository
import com.akmere.desafioguiabolso.data.source.ChuckNorrisApi
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.NullPointerException


@RunWith(JUnit4::class)
class RepositoryTest {

    @MockK
    private lateinit var api: ChuckNorrisApi

    private lateinit var server: MockWebServer
    private lateinit var schedulerProvider: TrampolineSchedulerProvider
    private lateinit var chuckNorrisRepository: ChuckNorrisJokeRepositoryContract

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        server = MockWebServer()
        schedulerProvider = TrampolineSchedulerProvider()
        chuckNorrisRepository = ChuckNorrisRepository(api, schedulerProvider)
        server.start()
    }

    @Test
    fun fetchCategoriesShouldRequestCategories() {
        every { api.requestCategories() } returns Observable.just(listOf())

        chuckNorrisRepository.fetchCategories()

        verify(exactly = 1) {
            api.requestCategories()
        }
    }
    @Test
    fun fetchCategoriesShouldComplete() {
        every { api.requestCategories() } returns Observable.just(listOf())

        val observable = chuckNorrisRepository.fetchCategories()
        val testObserver = TestObserver<List<String>>()
        observable.subscribe(testObserver)

        testObserver.assertComplete()
    }

    @Test
    fun whenNoConnectionFetchCategoriesShouldContainsIOException() {
        val noInternetErrorException = IOException()

        every { api.requestCategories() } returns Observable.error(noInternetErrorException)
        val observable = chuckNorrisRepository.fetchCategories()
        val testObserver = TestObserver<List<String>>()
        observable.subscribe(testObserver)

        testObserver.assertError(noInternetErrorException)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}
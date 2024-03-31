package kh.ad.appgaintask.core.api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kh.ad.appgaintask.model.models.MovieModel
import kh.ad.appgaintask.model.response.MovieSearchResponse
import kh.ad.appgaintask.core.AppExecutors
import kh.ad.appgaintask.core.LogUtil
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class PopularMoviesApiClient private constructor() {
    private val mMoviesPop: MutableLiveData<ArrayList<MovieModel>?> = MutableLiveData<ArrayList<MovieModel>?>()
    private var retrievePopularMoviesRunnable: RetrievePopularMoviesRunnable? = null

    val moviesPop: MutableLiveData<ArrayList<MovieModel>?>
        get() = mMoviesPop

    fun searchPopularMoviesApi(pageNumber: Int) {
        if (retrievePopularMoviesRunnable != null) retrievePopularMoviesRunnable = null
        retrievePopularMoviesRunnable = RetrievePopularMoviesRunnable(pageNumber)
        val myHandler: Future<*> = AppExecutors
            .instance!!
            .networkIO()
            .submit(retrievePopularMoviesRunnable)
        AppExecutors.instance!!.networkIO()
            .schedule({ myHandler.cancel(true) }, 5000, TimeUnit.MILLISECONDS)
    }

    private inner class RetrievePopularMoviesRunnable(private val pageNumber: Int) : Runnable {
        var cancelRequest = false
        override fun run() {
            try {
                val response: Response<MovieSearchResponse?> = getPopularMovies(
                    pageNumber
                )!!.execute()
                if (cancelRequest) {
                    return
                }
                LogUtil.d("PopularMovies", "Headers: " + response.headers())
                if (response.code() == 200) {
                    LogUtil.d("PopularMovies", "Response Body: " + response.body())
                    assert(response.body() != null)
                    val list = ArrayList<MovieModel>(
                        response.body()?.getMovies() ?: ArrayList()
                    )
                    if (pageNumber == 1) {
                        mMoviesPop.postValue(list)
                    } else {
                        val currentMovies: ArrayList<MovieModel> = mMoviesPop.value!!
                        currentMovies.addAll(list)
                        mMoviesPop.postValue(currentMovies)
                    }
                } else {
                    LogUtil.d("PopularMovies", "Response Error: " + response.errorBody())
                    assert(response.errorBody() != null)
                    val error = response.errorBody()!!.string()
                    LogUtil.d("Tag", "Error $error")
                    mMoviesPop.postValue(null)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                mMoviesPop.postValue(null)
            }
            if (cancelRequest) {
                return
            }
        }

        private fun getPopularMovies(pageNumber: Int): Call<MovieSearchResponse?>? {
            return Service.movieApi.getPopularMovies(Credentials.API_KEY, pageNumber)
        }

        private fun cancelRequest() {
            LogUtil.d("Tag", "Cancelling Search Request")
            cancelRequest = true
        }
    }

    companion object {
        var instance: PopularMoviesApiClient? = null
            get() {
                if (field == null) field = PopularMoviesApiClient()
                return field
            }
            private set
    }
}

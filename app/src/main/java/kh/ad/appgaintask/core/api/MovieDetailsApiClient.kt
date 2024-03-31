package kh.ad.appgaintask.core.api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kh.ad.appgaintask.model.models.MovieModel
import kh.ad.appgaintask.core.AppExecutors
import kh.ad.appgaintask.core.LogUtil
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class MovieDetailsApiClient private constructor() {
    private val mMovieDetails: MutableLiveData<MovieModel?> = MutableLiveData<MovieModel?>()
    val movieDetails: MutableLiveData<MovieModel?>
        get() = mMovieDetails

    fun getMovieDetailsById(id: Int) {
        if (retrieveMovieDetailsRunnable != null) retrieveMovieDetailsRunnable = null
        retrieveMovieDetailsRunnable = RetrieveMovieDetailsRunnable(id)
        val myHandler: Future<*> = AppExecutors
            .instance!!
            .networkIO()
            .submit(retrieveMovieDetailsRunnable)
        AppExecutors.instance!!.networkIO()
            .schedule({ myHandler.cancel(true) }, 5000, TimeUnit.MILLISECONDS)
    }

    private var retrieveMovieDetailsRunnable: RetrieveMovieDetailsRunnable? = null

    private inner class RetrieveMovieDetailsRunnable(private val movieId: Int) : Runnable {
        var cancelRequest = false
        override fun run() {
            try {
                val response: Response<MovieModel?> = getMovieDetails(movieId)!!.execute()
                if (cancelRequest) {
                    return
                }
                LogUtil.d("MovieDetails", "Headers: " + response.headers())
                if (response.code() == 200) {
                    LogUtil.d("MovieDetails", "Response Body: " + response.body())
                    assert(response.body() != null)
                    val movie: MovieModel? = response.body()
                    mMovieDetails.postValue(movie)
                } else {
                    LogUtil.d("MovieDetails", "Response Error: " + response.errorBody())
                    assert(response.errorBody() != null)
                    val error = response.errorBody()!!.string()
                    LogUtil.d("Tag", "Error $error")
                    mMovieDetails.postValue(null)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                mMovieDetails.postValue(null)
            }
            if (cancelRequest) {
                return
            }
        }

        private fun getMovieDetails(id: Int): Call<MovieModel?>? {
            return Service.movieApi.getMovieById(id, Credentials.API_KEY)
        }

        private fun cancelRequest() {
            LogUtil.d("Tag", "Cancelling Search Request")
            cancelRequest = true
        }
    }

    companion object {
        var instance: MovieDetailsApiClient? = null
            get() {
                if (field == null) field = MovieDetailsApiClient()
                return field
            }
            private set
    }
}
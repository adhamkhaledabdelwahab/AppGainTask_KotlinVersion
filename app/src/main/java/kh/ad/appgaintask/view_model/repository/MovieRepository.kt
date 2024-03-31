package kh.ad.appgaintask.view_model.repository

import androidx.lifecycle.MutableLiveData
import kh.ad.appgaintask.core.api.PopularMoviesApiClient
import kh.ad.appgaintask.model.models.MovieModel

class MovieRepository private constructor() {
    private val popularMoviesApiClient: PopularMoviesApiClient = PopularMoviesApiClient.instance!!
    private var mPageNumber = 0

    val popularMovies: MutableLiveData<ArrayList<MovieModel>?>
        get() = popularMoviesApiClient.moviesPop

    fun searchPopularMoviesApi(pageNumber: Int) {
        mPageNumber = pageNumber
        popularMoviesApiClient.searchPopularMoviesApi(pageNumber)
    }

    fun searchNextPagePopular() {
        searchPopularMoviesApi(mPageNumber + 1)
    }

    companion object {
        var instance: MovieRepository? = null
            get() {
                if (field == null) field = MovieRepository()
                return field
            }
            private set
    }
}

package kh.ad.appgaintask.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kh.ad.appgaintask.model.models.MovieModel
import kh.ad.appgaintask.view_model.repository.MovieRepository

class MovieListViewModel : ViewModel() {
    private val movieRepository: MovieRepository = MovieRepository.instance!!

    val popMovies: MutableLiveData<ArrayList<MovieModel>?>
        get() = movieRepository.popularMovies

    fun searchPopularMoviesApi(pageNumber: Int) {
        movieRepository.searchPopularMoviesApi(pageNumber)
    }

    fun searchNextPagePopular() {
        movieRepository.searchNextPagePopular()
    }
}
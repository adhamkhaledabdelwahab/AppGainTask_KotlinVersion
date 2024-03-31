package kh.ad.appgaintask.model.response

import kh.ad.appgaintask.model.models.MovieModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MovieSearchResponse {
    @SerializedName("total_results")
    @Expose
    val total_count = 0

    @SerializedName("results")
    @Expose
    private val movies: List<MovieModel>? = null
    fun getMovies(): List<MovieModel>? {
        return movies
    }

    override fun toString(): String {
        return "MovieSearchResponse{" +
                "total_count=" + total_count +
                ", movies=" + movies +
                '}'
    }
}
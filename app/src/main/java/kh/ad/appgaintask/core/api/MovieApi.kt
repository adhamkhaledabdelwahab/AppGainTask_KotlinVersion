package kh.ad.appgaintask.core.api

import kh.ad.appgaintask.model.models.MovieModel
import kh.ad.appgaintask.model.response.MovieSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("3/movie/{movie_id}")
    fun getMovieById(
        @Path("movie_id") id: Int,
        @Query("api_key") key: String?
    ): Call<MovieModel?>?

    @GET("3/movie/popular")
    fun getPopularMovies(
        @Query("api_key") key: String?,
        @Query("page") page: Int
    ): Call<MovieSearchResponse?>?
}
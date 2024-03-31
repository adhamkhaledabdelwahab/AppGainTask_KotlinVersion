package kh.ad.appgaintask.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kh.ad.appgaintask.view.adapter.MovieRecyclerView
import kh.ad.appgaintask.view.adapter.OnMovieListener
import kh.ad.appgaintask.view_model.MovieListViewModel
import kh.ad.appgaintask.view_model.NetworkViewModel
import kh.ad.appgaintask.databinding.ActivityMoviesBinding

class MoviesActivity : AppCompatActivity(), OnMovieListener,
    Observer<Boolean> {
    private lateinit var adapter: MovieRecyclerView
    private lateinit var movieListViewModel: MovieListViewModel
    private lateinit var binding: ActivityMoviesBinding
    private var navigationStart = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val networkViewModel: NetworkViewModel =
            ViewModelProvider(this)[NetworkViewModel::class.java]
        networkViewModel.registerNetworkStateObserver(application)
        networkViewModel.connected.observe(this, this)
        movieListViewModel = ViewModelProvider(this)[MovieListViewModel::class.java]
        configureRecyclerView()
        observeAnyChange()
        observePopularMovies()
        movieListViewModel.searchPopularMoviesApi(1)
    }

    private fun observePopularMovies() {
        movieListViewModel.popMovies.observe(
            this
        ) { movieModels ->
            if (movieModels != null) {
                adapter.setMovies(movieModels)
            }
        }
    }

    private fun observeAnyChange() {
        movieListViewModel.popMovies.observe(
            this
        ) { movieModels ->
            if (movieModels != null) {
                adapter.setMovies(movieModels)
            }
        }
    }

    private fun configureRecyclerView() {
        adapter = MovieRecyclerView(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL, false
        )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollHorizontally(1)) {
                    movieListViewModel.searchNextPagePopular()
                }
            }
        })
    }

    override fun onMovieClick(position: Int) {
        val intent = Intent(
            this@MoviesActivity,
            MovieDetailsActivity::class.java
        )
        intent.putExtra("movie", adapter.getSelectedMovie(position))
        startActivity(intent)
    }

    override fun onChanged(value: Boolean) {
        if (!value) {
            if (!navigationStart) {
                val intent = Intent(this, NoInternetActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
                navigationStart = true
            }
        }
    }
}
package kh.ad.appgaintask.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kh.ad.appgaintask.model.models.MovieModel
import kh.ad.appgaintask.view_model.NetworkViewModel
import kh.ad.appgaintask.R
import kh.ad.appgaintask.core.LogUtil
import kh.ad.appgaintask.core.api.MovieDetailsApiClient
import kh.ad.appgaintask.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), Animation.AnimationListener,
    Observer<Boolean> {
    private lateinit var binding: ActivityMainBinding
    private var navigationStart = false
    private lateinit var animation: Animation
    private var isNetworkConnected = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val networkViewModel: NetworkViewModel = ViewModelProvider(this)[NetworkViewModel::class.java]
        networkViewModel.registerNetworkStateObserver(application)
        networkViewModel.connected.observe(this, this)
        animation = AnimationUtils.loadAnimation(
            this,
            R.anim.bounce_animation
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        val str = "Movies App"
        animation.setAnimationListener(this)
        binding.AppName.text = str
        binding.AppName.postDelayed({ binding.AppName.startAnimation(animation) }, 500)
    }

    override fun onAnimationStart(animation: Animation) {}
    override fun onAnimationEnd(animation: Animation) {
        if (isNetworkConnected) {
            afterAnimation()
        } else {
            val intent = Intent(this@MainActivity, NoInternetActivity::class.java)
            val uri = getIntent().data
            LogUtil.d("MainActivity", "Uri: $uri")
            if (uri != null) {
                val parameters = uri.pathSegments
                if (parameters.size == 2) {
                    intent.putExtra("movieId", parameters[1].toInt())
                }
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }

    private fun afterAnimation() {
        val uri = intent.data
        if (uri != null) {
            LogUtil.d("MainActivity", "Uri: $uri")
            val parameters = uri.pathSegments
            if (parameters.size == 2) {
                val instance: MovieDetailsApiClient = MovieDetailsApiClient.instance!!
                instance.movieDetails.observe(this@MainActivity) { movieModel ->
                    if (!navigationStart) {
                        if (movieModel != null) {
                            navigateToMovieDetailsActivity(movieModel)
                        } else {
                            navigateToMoviesActivity()
                        }
                        navigationStart = true
                    }
                }
                instance.getMovieDetailsById(parameters[1].toInt())
            } else {
                navigateToMoviesActivity()
            }
        } else {
            navigateToMoviesActivity()
        }
    }

    private fun navigateToMoviesActivity() {
        val mHandler = Handler()
        mHandler.postDelayed(Runnable {
            val intent = Intent(this@MainActivity, MoviesActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }, 500)
    }

    private fun navigateToMovieDetailsActivity(movieModel: MovieModel) {
        val intent = Intent(this@MainActivity, MovieDetailsActivity::class.java)
        intent.putExtra("movie", movieModel)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    override fun onAnimationRepeat(animation: Animation) {}

    override fun onChanged(value: Boolean) {
        isNetworkConnected = value
    }
}
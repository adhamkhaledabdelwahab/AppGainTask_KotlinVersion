package kh.ad.appgaintask.view.activity

import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kh.ad.appgaintask.model.models.MovieModel
import kh.ad.appgaintask.view_model.NetworkViewModel
import kh.ad.appgaintask.core.api.MovieDetailsApiClient
import kh.ad.appgaintask.databinding.ActivityNoInternetBinding

class NoInternetActivity : AppCompatActivity(), Observer<Boolean> {
    private var navigationStart = false
    private var model: MovieModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityNoInternetBinding = ActivityNoInternetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDataFromIntent()
        val networkViewModel: NetworkViewModel =
            ViewModelProvider(this)[NetworkViewModel::class.java]
        networkViewModel.registerNetworkStateObserver(application)
        networkViewModel.connected.observe(this, this)
    }

    private fun getDataFromIntent() {
        if (intent.hasExtra("movie")) {
            model = intent.getParcelableExtra("movie")
        }
    }

    override fun onChanged(value: Boolean) {
        if (value) {
            if (!navigationStart) {
                if (model != null) {
                    val intent1 = Intent(this, MoviesActivity::class.java)
                    val intent2 = Intent(this, MovieDetailsActivity::class.java)
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent2.putExtra("movie", model)
                    TaskStackBuilder.create(this).addNextIntent(intent1)
                        .addNextIntentWithParentStack(intent2).startActivities()
                } else {
                    val movieId = intent.getIntExtra("movieId", 0)
                    if (movieId == 0) {
                        val intent = Intent(this, MoviesActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        navigationStart = true
                    } else {
                        val instance: MovieDetailsApiClient = MovieDetailsApiClient.instance!!
                        instance.movieDetails.observe(this@NoInternetActivity) { movieModel ->
                            if (!navigationStart) {
                                if (movieModel != null) {
                                    navigateToMovieDetailsActivity(movieModel)
                                } else {
                                    Toast.makeText(this, "Invalid Movie ID", Toast.LENGTH_SHORT)
                                        .show()
                                    finishAffinity()
                                }
                                navigationStart = true
                            }
                        }
                        instance.getMovieDetailsById(movieId)
                    }
                }
            }
        }
    }

    private fun navigateToMovieDetailsActivity(movieModel: MovieModel) {
        val intent = Intent(this@NoInternetActivity, MovieDetailsActivity::class.java)
        intent.putExtra("movie", movieModel)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}
package kh.ad.appgaintask.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import kh.ad.appgaintask.model.models.MovieModel
import kh.ad.appgaintask.view_model.NetworkViewModel
import kh.ad.appgaintask.databinding.ActivityMovieDetailsBinding

class MovieDetailsActivity() : AppCompatActivity(), View.OnClickListener,
    Observer<Boolean> {
    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var movieModel: MovieModel
    private var navigationStart = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val networkViewModel: NetworkViewModel = ViewModelProvider(this)[NetworkViewModel::class.java]
        networkViewModel.registerNetworkStateObserver(application)
        networkViewModel.connected.observe(this, this)
        binding.backButton.setOnClickListener(this)
        getDataFromIntent()
    }

    private fun getDataFromIntent() {
        if (intent.hasExtra("movie")) {
            movieModel = intent.getParcelableExtra("movie")!!
            binding.movieDetailsDesc.text = movieModel.overview
            binding.movieDetailsTitle.text = movieModel.title
            binding.movieDetailsRating.rating = movieModel.vote_average / 2
            Glide.with(this)
                .load(
                    "https://image.tmdb.org/t/p/original"
                            + if (movieModel.backdrop_path != null) movieModel.backdrop_path else movieModel.poster_path
                )
                .into(binding.movieDetailsImage)
        }
    }

    override fun onClick(v: View) {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun onChanged(value: Boolean) {
        if (!value) {
            if (!navigationStart) {
                val intent = Intent(this, NoInternetActivity::class.java)
                intent.putExtra("movie", movieModel)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
                navigationStart = true
            }
        }
    }
}
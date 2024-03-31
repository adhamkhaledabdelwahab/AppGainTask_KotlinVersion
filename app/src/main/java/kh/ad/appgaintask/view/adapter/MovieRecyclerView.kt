package kh.ad.appgaintask.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kh.ad.appgaintask.model.models.MovieModel
import kh.ad.appgaintask.databinding.PopularMoviesLayoutBinding

class MovieRecyclerView(listener: OnMovieListener) :
    RecyclerView.Adapter<MovieRecyclerView.Holder>() {
    private var mMovies: List<MovieModel> = emptyList()
    private val listener: OnMovieListener
    private lateinit var binding: PopularMoviesLayoutBinding

    init {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        binding = PopularMoviesLayoutBinding.inflate(inflater, parent, false)
        return Holder(binding.root, listener)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.title.text = mMovies[position].title
        holder.ratingBar.numStars = 5
        holder.ratingBar.rating = mMovies[position].vote_average / 2
        Glide.with(holder.itemView.context)
            .load(
                "https://image.tmdb.org/t/p/original"
                        + mMovies[position].poster_path
            )
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return mMovies.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setMovies(movieModels: List<MovieModel>) {
        mMovies = movieModels
        notifyDataSetChanged()
    }

    fun getSelectedMovie(position: Int): MovieModel? {
        if (mMovies.isNotEmpty()) {
            return mMovies[position]
        }
        return null
    }

    inner class Holder(itemView: View, listener: OnMovieListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var listener: OnMovieListener
        var title: TextView = binding.movieTitle
        var imageView: ImageView = binding.movieImg
        var ratingBar: RatingBar = binding.ratingBar

        init {
            binding.root.setOnClickListener(this)
            this.listener = listener
        }

        override fun onClick(v: View) {
            listener.onMovieClick(adapterPosition)
        }
    }
}

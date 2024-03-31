package kh.ad.appgaintask.model.models

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

open class MovieModel : Parcelable {
    val title: String?
    val poster_path: String?
    val backdrop_path: String?
    val release_date: String?
    val id: Int
    val vote_average: Float
    val overview: String?
    val original_language: String?

    constructor(
        title: String?,
        poster_path: String?,
        backdrop_path: String?,
        release_date: String?,
        id: Int,
        vote_average: Float,
        overview: String?,
        original_language: String?
    ) {
        this.title = title
        this.poster_path = poster_path
        this.backdrop_path = backdrop_path
        this.release_date = release_date
        this.id = id
        this.vote_average = vote_average
        this.overview = overview
        this.original_language = original_language
    }

    protected constructor(`in`: Parcel) {
        title = `in`.readString()
        poster_path = `in`.readString()
        backdrop_path = `in`.readString()
        release_date = `in`.readString()
        id = `in`.readInt()
        vote_average = `in`.readFloat()
        overview = `in`.readString()
        original_language = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "MovieModel{" +
                "title='" + title + '\'' +
                ", poster_path='" + poster_path + '\'' +
                ", backdrop_path='" + backdrop_path + '\'' +
                ", release_date='" + release_date + '\'' +
                ", movie_id=" + id +
                ", vote_average=" + vote_average +
                ", overview='" + overview + '\'' +
                ", original_language='" + original_language + '\'' +
                '}'
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeString(poster_path)
        dest.writeString(backdrop_path)
        dest.writeString(release_date)
        dest.writeInt(id)
        dest.writeFloat(vote_average)
        dest.writeString(overview)
        dest.writeString(original_language)
    }

    companion object {
        @JvmField
        val CREATOR: Creator<MovieModel?> = object : Creator<MovieModel?> {
            override fun createFromParcel(`in`: Parcel): MovieModel {
                return MovieModel(`in`)
            }

            override fun newArray(size: Int): Array<MovieModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
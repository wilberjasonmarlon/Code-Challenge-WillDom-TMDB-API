package cu.wilb3r.codechallengetm.data.local.mapper

import cu.wilb3r.codechallengetm.data.local.entities.DBMovie
import cu.wilb3r.codechallengetm.data.local.entities.MoviePopular
import cu.wilb3r.codechallengetm.data.local.entities.MovieTop
import cu.wilb3r.codechallengetm.data.remote.model.Movie
import cu.wilb3r.codechallengetm.domain.model.Media
import cu.wilb3r.codechallengetm.domain.model.MediaType

fun Movie.toMedia() =
    Media(
        id!!,
        poster_path,
        backdrop_path,
        title,
        overview,
        vote_average,
        vote_count,
        MediaType.VIDEO
    )

fun Movie.toEntity() =
    DBMovie(
        adult,
        backdrop_path,
        genre_ids,
        genre,
        id,
        original_language,
        original_title,
        overview,
        popularity,
        poster_path,
        release_date,
        title,
        video,
        vote_average,
        vote_count
    )

fun movieFromEntity(item: DBMovie) =
    Movie(
        item.adult,
        item.backdrop_path,
        item.genre_ids,
        item.genre,
        item.id,
        item.original_language,
        item.original_title,
        item.overview,
        item.popularity,
        item.poster_path,
        item.release_date,
        item.title,
        item.video,
        item.vote_average,
        item.vote_count
    )

fun DBMovie.toMedia() =
    Media(id ?: 0, poster_path, backdrop_path, title, overview, vote_average, vote_count, MediaType.VIDEO)

fun DBMovie.toMoviePopular(page: Int) = MoviePopular(id, page)
fun DBMovie.toMovieTop(page: Int) = MovieTop(id, page)
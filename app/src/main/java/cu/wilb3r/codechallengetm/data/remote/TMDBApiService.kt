package cu.wilb3r.codechallengetm.data.remote

import cu.wilb3r.codechallengetm.data.remote.model.*
import cu.wilb3r.codechallengetm.data.remote.model.dell.SearchResponse
import cu.wilb3r.constant.Api
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface TMDBApiService {

    @GET(Api.MOVIE_TOP_RATED)
    suspend fun getMovieTopRated(
        @QueryMap params: Map<String, String>
    ): Response<PageResponse<Movie>>

    @GET(Api.MOVIE_POPULAR)
    suspend fun getMoviePopular(
        @QueryMap params: Map<String, String>
    ): Response<PageResponse<Movie>>

    @GET(Api.TV_TOP_RATED)
    suspend fun getTvToprated(
        @QueryMap params: Map<String, String>
    ): Response<PageResponse<Tv>>

    @GET(Api.TV_POPULAR)
    suspend fun getTvPopular(
        @QueryMap params: Map<String, String>
    ): Response<PageResponse<Tv>>

    @GET(Api.GENRE_MOVIE)
    suspend fun getMovieGenre(
        @QueryMap params: Map<String, String>
    ): GenreResponse

    @GET(Api.GENRE_TV)
    suspend fun getTvGenre(
        @QueryMap params: Map<String, String>
    ): GenreResponse

    @GET(Api.TV_VIDEOS)
    suspend fun getTvVideos(
        @Path("id") path: Int,
        @QueryMap params: Map<String, String>,
    ): Response<VideosResponse>

    @GET(Api.MOVIE_VIDEOS)
    suspend fun getMovieVideos(
        @Path("id") path: Int,
        @QueryMap params: Map<String, String>,
    ): Response<VideosResponse>

    @GET(Api.MOVIE_CREDITS)
    suspend fun getMovieCredits(
        @Path("id") path: Int,
        @QueryMap params: Map<String, String>,
    ): Response<CastResponse>

    @GET(Api.TV_CREDITS)
    suspend fun getTvCredits(
        @Path("id") path: Int,
        @QueryMap params: Map<String, String>,
    ): Response<CastResponse>

    @GET(Api.SEARCH)
    suspend fun searchAny(
        @QueryMap params: Map<String, String>,
    ): Response<SearchResponse>
}
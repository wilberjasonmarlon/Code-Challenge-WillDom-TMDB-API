package cu.wilb3r.codechallengetm.data.remote.model

import com.google.gson.annotations.SerializedName

data class PageResponse<T>(
    @SerializedName("results")
    val results: List<T>,
    @SerializedName("page")
    val page: Int
)
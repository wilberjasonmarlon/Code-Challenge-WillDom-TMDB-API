package cu.wilb3r.codechallengetm.data.remote.model.dell

data class SearchResponse(
    val page: Int,
    val results: List<SearchResult>,
    val total_pages: Int,
    val total_results: Int
)
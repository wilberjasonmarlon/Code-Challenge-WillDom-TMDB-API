package cu.wilb3r.codechallengetm.ui.modules.detail

import cu.wilb3r.codechallengetm.data.remote.model.Cast
import cu.wilb3r.codechallengetm.data.remote.model.MediaResult


sealed class ViewState {
    data class VideosViewState(
        val isLoading: Boolean = false,
        val videos: List<MediaResult>? = null,
        val error: String? = null
    ) : ViewState()

    data class CastViewState(
        val isLoading: Boolean = false,
        val cast: List<Cast>? = null,
        val error: String? = null
    ) : ViewState()
}

package cu.wilb3r.codechallengetm.ui.modules.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cu.wilb3r.codechallengetm.domain.model.Media
import cu.wilb3r.codechallengetm.domain.model.MediaType
import cu.wilb3r.codechallengetm.domain.usecases.GetCastUseCase
import cu.wilb3r.codechallengetm.domain.usecases.GetVideosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCastUseCase: GetCastUseCase,
    private val getVideosUseCase: GetVideosUseCase
) : ViewModel() {

    private val _media: MutableLiveData<Media> = MutableLiveData()
    val media: LiveData<Media>
        get() = _media


    fun getCast(video_id: Int) = getCastUseCase(video_id)
    fun getVideo(@MediaType type: String, video_id: Int) = getVideosUseCase(type, video_id)


}
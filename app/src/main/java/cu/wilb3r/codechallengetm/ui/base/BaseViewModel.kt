package cu.wilb3r.codechallengetm.ui.base

import android.util.Pair
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    protected var showProgressBarMLD: MutableLiveData<Boolean> = MutableLiveData(false)

    private val onDefaultErrorMLD: MutableLiveData<Pair<Error, Int>> = MutableLiveData()

    protected val shimmerShowMLD: MutableLiveData<Boolean> = MutableLiveData()
    protected val onShouldShowLoadingMLD: MutableLiveData<Boolean> = MutableLiveData()

    fun onCreate() {
        println("=============================| onCreate |=============================")
    }

    fun onPause() {
        println("=============================| onPause |=============================")
    }

    fun onResume() {
        println("=============================| onResume |=============================")
    }

    fun onDestroy() {
        println("=============================| onDestroy |=============================")
    }

    override fun onCleared() {
        super.onCleared()
        println("=============================| onCleared |=============================")
    }

    fun onShowProgressBar(): LiveData<Boolean> = showProgressBarMLD
    fun onShimmerShow(): LiveData<Boolean> = shimmerShowMLD
    fun onGetShouldShowLoading(): LiveData<Boolean> = onShouldShowLoadingMLD
    fun onDefaultErrorService(): LiveData<Pair<Error, Int>> = onDefaultErrorMLD
}
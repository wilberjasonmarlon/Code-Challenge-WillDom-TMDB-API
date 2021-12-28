package cu.wilb3r.codechallengetm.ui.base

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import javax.inject.Inject

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    lateinit var binding: T

    @Inject
    lateinit var inputMethodManager: InputMethodManager

    abstract fun getViewModel(): BaseViewModel

    var baseViewModel: BaseViewModel? = null

    private var actionId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getBindingView()
        setOwnTheme()
        setContentView(binding.root)
        baseViewModel = getViewModel()
        baseViewModel?.onCreate()
        initView()
        setUp(intent.extras)
        doConfigHeader()
    }


    abstract fun getBindingView(): T

    protected open fun setUp(extras: Bundle?) {

    }

    protected open fun initView() {

    }

    protected open fun setOwnTheme() {

    }

    open fun doConfigHeader() {
    }

    override fun onPause() {
        super.onPause()
        baseViewModel?.onPause()
    }

    override fun onResume() {
        super.onResume()
        baseViewModel?.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        baseViewModel?.onDestroy()
    }

    fun openNetworkSetting(actionId: Int) {
        this.actionId = actionId
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        resultLauncher.launch(intent)
    }

    open fun onActivityResult(result: ActivityResult, actionId: Int) = Unit

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onActivityResult(result, actionId)
        }

    fun hideKeyboard() {
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}
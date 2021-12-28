package cu.wilb3r.codechallengetm.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    private var _binding: T? = null

    val binding get() = _binding!!

    private var activity: AppCompatActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUp(arguments)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getBindingView(inflater, container, savedInstanceState)
        initTransition()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        doConfigActivityResult()
        setTitleHeader("")
        doConfigHeader()

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(onBackPressedEnabled()) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    open fun onBackPressed() = Unit

    open fun onBackPressedEnabled(): Boolean {
        return false
    }

    abstract fun getBindingView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): T

    protected open fun initView() = Unit

    protected open fun initTransition() = Unit

    open fun doConfigHeader() = Unit

    open fun doConfigActivityResult() = Unit

    protected open fun setUp(arguments: Bundle?) = Unit

    protected fun getParent(): BaseActivity<*> {
        return activity as BaseActivity<*>
    }

    protected fun setTitleHeader(tittleHeader: String) {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = tittleHeader
    }

    override fun onDestroyView() {
        onDestroyFragment()
        super.onDestroyView()
        _binding = null
    }

    protected open fun onDestroyFragment() {}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity
    }

    fun hideKeyboard() = getParent().hideKeyboard()
}
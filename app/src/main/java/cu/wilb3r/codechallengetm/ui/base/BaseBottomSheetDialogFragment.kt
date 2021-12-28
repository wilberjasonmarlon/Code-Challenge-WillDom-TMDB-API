package cu.wilb3r.codechallengetm.ui.base

import android.app.Activity
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import cu.wilb3r.codechallengetm.R
import cu.wilb3r.codechallengetm.ef.dpToPx

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    lateinit var behavior: BottomSheetBehavior<FrameLayout>
    val displayMetrics = DisplayMetrics()

    companion object {
        private const val START_OFFSET = 0.3f
    }

    open fun getToolbarLayout(): View? = null

    open fun hideToolbar() {}

    open fun showToolbar() {}

    val mParentFragment: Fragment?
        get() {
            val parent = parentFragment
            return if (parent is NavHostFragment)
                parent.parentFragment
            else parent
        }


    open fun onAttachToParentFragment(fragment: Fragment?) {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().display?.getRealMetrics(DisplayMetrics())
        } else {
            @Suppress("DEPRECATION")
            requireActivity().windowManager.defaultDisplay.getMetrics(DisplayMetrics())
        }
        onAttachToParentFragment(mParentFragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        behavior = (dialog as BottomSheetDialog).behavior
        prepareToolbarLayout()
        return dialog
    }

    private fun prepareToolbarLayout() {

        val tv = TypedValue()
        val actionBarHeight =
            if (requireActivity().theme.resolveAttribute(R.attr.actionBarSize, tv, true)) {
                TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
            } else 0

        val minHeight = requireContext().dpToPx(16f)

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

                getToolbarLayout()?.let {

                    val customOffset = if (slideOffset >= START_OFFSET)
                        slideOffset - START_OFFSET
                    else 0f

                    val lp = it.layoutParams
                    lp.height = (actionBarHeight * (customOffset / (1 - START_OFFSET)))
                        .coerceAtLeast(minHeight).toInt()
                    it.layoutParams = lp

                } ?: behavior.removeBottomSheetCallback(this)

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED)
                    showToolbar()
                else
                    hideToolbar()
            }
        })
    }

    protected fun prepareToolbar(toolbar: Toolbar) {
        val activity = requireActivity()
        if (activity is AppCompatActivity)
            activity.setSupportActionBar(toolbar)
    }

    fun hideSoftInput(view: View) {

        val inm: InputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        if (view.windowToken == null) {
            requireActivity().window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            )
        } else
            inm.hideSoftInputFromWindow(view.windowToken, 0)

    }

}
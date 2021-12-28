package cu.wilb3r.codechallengetm.ef

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View


fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun dpToPx(displayMetrics: DisplayMetrics, dip: Float): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dip,
    displayMetrics
)

fun Context.dpToPx(dip: Float): Float = dpToPx(resources.displayMetrics, dip)

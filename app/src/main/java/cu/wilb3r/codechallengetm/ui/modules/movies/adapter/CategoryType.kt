package cu.wilb3r.codechallengetm.ui.modules.movies.adapter

import androidx.annotation.IntDef

@IntDef(CategoryType.POPULAR, CategoryType.TOP_RATED, CategoryType.TRENDING)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.EXPRESSION,
    AnnotationTarget.CLASS,
    AnnotationTarget.TYPE
)
@Retention(AnnotationRetention.SOURCE)
annotation class CategoryType {
    companion object {
        const val POPULAR = 1
        const val TOP_RATED = 2
        const val TRENDING = 3
    }
}

package cu.wilb3r.codechallengetm.domain.model

import androidx.annotation.StringDef

@StringDef(MediaType.VIDEO, MediaType.TV)
@Target(
    AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.EXPRESSION,
    AnnotationTarget.CLASS,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.LOCAL_VARIABLE
)
@Retention(AnnotationRetention.SOURCE)
annotation class MediaType {
    companion object {
        const val VIDEO = "video"
        const val TV = "tv"
    }
}

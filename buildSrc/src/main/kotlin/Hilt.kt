object Hilt {
    private const val versionVmodel = "1.0.0-alpha03"
    const val version = "2.37"
    const val hiltAndroid = "com.google.dagger:hilt-android:$version"
    const val hiltLycViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:$versionVmodel"
    const val hiltCompilerKapt = "com.google.dagger:hilt-compiler:$version"
    const val hiltTestingImp = "com.google.dagger:hilt-android-testing:$version" //androidTestImplementation
    const val hiltTestingKapt = "com.google.dagger:hilt-android-compiler:$version" //kaptAndroidTest
}

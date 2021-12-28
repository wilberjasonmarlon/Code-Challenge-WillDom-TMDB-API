package cu.wilb3r.codechallengetm.ui.modules.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cu.wilb3r.codechallengetm.R
import cu.wilb3r.codechallengetm.databinding.FragmentWebBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WebviewActivity @Inject constructor() : AppCompatActivity() {
    lateinit var binding: FragmentWebBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_CodeChallengeTM)

        binding = FragmentWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent.extras?.getString("url")?.let { url ->
            binding.webView.apply {
                webViewClient = CustomWebViewClient(binding.progress)
                settings.javaScriptEnabled = true
                loadUrl(url)
            }
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
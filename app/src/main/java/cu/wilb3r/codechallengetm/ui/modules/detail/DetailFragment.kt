package cu.wilb3r.codechallengetm.ui.modules.detail

import android.animation.ValueAnimator
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.ImageLoader
import coil.load
import cu.wilb3r.codechallengetm.databinding.FragmentDetailBinding
import cu.wilb3r.codechallengetm.domain.model.Media
import cu.wilb3r.codechallengetm.ui.base.BaseFragment
import cu.wilb3r.codechallengetm.ui.modules.main.WebviewActivity
import cu.wilb3r.constant.Api
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    @Inject
    lateinit var castAdapter: CastAdapter

    @Inject
    lateinit var videoAdapter: VideoAdapter

    @Inject
    lateinit var imageLoader: ImageLoader

    private val args: DetailFragmentArgs by navArgs()

    private var animator: ValueAnimator? = null
    private val vm by viewModels<DetailViewModel>()
    private lateinit var mMedia: Media

    private var jobCast: Job? = null
    private var jobVideo: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMedia = args.mMedia
    }

    override fun setUp(arguments: Bundle?) {
        super.setUp(arguments)
        videoAdapter.addItems(emptyList())
        castAdapter.addItems(emptyList())
    }

    override fun initTransition() {
        super.initTransition()
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        postponeEnterTransition(500L, TimeUnit.MILLISECONDS)
    }

    override fun initView() {
        super.initView()
        binding.mediaInfo.apply {
            backdropImage.transitionName = mMedia.poster_path.toString()
            txtTitle.transitionName = mMedia.name.toString()
            backdropImage.load(
                Api.IMAGESURL + mMedia.backdrop_path.toString(),
                imageLoader
            ) {
                listener(
                    onSuccess = { _, _ ->
                        startPostponedEnterTransition()
                    })
            }
            txtTitle.text = mMedia.name
            mediaOverview.text = mMedia.overview
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
        initRecyclers()
        loadData()
    }

    private fun loadData() {
        jobCast?.cancel()
        jobVideo?.cancel()
        jobCast = lifecycleScope.launch {
            vm.getCast(mMedia.id).collectLatest {
                it.data?.let { data ->
                    castAdapter.addItems(data)
                }
            }
        }
        jobVideo = lifecycleScope.launch {
            vm.getVideo(mMedia.type, mMedia.id).collectLatest {
                it.data?.let { data ->
                    videoAdapter.addItems(data)
                }
            }
        }
    }

    private fun initRecyclers() {
        with(binding.mediaInfo) {
            recyclerCasting.adapter = castAdapter
        }
        with(binding.mediaInfo) {
            recyclerVideos.adapter = videoAdapter.apply {
                setOnItemClickListener { mediaResult, s ->
                    navigateYoutube(s)
                }
            }
        }
    }

    private fun navigateYoutube(uri: String) {
        try {
            startActivity(
                Intent(requireContext(), WebviewActivity::class.java)
                    .putExtra("url", uri)
            )
        } catch (ex: ActivityNotFoundException) {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        jobCast?.cancel()
        jobVideo?.cancel()
    }

    companion object {
        const val MEDIA_EXTRA = "media_extra"
    }

    override fun getBindingView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDetailBinding.inflate(layoutInflater, container, false)


}
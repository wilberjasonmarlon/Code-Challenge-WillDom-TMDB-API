package cu.wilb3r.codechallengetm.ui.modules.movies.detail

import android.animation.ValueAnimator
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import cu.wilb3r.codechallengetm.databinding.FragmentDetailBinding
import cu.wilb3r.codechallengetm.domain.model.Media
import cu.wilb3r.codechallengetm.ui.base.BaseBottomSheetDialogFragment
import cu.wilb3r.codechallengetm.ui.customs.LockableBottomSheetBehavior
import cu.wilb3r.codechallengetm.ui.modules.main.WebviewActivity
import cu.wilb3r.constant.Api
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.absoluteValue


@AndroidEntryPoint
class DetailFragment : BaseBottomSheetDialogFragment() {

    @Inject
    lateinit var castAdapter: CastAdapter

    @Inject
    lateinit var videoAdapter: VideoAdapter

    @Inject lateinit var imageLoader: ImageLoader

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private var animator: ValueAnimator? = null
    private val vm by viewModels<DetailViewModel>()
    private lateinit var mMedia: Media

    private var jobCast: Job? = null
    private var jobVideo: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelable<Media>(MEDIA_EXTRA)?.let {
            mMedia = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        videoAdapter.addItems(emptyList())
        castAdapter.addItems(emptyList())
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        postponeEnterTransition(1000L, TimeUnit.MILLISECONDS)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.constraintLayout.minHeight = displayMetrics.heightPixels
        binding.mediaInfo.backdropImage.transitionName = mMedia.poster_path.toString()
        binding.mediaInfo.mediaOverview.transitionName = mMedia.overview.toString()
        binding.mediaInfo.backdropImage.load(Api.IMAGESURL + mMedia.backdrop_path.toString(), imageLoader)
        binding.mediaInfo.mediaOverview.text = mMedia.overview
        startPostponedEnterTransition()
        setOnClick()
        initRecyclers()
        loadData()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        behavior.peekHeight = displayMetrics.heightPixels
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED)
                    showCloseButton()
                else if (binding.buttonCloseDialog.visibility == View.VISIBLE)
                    hideCloseButton()

                if (behavior is LockableBottomSheetBehavior && newState == BottomSheetBehavior.STATE_EXPANDED)
                    (behavior as LockableBottomSheetBehavior).isDraggingEnable = false

                if (newState != BottomSheetBehavior.STATE_EXPANDED)
                    hideSoftInput(bottomSheet)

            }
        })

        return dialog
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
            startActivity(Intent(requireContext(), WebviewActivity::class.java)
                .putExtra("url", uri))
        } catch (ex: ActivityNotFoundException) {
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        jobCast?.cancel()
        jobVideo?.cancel()
        _binding = null
    }


    private fun showCloseButton() {
        animateCloseButton(0f, 100f)
    }

    private fun hideCloseButton() {
        animateCloseButton(100f, 0f)
    }

    private fun animateCloseButton(from: Float, to: Float) {

        //obtener el valor actual de la animacion
        val realFrom = (animator?.animatedValue ?: from) as Float

        //cancelar cualquier animacion anterior
        animator?.cancel()

        animator = ValueAnimator.ofFloat(realFrom, to)
        animator?.duration =
            (400 * (realFrom - to).absoluteValue / (from - to).absoluteValue).toLong()

        animator?.addUpdateListener { animation ->
            val value = animation.animatedValue as Float

            val rotation = value / 100 * 135
            val alpha = value / 100

            binding.buttonCloseDialog.alpha = alpha
            binding.buttonCloseDialog.rotation = rotation

            if (value <= 1 && binding.buttonCloseDialog.visibility != View.INVISIBLE)
                binding.buttonCloseDialog.visibility = View.INVISIBLE
            else if (binding.buttonCloseDialog.visibility != View.VISIBLE)
                binding.buttonCloseDialog.visibility = View.VISIBLE
        }

        animator?.start()
    }

    private fun setOnClick() {
        binding.buttonCloseDialog.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        const val MEDIA_EXTRA = "media_extra"
    }


}
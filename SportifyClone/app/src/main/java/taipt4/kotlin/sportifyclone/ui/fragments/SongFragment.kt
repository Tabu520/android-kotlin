package taipt4.kotlin.sportifyclone.ui.fragments

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import taipt4.kotlin.sportifyclone.R
import taipt4.kotlin.sportifyclone.adapters.SongAdapter
import taipt4.kotlin.sportifyclone.data.entities.Song
import taipt4.kotlin.sportifyclone.databinding.FragmentHomeBinding
import taipt4.kotlin.sportifyclone.databinding.FragmentSongBinding
import taipt4.kotlin.sportifyclone.exoplayer.isPlaying
import taipt4.kotlin.sportifyclone.exoplayer.toSong
import taipt4.kotlin.sportifyclone.other.Status
import taipt4.kotlin.sportifyclone.ui.viewmodels.MainViewModel
import taipt4.kotlin.sportifyclone.ui.viewmodels.SongViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SongFragment : Fragment() {

    private var _binding: FragmentSongBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()
    private val songViewModel: SongViewModel by viewModels()

    private var currentSong: Song? = null
    private var playbackState: PlaybackStateCompat? = null

    private var shouldUpdateSeekbar = true

    @Inject
    lateinit var glide: RequestManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()

        binding.ivPlayPauseDetail.setOnClickListener {
            currentSong?.let {
                mainViewModel.playOrToggleSong(it, true)
            }
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    setCurrentPlayerTimeToTextView(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                shouldUpdateSeekbar = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
                    mainViewModel.seekTo(it.progress.toLong())
                    shouldUpdateSeekbar = true
                }
            }

        })

        binding.ivSkipPrevious.setOnClickListener {
            mainViewModel.skipToPreviousSong()
        }
        binding.ivSkip.setOnClickListener {
            mainViewModel.skipToNextSong()
        }
    }

    private fun updateTitleAndSongImage(song: Song) {
        val title = "${song.title} - ${song.subtitle}"
        binding.tvSongName.text = title
        glide.load(song.imageUrl).into(binding.ivSongImage)
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItem.observe(viewLifecycleOwner) {
            it?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        result.data?.let { songs ->
                            if (currentSong == null && songs.isNotEmpty()) {
                                currentSong = songs[0]
                                updateTitleAndSongImage(songs[0])
                            }
                        }
                    }
                    else -> Unit
                }
            }
        }

        mainViewModel.currentPlayingSong.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            currentSong = it.toSong()
            updateTitleAndSongImage(currentSong!!)
        }

        mainViewModel.playbackState.observe(viewLifecycleOwner) {
            playbackState = it
            binding.ivPlayPauseDetail.setImageResource(
                if (playbackState?.isPlaying == true) R.drawable.ic_pause else R.drawable.ic_play
            )
            binding.seekBar.progress = it?.position?.toInt() ?: 0
        }

        songViewModel.currentPlayerPosition.observe(viewLifecycleOwner) {
            if (shouldUpdateSeekbar) {
                binding.seekBar.progress = it.toInt()
                setCurrentPlayerTimeToTextView(it)
            }
        }

        songViewModel.currentSongDuration.observe(viewLifecycleOwner) {
            binding.seekBar.max = it.toInt()
            val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
            binding.tvSongDuration.text = dateFormat.format(it)
        }
    }

    private fun setCurrentPlayerTimeToTextView(ms: Long) {
        val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        binding.tvCurTime.text = dateFormat.format(ms)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package taipt4.kotlin.sportifyclone.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import taipt4.kotlin.sportifyclone.R
import taipt4.kotlin.sportifyclone.adapters.SwipeSongAdapter
import taipt4.kotlin.sportifyclone.data.entities.Song
import taipt4.kotlin.sportifyclone.databinding.ActivityMainBinding
import taipt4.kotlin.sportifyclone.exoplayer.isPlaying
import taipt4.kotlin.sportifyclone.exoplayer.toSong
import taipt4.kotlin.sportifyclone.other.Status
import taipt4.kotlin.sportifyclone.ui.viewmodels.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var swipeSongAdapter: SwipeSongAdapter

    private var currentPlayingSong: Song? = null
    private var playbackState: PlaybackStateCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        subscribeToObservers()

        binding.vpSong.adapter = swipeSongAdapter
        binding.vpSong.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (playbackState?.isPlaying == true) {
                    mainViewModel.playOrToggleSong(swipeSongAdapter.songs[position])
                } else {
                    currentPlayingSong = swipeSongAdapter.songs[position]
                }
            }
        })

        binding.ivPlayPause.setOnClickListener {
            currentPlayingSong?.let {
                mainViewModel.playOrToggleSong(it, toggle = true)
            }
        }

        val navController = findNavController(R.id.navHostFragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.songFragment -> hideBottomBar()
                R.id.homeFragment -> showBottomBar()
                else -> showBottomBar()
            }
        }

        swipeSongAdapter.setItemClickListener {
            navController.navigate(R.id.globalActionToSongFragment)
        }
    }

    private fun switchViewPagerToCurrentSong(song: Song) {
        val newItemIndex = swipeSongAdapter.songs.indexOf(song)
        if (newItemIndex != -1) {
            binding.vpSong.currentItem = newItemIndex
            currentPlayingSong = song
        }
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItem.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        result.data?.let { songs ->
                            swipeSongAdapter.songs = songs
                            if (songs.isNotEmpty()) {
                                glide.load((currentPlayingSong ?: songs[0]).imageUrl)
                                    .into(binding.ivCurSongImage)
                            }
                            switchViewPagerToCurrentSong(currentPlayingSong ?: return@observe)
                        }
                    }
                    Status.ERROR -> Unit
                    Status.LOADING -> Unit
                }
            }
        }

        mainViewModel.currentPlayingSong.observe(this) {
            if (it == null) return@observe
            currentPlayingSong = it.toSong()
            glide.load(currentPlayingSong?.imageUrl).into(binding.ivCurSongImage)
            switchViewPagerToCurrentSong(currentPlayingSong ?: return@observe)
        }

        mainViewModel.playbackState.observe(this) {
            playbackState = it
            binding.ivPlayPause.setImageResource(
                if (playbackState?.isPlaying == true) R.drawable.ic_pause else R.drawable.ic_play
            )
        }

        mainViewModel.isConnected.observe(this) {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.ERROR -> {
                        Snackbar.make(
                            binding.rootLayout,
                            result.message ?: "An unknown error occurred!",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    else -> Unit
                }
            }
        }

        mainViewModel.networkError.observe(this) {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.ERROR -> {
                        Snackbar.make(
                            binding.rootLayout,
                            result.message ?: "An unknown error occurred!",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun hideBottomBar() {
        binding.ivPlayPause.isVisible = false
        binding.ivCurSongImage.isVisible = false
        binding.vpSong.isVisible = false
    }

    private fun showBottomBar() {
        binding.ivPlayPause.isVisible = true
        binding.ivCurSongImage.isVisible = true
        binding.vpSong.isVisible = true
    }
}
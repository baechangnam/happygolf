package com.artilearn.happygolfgo.ui.home.rhythmcoach

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.databinding.RhythmCoachFragmentBinding
import com.artilearn.happygolfgo.di.KoinApplication
import com.artilearn.happygolfgo.service.RhythmCoachBroadcastAction

class RhythmCoachFragment : Fragment() {

    private lateinit var binding: RhythmCoachFragmentBinding
    private var mBroadcastReceiver : BroadcastReceiver? = null
    // comments newInstance is never used
//    companion object {
//        fun newInstance() = RhythmCoachFragment()
//    }

    private lateinit var viewModel: RhythmCoachViewModel

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.rhythm_coach_fragment, container,  false)
        binding.tvSpeedDisplay.text = getString( R.string.rhythm_coach_fragment_x_speed_index_default_format)
        initEventHandler()
        KoinApplication.mInstance.mRhythmCoachServiceInterface?.queryState()
        return binding.root
    }

    private fun initObserver() {
         val observerSpeedDisplay = Observer<String>  {
              binding.tvSpeedDisplay.text = it
         }
         viewModel.speedDisplay.observe(viewLifecycleOwner, observerSpeedDisplay)
    }

    private fun initEventHandler() {
        with(binding) {
            btnSpeedUp.setOnClickListener {
                KoinApplication.mInstance.mRhythmCoachServiceInterface?.upSpeed()
            }

            btnSpeedDown.setOnClickListener {
                KoinApplication.mInstance.mRhythmCoachServiceInterface?.downSpeed()
            }

            btnRepeat.setOnClickListener  {
//                btnUn repeat.visibility = View.VISIBLE;
//                btnRepeat.visibility = View.GONE;
                KoinApplication.mInstance.mRhythmCoachServiceInterface?.repeat()
            }

            btnUnrepeat.setOnClickListener {
//                btnUn repeat.visibility = View.GONE;
//                btnRepeat.visibility = View.VISIBLE;
                KoinApplication.mInstance.mRhythmCoachServiceInterface?.playOnce()
            }

            btnPlay.setOnClickListener {
//                btnPlay.visibility = View.GONE;
//                btnPause.visibility = View.VISIBLE;
                KoinApplication.mInstance.mRhythmCoachServiceInterface?.play()
            }

            btnPause.setOnClickListener {
//                btnPlay.visibility = View.VISIBLE
//                btnPause.visibility = View.GONE;
                KoinApplication.mInstance.mRhythmCoachServiceInterface?.pause()
            }
        }
    }

    private fun stringSpeedIndex(speedIndex:Int) : String {
         return when(speedIndex) {
              1 -> "0.8x"
              2 -> "0.9x"
              3 -> "1.0x"
              4 -> "1.1x"
              5 -> "1.2x"
             else -> "0.8x"
         }
    }

    fun onBroadcastFromRhythmCoachService(intent: Intent?) {
        if ( intent != null) {
             if (intent.action == RhythmCoachBroadcastAction.PREPARED) {
                  val duration : Double = (intent.getIntExtra(RhythmCoachBroadcastAction.STATE_DURATION, 0)) / 60000.0
                  val currentPosition  : Double = intent.getIntExtra(RhythmCoachBroadcastAction.STATE_CURRENT_POSITION, 0) / 60000.0
                  binding.pbAudio.max = duration.toInt()
                  binding.pbAudio.progress = currentPosition.toInt()
                  binding.tvMaxPos.text = getString( R.string.rhythm_coach_fragment_to_double_format).format(duration)
                  binding.tvCurrentPos.text = getString( R.string.rhythm_coach_fragment_to_double_format).format(currentPosition)
             } else if (intent.action == RhythmCoachBroadcastAction.CURRENT_PLAY_STATE) {
                    val isPlaying = intent.getBooleanExtra(RhythmCoachBroadcastAction.STATE_IS_PLAYING, false)
                    val speedIndex = intent.getIntExtra(RhythmCoachBroadcastAction.STATE_SPEED_INDEX, 1)
                    val isLooping = intent.getBooleanExtra(RhythmCoachBroadcastAction.STATE_IS_LOOPING, false)
                    binding.tvSpeedDisplay.text =   getString(R.string.rhythm_coach_fragment_x_speed_index_format,  stringSpeedIndex(speedIndex))
                    val duration : Double = (intent.getIntExtra(RhythmCoachBroadcastAction.STATE_DURATION, 0)) / 60000.0
                    val currentPosition  : Double = intent.getIntExtra(RhythmCoachBroadcastAction.STATE_CURRENT_POSITION, 0) / 60000.0
                    binding.tvMaxPos.text = getString( R.string.rhythm_coach_fragment_to_double_format).format(duration)
                    binding.tvCurrentPos.text = getString( R.string.rhythm_coach_fragment_to_double_format).format(currentPosition)
                   if (isPlaying) {
                       binding.btnPlay.visibility = View.GONE
                       binding.btnPause.visibility = View.VISIBLE
                   } else {
                       binding.btnPlay.visibility = View.VISIBLE
                       binding.btnPause.visibility = View.GONE
                   }
                   if (isLooping) {
                       binding.btnUnrepeat.visibility = View.VISIBLE
                       binding.btnRepeat.visibility = View.GONE
                   } else {
                       binding.btnUnrepeat.visibility = View.GONE
                       binding.btnRepeat.visibility = View.VISIBLE
                   }
             } else if (intent.action == RhythmCoachBroadcastAction.STATE_PROGRESS) {
                 val duration : Double = (intent.getIntExtra(RhythmCoachBroadcastAction.STATE_DURATION, 0)) / 60000.0
                 val currentPosition  : Double = intent.getIntExtra(RhythmCoachBroadcastAction.STATE_CURRENT_POSITION, 0) / 60000.0
                 binding.tvMaxPos.text = getString( R.string.rhythm_coach_fragment_to_double_format).format(duration).replace(".", ":")
                 binding.tvCurrentPos.text = getString( R.string.rhythm_coach_fragment_to_double_format).format(currentPosition).replace(".", ":")
                 binding.pbAudio.max = 100 //duration.toInt();
                 try {
                     binding.pbAudio.progress = (currentPosition / duration * 100.0).toInt()
                 } catch (e: Exception) {
                     binding.pbAudio.progress = 100
                 }
             }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[RhythmCoachViewModel::class.java]
        initObserver()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                onBroadcastFromRhythmCoachService(intent)
            }
        }
        registerBroadcastReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterBroadcastReceiver()
    }

    private fun registerBroadcastReceiver() {
        if (mBroadcastReceiver != null) {
            val intentFiler  = IntentFilter()
            intentFiler.addAction(RhythmCoachBroadcastAction.PLAY_STATE_CHANGE)
            intentFiler.addAction(RhythmCoachBroadcastAction.PREPARED)
            intentFiler.addAction(RhythmCoachBroadcastAction.PLAY_SPEED)
            intentFiler.addAction(RhythmCoachBroadcastAction.PAUSE)
            intentFiler.addAction(RhythmCoachBroadcastAction.PLAY)
            intentFiler.addAction(RhythmCoachBroadcastAction.SPEED_UP)
            intentFiler.addAction(RhythmCoachBroadcastAction.SPEED_DOWN)
            intentFiler.addAction(RhythmCoachBroadcastAction.REPEAT)
            intentFiler.addAction(RhythmCoachBroadcastAction.PLAY_ONCE)
            intentFiler.addAction(RhythmCoachBroadcastAction.CURRENT_PLAY_STATE)
            intentFiler.addAction(RhythmCoachBroadcastAction.STATE_PROGRESS)
            context?.registerReceiver(mBroadcastReceiver, intentFiler)
        }
    }

    private fun unregisterBroadcastReceiver() {
        if (mBroadcastReceiver != null) {
            context?.unregisterReceiver(mBroadcastReceiver)
            mBroadcastReceiver = null
        }
    }
}
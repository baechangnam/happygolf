package com.artilearn.happygolfgo.service

class RhythmCoachBroadcastAction {
    companion object {
         const val PREPARED = "RHYTHM_COACH_MEDIA_PREPARED"
         const val PLAY_STATE_CHANGE = "RHYTHM_COACH_MEDIA_PLAY_STATE_CHANGE"
         const val PLAY_SPEED = "RHYTHM_COACH_MEDIA_PLAY_SPEED"
         const val PAUSE = "RHYTHM_COACH_MEDIA_PLAY_PAUSE"
         const val PLAY = "RHYTHM_COACH_MEDIA_PLAY_PLAY"
         const val SPEED_UP = "RHYTHM_COACH_MEDIA_SPEED_UP"
         const val SPEED_DOWN = "RHYTHM_COACH_MEDIA_SPEED_DOWN"
         const val REPEAT = "RHYTHM_COACH_MEDIA_REPEAT"
         const val PLAY_ONCE = "RHYTHM_COACH_MEDIA_PLAY_ONCE"
         const val CURRENT_PLAY_STATE = "RHYTHM_COACH_MEDIA_CURRENT_PLATE_STATE"

         const val STATE_PROGRESS = "RHYTHM_COACH_MEDIA_STATE_PROGRESS"

         const val STATE_DURATION = "DURATION"
         const val STATE_CURRENT_POSITION = "CURRENT_POSITION"

         const val STATE_IS_LOOPING = "IS_LOOPING"
         const val STATE_SPEED_INDEX = "SPEED_INDEX"
         const val STATE_IS_PLAYING = "IS_PLAYING"
    }
}
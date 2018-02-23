package com.dhl.musicplayer.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.dhl.musicplayer.MainActivity

/**
 * Created by Hitesh on 2/23/2018.
 */
class PlayMusicService : Service() {
    var currentPos: Int = 0
    var musicDataList: ArrayList<String> = ArrayList()
    var mp: MediaPlayer? = null
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        currentPos = intent!!.getIntExtra(MainActivity.MUSIC_POSITION, 0)
        musicDataList = intent!!.getStringArrayListExtra(MainActivity.MUSICLIST)

        if (mp != null && mp!!.isPlaying) {
            mp!!.stop()
            mp!!.release()
            mp == null
        }

        mp = MediaPlayer()
        mp!!.setDataSource(musicDataList[currentPos])
        mp!!.prepare()
        mp!!.setOnPreparedListener {
            mp!!.start()
        }

        return super.onStartCommand(intent, flags, startId)
    }
}
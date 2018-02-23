package com.dhl.musicplayer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.dhl.musicplayer.adapters.SongListAdapter
import com.dhl.musicplayer.interfaces.RecycleViewItemClickListener
import com.dhl.musicplayer.models.SongModel
import com.dhl.musicplayer.services.PlayMusicService
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), RecycleViewItemClickListener {
    var mSongsAdapter: SongListAdapter? = null
    var mSongs: ArrayList<SongModel> = ArrayList()
    var allMusicList: ArrayList<String> = ArrayList()

    companion object {
        val MUSICLIST = "musicList"
        val MUSIC_POSITION = "position"
        val REQUEST_PERMISSION_CODE = 12
    }

    override fun onItemClick(view: View, position: Int) {
        for (i in 0 until mSongs.size) {
            allMusicList.add(mSongs[i].mPath)
        }
        Log.e("Song", allMusicList.toString())
        var musicDataIntent = Intent(this@MainActivity, PlayMusicService::class.java)
        musicDataIntent.putStringArrayListExtra(MUSICLIST, allMusicList)
        musicDataIntent.putExtra(MUSIC_POSITION, position)
        this@MainActivity.startService(musicDataIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION_CODE)
        } else {
            loadSongs()
        }

    }

    private fun loadSongs() {
        val songCursor: Cursor? = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null)
        while (songCursor != null && songCursor.moveToNext()) {
            var songName = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            var songDuration = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
            val songPath = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA))
            mSongs.add(SongModel(songName, toMinutes(songDuration.toLong()), songPath))
        }
        mSongsAdapter = SongListAdapter(mSongs, this)
        val layoutManager = LinearLayoutManager(this)
        rvSongs.layoutManager = layoutManager
        rvSongs.setHasFixedSize(true)
        rvSongs.adapter = mSongsAdapter
    }

    private fun toMinutes(millis: Long): String {
        var duracation = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))

        return duracation
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Granted", Toast.LENGTH_SHORT).show()
                loadSongs()
            } else {
                Toast.makeText(applicationContext, "Permissions Not granted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

package com.example.mediaplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader

class SongActivity : AppCompatActivity() {
    private lateinit var songImage: ImageView
    private lateinit var songTitle: TextView
    private lateinit var songSinger: TextView
    private lateinit var songTime: TextView
    private lateinit var play: Button
    private lateinit var next: Button
    private lateinit var prev: Button
    private lateinit var lyrics: Button
    private var player: MediaPlayer? = null
    private lateinit var songDuration: SeekBar
    private lateinit var songNotify: SongNotification
    private var timer: CountDownTimer? = null
    private var totalSeconds: Long = 0
    private var durInt:Int = 0
    private var songsList: List<Song> =
        listOf(
            Song("Chop Suey", "System of a Down", R.raw.chopsuey, R.drawable.chopsuey,R.raw.chopsueylyrics, "chopsueylyrics"),
        Song("Can You Feel My Heart", "Bring Me the Horizon", R.raw.canyoufeelmyheart, R.drawable.canyoufeelmyheart, R.raw.canyoufeelmyheartlyrics,
        "canyoufeelmyheartlyrics"),
            Song("Nico and the Niners", "21 pilots", R.raw.nicoandtheniners,R.drawable.nicoandtheniners, R.raw.nicoandtheninerslyrics,
            "nicoandtheninerslyrics"),
            Song("Wake Me Up", "Avicii", R.raw.wakemeup, R.drawable.wakemeup,R.raw.wakemeuplyrics, "wakemeuplyrics"),
        Song("Locked out of Heaven", "Bruno Mars", R.raw.lockedoutofheaven,  R.drawable.lockedoutofheaven, R.raw.lockedoutofheavenlyrics, "lockedoutofheavenlyrics"))
    private var songIndex: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)
        songImage = findViewById(R.id.songImage)
        songTitle = findViewById(R.id.songTitle)
        songSinger = findViewById(R.id.songSinger)
        play = findViewById(R.id.play)
        next = findViewById(R.id.next)
        prev = findViewById(R.id.prev)
        lyrics = findViewById(R.id.lyricsBtn)
        songTime = findViewById(R.id.songTime)
        songDuration = findViewById(R.id.songDuration)
        lyrics.setOnClickListener {
            var song = songsList[songIndex]
            resources.openRawResource(song.lyricsId).use {
                BufferedReader(it.reader()).use {
                    var sb = StringBuilder()
                    var line = it.readLine()
                    while (line != null){
                        sb.append(line + '\n')
                        line = it.readLine()
                    }
                    LyricsDialog(song.Title, sb.toString()).show(supportFragmentManager, "custom")
                }
            }
        }
        playSong(songsList[0])
        play.setBackgroundResource(android.R.drawable.ic_media_pause)
        play.setOnClickListener {
            if(player?.isPlaying == true) {
                play.setBackgroundResource(android.R.drawable.ic_media_play)
                player?.pause()
                cancelTimer()
            } else {
                play.setBackgroundResource(android.R.drawable.ic_media_pause)
                player?.start()
                resumeTimer(totalSeconds, durInt)
            }
        }
        prev.setOnClickListener {
            playPrevSong()
        }
        next.setOnClickListener {
            playNextSong()
        }
    }


    private fun playSong(song: Song){
        cancelTimer()
        songTitle.text = song.Title
        songSinger.text = song.Singer
        songImage.setImageResource(song.songImageId)
        player = MediaPlayer.create(this, song.songMp3)
        player?.isLooping = false // Sets the player to be looping or non-looping.
        player?.start() // Starts Playback.
        var dur:Long = player!!.duration.toLong()
        durInt = player!!.duration
        var seconds:String = ((dur % 60000)/1000).toString()
        var minutes:String = (dur / 60000).toString()
        totalSeconds = (minutes.toLong() * 60) + seconds.toLong()
        songDuration.progress = 0
        initMins = 0
        initSecs = 0
        songTime.text = "00:00"
        songNotify = SongNotification(this, song.Title, song.songImageId)
        startTimer(totalSeconds, durInt)
    }




    private fun startTimer(totalSeconds: Long, durInt: Int){
         timer = object:CountDownTimer(totalSeconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                    songDuration.max = durInt
                    songDuration.progress = player!!.currentPosition
                    if (initSecs + 1 == 60) {
                        initSecs = 0
                        initMins++
                    }
                    songNotify.updateTime(initMins, initSecs)
                    if (initSecs < 10) {
                        songTime.text = "0$initMins:0$initSecs"
                    } else {
                        songTime.text = "0$initMins:$initSecs"
                    }
                    initSecs++
            }
            override fun onFinish() {
                cancel()
                playNextSong()
            }
        }.start()
    }


    private fun cancelTimer(){
        timer?.cancel()
    }
    private fun resumeTimer(totalSeconds: Long, durInt: Int){
        startTimer(totalSeconds, durInt)
    }

    private fun playNextSong(){
        if(songIndex + 1 == songsList.size) songIndex = -1
        player?.stop()
        player?.release()
        playSong(songsList[++songIndex])
        play.setBackgroundResource(android.R.drawable.ic_media_pause)
    }
    private fun playPrevSong(){
        if(songIndex - 1 < 0) songIndex = songsList.size
        player?.stop()
        player?.release()
        playSong(songsList[--songIndex])
        play.setBackgroundResource(android.R.drawable.ic_media_pause)
    }


    companion object{
        var initSecs:Int = 0
        var initMins:Int = 0
    }
}
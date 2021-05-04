package com.example.mediaplayer

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class LyricsDialog(private var songTitle: String, private var lyrics: String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity).setTitle(songTitle).setMessage(lyrics).setPositiveButton("Close", null).create()
    }
}
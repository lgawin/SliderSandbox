package dev.lgawin.sandbox

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

@Composable
fun RingtoneSandbox(modifier: Modifier = Modifier.Companion) {
    Box(modifier) {
        val context = LocalContext.current
        Button(onClick = { context.testPlayingCustomRingtone() }) {
            Text("Play")
        }
    }
}

private fun Context.testPlayingCustomRingtone() {
    //    val uri = getResUri(R.raw.sunday_bloody)
    val uri = getResUri("raw/sunday_bloody")

    val duration = getRingtoneDuration(uri)
    Log.d("gawluk", "duration: $duration")

    val ringtone = RingtoneManager.getRingtone(this, uri)
        .apply { isLooping = false }

//    val ringtoneManager = RingtoneManager(this)
//    val index = ringtoneManager.getRingtonePosition(uri)


//    val attrs = ringtone.audioAttributes

    ringtone.play()

//    MediaPlayer.create(this, R.raw.sunday_bloody).apply {
//        isLooping = true
//        start()
//    }

//    MediaPlayer.create(this, uri).apply {
//        isLooping = true
//        start()
//    }

//    MediaPlayer().apply {
//        setDataSource(this@installCarFeature, uri)
//        prepare()
//        isLooping = true
//        start()
//    }
}

fun Context.getRingtoneDuration(ringtoneUri: Uri): Long {
    return MediaMetadataRetriever().use { mmr ->
        try {
            mmr.setDataSource(this, ringtoneUri)
            val durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            durationStr?.toLongOrNull() ?: 0L
        } catch (e: Exception) {
            e.printStackTrace()
            0L
        }
    }
}

private fun Context.getResUri(@RawRes id: Int): Uri =
    "android.resource://${packageName}/$id".toUri()

private fun Context.getResUri(resTypeAndName: String): Uri =
    "android.resource://${packageName}/${resTypeAndName}".toUri()

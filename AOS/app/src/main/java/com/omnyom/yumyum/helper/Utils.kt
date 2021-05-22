package com.omnyom.yumyum.helper

import android.content.ContentResolver
import android.content.Context
import android.graphics.ColorFilter
import android.net.Uri
import android.provider.OpenableColumns
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.LottieValueCallback
import java.io.File

fun ContentResolver.getFileName(uri: Uri): String {
    var name = ""
    val cursor = query(uri, null, null, null, null)
    cursor?.use {
        it.moveToFirst()
        name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }
    return name
}

fun LottieAnimationView.changeLayersColor(@ColorRes colorRes: Int) {
    val color = ContextCompat.getColor(context, colorRes)
    val filter = SimpleColorFilter(color)
    val keyPath = KeyPath("**")
    val callback: LottieValueCallback<ColorFilter> = LottieValueCallback(filter)

    addValueCallback(keyPath, LottieProperty.COLOR_FILTER, callback)
}

fun getVideoCacheDir(context: Context): File {
    return File(context.externalCacheDir, "video-cache")
}

fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}
package com.example.flyapp.ui.theme.components

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.View
import androidx.core.graphics.createBitmap
import androidx.core.graphics.get

fun getPixelColorFromView(view: View, x: Int, y: Int, context: Context, onResult: (Int) -> Unit) {
    val bitmap = createBitmap(view.width, view.height)
    val locationInWindow = IntArray(2)
    view.getLocationInWindow(locationInWindow)

    val window = (context as? Activity)?.window ?: return

    PixelCopy.request(window, bitmap, { result ->
        if (result == PixelCopy.SUCCESS) {
            if (x in 0 until bitmap.width && y in 0 until bitmap.height) {
                val pixelColor = bitmap[x, y]
                onResult(pixelColor)
            }
        }
    }, Handler(Looper.getMainLooper()))
}

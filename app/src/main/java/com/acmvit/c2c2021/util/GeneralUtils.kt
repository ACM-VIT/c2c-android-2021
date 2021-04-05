package com.acmvit.c2c2021.util

import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.*
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.FileProvider
import com.acmvit.c2c2021.BuildConfig
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.isConnected
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.functions.Consumer
import java.io.File


fun String.format(args: Array<Any>?) = args?.let { this.format(*it) }

fun downloadFile(url: String, name: String, context: Context, onError: Consumer<Throwable>): Long? {
    if (!isConnected) {
        onError.accept(NetworkException())
        return null
    }

    val path = "${name}.pdf"
    val request = DownloadManager.Request(Uri.parse(url))
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        .setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "${APP_EXTERNAL_STORAGE}/$path"
        )
        .setTitle(name)
        .setDescription("Downloading $path")
        .setRequiresCharging(false)
        .setAllowedOverMetered(true)
        .setAllowedOverRoaming(true)

    val downloadManager = getSystemService(context, DownloadManager::class.java)
    return downloadManager?.enqueue(request)
}

inline fun <T> Array<T>.apply(block: T.() -> Unit) {
    for (item in this) {
        item.block()
    }
}

@Suppress("DEPRECATION")
fun checkFileExists(path: String): File? {
    Log.d(
        "est",
        "checkFileExists: ${Environment.getExternalStorageDirectory()}/${APP_EXTERNAL_STORAGE}/${path}"
    )
    val file = File(
        "${
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            )
        }/${APP_EXTERNAL_STORAGE}/${path}"
    )
    return if (file.exists()) file else null
}

fun delayedExecute(delay: Long, block: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(block, delay)
}

fun showSnackbar(
    anchor: View,
    msg: String,
    actionName: String = "",
    action: () -> Unit = { },
    length: Int = Snackbar.LENGTH_SHORT) {

    Snackbar.make(anchor, msg, length)
        .setTextColor(ContextCompat.getColor(anchor.context, android.R.color.white))
        .setBackgroundTint(ContextCompat.getColor(anchor.context, R.color.colorSurfaceLight))
        .setActionTextColor(ContextCompat.getColor(anchor.context, R.color.primaryGreen))
        .setAction(actionName) { action() }
        .show()
}

fun Context.showAlertDialog(
    title: String,
    msg: String,
    todo: (isSuccessful: Boolean) -> Unit,
    positiveButton: String = "Yes",
    negativeButton: String = "No"
) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(msg)
        .setPositiveButton(positiveButton) { _, _ -> todo(true) }
        .setNegativeButton(negativeButton) { _, _ -> todo(false) }
        .show()
}

fun View.startAnimation(
    resId: Int,
    after: (anim: Animation?) -> Unit = {},
    before: (anim: Animation?) -> Unit = {},
    repeat: (anim: Animation?) -> Unit = {}
) {
    AnimationUtils.loadAnimation(context, resId).apply {
        setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                before(animation)
            }

            override fun onAnimationRepeat(animation: Animation?) {
                repeat(animation)
            }

            override fun onAnimationEnd(animation: Animation?) {
                after(animation)
            }
        })
        startAnimation(this)
    }
}

fun Context.openPdf(file: File) {
    if (!file.exists()) return

    val uri = FileProvider.getUriForFile(
        this,
        BuildConfig.APPLICATION_ID + ".provider",
        file
    )

    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(uri, "application/pdf")
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_CLEAR_TOP)

    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            this,
            "No app available to open the PDF",
            Toast.LENGTH_SHORT
        ).show()
    }
}


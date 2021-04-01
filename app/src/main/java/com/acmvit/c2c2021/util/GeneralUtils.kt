package com.acmvit.c2c2021.util

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

private const val TAG = "GeneralUtils"

const val EXTERNAL_STORAGE = "C2C 2021"

fun String.format(args: Array<Any>?) = args?.let { this.format(*it) }

fun downloadFile(url: String, name: String, context: Context): Long? {
    val fileName = "${name}.pdf"
    val request = DownloadManager.Request(Uri.parse(url))
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationInExternalPublicDir(EXTERNAL_STORAGE, fileName)
        .setTitle(name)
        .setDescription("Downloading $fileName")
        .setRequiresCharging(false)
        .setAllowedOverMetered(true)
        .setAllowedOverRoaming(true)

    val downloadManager = ContextCompat.getSystemService(context, DownloadManager::class.java)
    return downloadManager?.enqueue(request)
}

fun showSnackbar(anchor: View, msg: String, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(anchor, msg, length).show()
}


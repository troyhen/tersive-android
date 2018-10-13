package com.troy.tersive.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WebDoc(
    val url: String,
    val title: String,
    val author: String? = null,
    val beginning: String? = null,
    val ending: String? = null
) : Parcelable

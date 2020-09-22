package com.troy.tersive.ui.ext

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun Context.inflate(@LayoutRes res: Int, parent: ViewGroup? = null, attachToParent: Boolean = false): View = LayoutInflater.from(this).inflate(res, parent, attachToParent)

fun ViewGroup.inflate(@LayoutRes res: Int, attachToParent: Boolean = false): View = LayoutInflater.from(context).inflate(res, this, attachToParent)
fun ViewGroup.inflater(): LayoutInflater = LayoutInflater.from(context)
package io.ffem.reports.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result(
        val id: Int,
        val name: String?,
        val unit: String?,
        val result: String?,
        val resultValue: Double?
) : Parcelable
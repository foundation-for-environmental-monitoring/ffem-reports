package io.ffem.reports.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class TestInfo(
        var name: String? = null,
        var type: String? = null,
        var uuid: String? = null,
        var unit: String? = null,
        var resultSuffix: String? = "",
        var results: List<Result>? = ArrayList()
) : Parcelable
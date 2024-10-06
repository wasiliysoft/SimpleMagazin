package ru.wasiliysoft.simplemagazin.data

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class SimpleItem(
    @SerializedName("title") val title: String,
    @SerializedName("id") val id: String = UUID.randomUUID().toString(),
    @SerializedName("selected") val selected: Boolean = false,
    @SerializedName("isSuccess") val isSuccess: Boolean = false
)

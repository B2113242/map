package com.example.mymap.models

import android.icu.text.CaseMap.Title
import java.io.Serializable

data class UserMap(
    val title: String,
    val places: List<Place>
): Serializable

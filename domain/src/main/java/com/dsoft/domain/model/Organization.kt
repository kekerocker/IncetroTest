package com.dsoft.domain.model

data class Organization(
    val id: Int,
    val name: String,
    val photo: String,
    val rate: Double,
    val cuisines: List<String>,
    val isFavorite: Boolean,
    val distance: Float
)
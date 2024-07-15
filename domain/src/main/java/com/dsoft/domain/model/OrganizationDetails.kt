package com.dsoft.domain.model

import java.time.LocalDateTime
import java.time.LocalTime

data class OrganizationDetails(
    val id: Int,
    val name: String,
    val email: String,
    val categoryName: String,
    val detailedInfo: String,
    val photos: List<String>,
    val phones: List<String>,
    val urls: List<String>,
    val socials: List<Social>,
    val location: Location,
    val schedule: List<Schedule>,
    val rateCount: Int,
    val rate: Double,
    val averageCheck: List<Any>,
    val services: List<Any>,
    val serviceLanguages: List<String>,
    val cuisines: List<Any>,
    val reviewCount: Int,
    val review: Review,
    val distance: Float,
    val discount: Int,
    val isFavorite: Boolean
) {
    data class Location(
        val id: Int,
        val latitude: Float,
        val longitude: Float,
        val city: String,
        val address: String,
        val organization: Int,
        val district: Int
    )

    data class Social(
        val id: Int,
        val type: Int,
        val name: String,
        val url: String,
        val organization: Int
    )

    data class Schedule(
        val day: Int,
        val start: LocalTime,
        val end: LocalTime
    )

    data class Review(
        val organization: String,
        val user: User,
        val rate: Int,
        val text: String,
        val publicationDate: LocalDateTime
    )

    data class User(
        val email: String,
        val profile: Profile,
    )

    data class Profile(
        val id: Int,
        val email: String,
        val firstName: String,
        val lastName: String,
        val avatar: String,
        val organizations: List<Int>
    )
}

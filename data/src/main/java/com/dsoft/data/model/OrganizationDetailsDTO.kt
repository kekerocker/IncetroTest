package com.dsoft.data.model

data class OrganizationDetailsDTO(
    val id: Int?,
    val name: String?,
    val email: String?,
    val categoryName: String?,
    val detailedInfo: String?,
    val photos: List<String>?,
    val phones: List<String>?,
    val urls: List<String>?,
    val socials: List<SocialPayload>?,
    val location: LocationPayload?,
    val schedule: List<SchedulePayload>?,
    val rateCount: Int?,
    val rate: Double?,
    val averageCheck: List<Any>?,
    val services: List<Any>?,
    val serviceLanguages: List<String>?,
    val cuisines: List<Any>?,
    val reviewCount: Int?,
    val review: ReviewPayload?,
    val distance: Float?,
    val discount: Int?,
    val isFavorite: Boolean?
) {
    data class LocationPayload(
        val id: Int?,
        val latitude: Float?,
        val longitude: Float?,
        val city: String?,
        val address: String?,
        val organization: Int?,
        val district: Int?
    )

    data class SocialPayload(
        val id: Int?,
        val type: Int?,
        val name: String?,
        val url: String?,
        val organization: Int?
    )

    data class SchedulePayload(
        val day: Int?,
        val start: Long?,    //32400 seconds to LocalTime
        val end: Long ?      //79200 seconds to LocalTime
    )

    data class ReviewPayload(
        val organization: String?,
        val user: UserPayload?,
        val rate: Int?,
        val text: String?,
        val publicationDate: Float? // 1710159481.329515 timestamp to LDT
    )

    data class UserPayload(
        val email: String?,
        val profile: ProfilePayload?,
    )

    data class ProfilePayload(
        val id: Int?,
        val email: String?,
        val firstName: String?,
        val lastName: String?,
        val avatar: String?,
        val organizations: List<Int>?
    )
}

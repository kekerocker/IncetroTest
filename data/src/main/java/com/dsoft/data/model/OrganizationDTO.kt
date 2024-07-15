package com.dsoft.data.model

import com.google.gson.annotations.SerializedName

data class OrganizationDTO(
    @SerializedName("data") val data: List<OrganizationPayload>?
) {
    data class OrganizationPayload(
        @SerializedName("id") val id: Int?,
        @SerializedName("name") val name: String?,
        @SerializedName("photo") val photo: String?,
        @SerializedName("rate") val rate: Double?,
        @SerializedName("averageCheck") val averageCheck: List<Any>?,
        @SerializedName("cuisines") val cuisines: List<String>?,
        @SerializedName("isFavorite") val isFavorite: Boolean?,
        @SerializedName("distance") val distance: Float?
    )
}

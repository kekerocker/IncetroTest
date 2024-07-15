package com.dsoft.data.api

import com.dsoft.data.BuildConfig
import com.dsoft.data.model.OrganizationDTO
import com.dsoft.data.model.OrganizationDetailsDTO
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface NetworkApi {
    @GET("api/internship/organization/{id}/")
    suspend fun getOrganizationById(
        @Header("Authorization") token: String = BuildConfig.TOKEN,
        @Path("id") id: Int
    ): OrganizationDetailsDTO

    @GET("api/internship/organizations/category/1/organizations/")
    suspend fun getOrganizationsList(
        @Header("Authorization") token: String = BuildConfig.TOKEN
    ): OrganizationDTO

    @POST("api/internship/organization/{id}/favorite/")
    suspend fun addToFavorite(
        @Header("Authorization") token: String = BuildConfig.TOKEN,
        @Path("id") id: Int
    ) : Result<Any?>

    @DELETE("api/internship/organization/{id}/favorite/")
    suspend fun deleteFromFavorite(
        @Header("Authorization") token: String = BuildConfig.TOKEN,
        @Path("id") id: Int
    ) : Result<Any?>
}
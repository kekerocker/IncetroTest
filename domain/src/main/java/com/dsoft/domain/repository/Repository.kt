package com.dsoft.domain.repository

import com.dsoft.domain.model.Organization
import com.dsoft.domain.model.OrganizationDetails
import com.dsoft.domain.util.Response

interface Repository {
    suspend fun getOrganizationById(id: Int): Response<OrganizationDetails>

    suspend fun getOrganizations(): Response<List<Organization>>

    suspend fun addToFavorite(id: Int): Response<Boolean>

    suspend fun deleteFromFavorite(id: Int): Response<Boolean>
}
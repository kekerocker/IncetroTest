package com.dsoft.data.repository

import com.dsoft.domain.repository.Repository
import com.dsoft.data.api.NetworkApi
import com.dsoft.data.mapper.organizationDetailsMapper
import com.dsoft.data.mapper.organizationModelMapper
import com.dsoft.domain.model.Organization
import com.dsoft.domain.model.OrganizationDetails
import com.dsoft.domain.util.Response
import dagger.hilt.android.scopes.ActivityScoped
import okio.IOException
import javax.inject.Inject

@ActivityScoped
class RepositoryImpl @Inject constructor(
    private val api: NetworkApi
) : Repository {
    override suspend fun getOrganizationById(id: Int): Response<OrganizationDetails> {
        return try {
            val response = api.getOrganizationById(id = id)
            Response.Success(response.let(organizationDetailsMapper))
        } catch(e: Exception) {
            Response.Failure(IOException(e.message))
        }
    }

    override suspend fun getOrganizations(): Response<List<Organization>> {
        return try {
            val response = api.getOrganizationsList()
            Response.Success(response.let(organizationModelMapper))
        } catch(e: Exception) {
            Response.Failure(IOException(e.message))
        }
    }

    override suspend fun addToFavorite(id: Int): Response<Boolean> {
        return try {
            val response = api.addToFavorite(id = id)
            if (response.isSuccess) Response.Success(true)
            else Response.Failure(IOException("Unknown exception"))
        } catch(e:Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun deleteFromFavorite(id: Int): Response<Boolean> {
        try {
            val response = api.deleteFromFavorite(id = id)
            return if (response.isSuccess) Response.Success(true)
            else Response.Failure(IOException("Unknown exception"))
        } catch(e: Exception) {
            return Response.Failure(IOException(e))
        }

    }
}
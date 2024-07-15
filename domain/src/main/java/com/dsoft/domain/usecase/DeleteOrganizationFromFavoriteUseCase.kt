package com.dsoft.domain.usecase

import com.dsoft.domain.repository.Repository
import com.dsoft.domain.util.Response
import javax.inject.Inject

class DeleteOrganizationFromFavoriteUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(id: Int): Response<Boolean> {
        return repository.deleteFromFavorite(id)
    }
}
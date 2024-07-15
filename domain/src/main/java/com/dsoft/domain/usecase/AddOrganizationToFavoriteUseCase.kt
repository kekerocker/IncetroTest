package com.dsoft.domain.usecase

import com.dsoft.domain.repository.Repository
import com.dsoft.domain.util.Response
import javax.inject.Inject

class AddOrganizationToFavoriteUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(id: Int): Response<Boolean> {
        return repository.addToFavorite(id)
    }
}
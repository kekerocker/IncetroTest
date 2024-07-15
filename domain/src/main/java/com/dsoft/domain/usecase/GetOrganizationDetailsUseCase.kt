package com.dsoft.domain.usecase

import com.dsoft.domain.repository.Repository
import javax.inject.Inject

class GetOrganizationDetailsUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(id: Int) = repository.getOrganizationById(id)
}
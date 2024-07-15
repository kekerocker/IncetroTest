package com.dsoft.domain.usecase

import com.dsoft.domain.repository.Repository
import javax.inject.Inject

class GetOrganizationListUseCase @Inject constructor (
    private val repository: Repository
) {
    suspend operator fun invoke() = repository.getOrganizations()
}
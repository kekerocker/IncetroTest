package com.dsoft.incetrotest.ui.fragments.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsoft.domain.model.OrganizationDetails
import com.dsoft.domain.usecase.AddOrganizationToFavoriteUseCase
import com.dsoft.domain.usecase.DeleteOrganizationFromFavoriteUseCase
import com.dsoft.domain.usecase.GetOrganizationDetailsUseCase
import com.dsoft.domain.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class OrganizationDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getOrganizationDetailsUseCase: GetOrganizationDetailsUseCase,
    private val addOrganizationToFavoriteUseCase: AddOrganizationToFavoriteUseCase,
    private val deleteOrganizationFromFavoriteUseCase: DeleteOrganizationFromFavoriteUseCase
) : ViewModel() {

    private val _detailsState: MutableStateFlow<Response<OrganizationDetails>?> = MutableStateFlow(null)
    val detailsState: StateFlow<Response<OrganizationDetails>?> get() = _detailsState.asStateFlow()

    private val id: Int = checkNotNull(savedStateHandle["organization_id"])

    init {
        fetchOrganizationDetails(id)
    }

    private fun fetchOrganizationDetails(id: Int) {
        viewModelScope.launch {
            _detailsState.value = Response.Loading()
            _detailsState.value = getOrganizationDetailsUseCase(id)
        }
    }

    fun addToFavorite(id: Int) {
        viewModelScope.launch {
            _detailsState.value = Response.Loading()
            async { addOrganizationToFavoriteUseCase(id) }.await()
            fetchOrganizationDetails(id)
        }
    }

    fun deleteFromFavorite(id: Int) {
        viewModelScope.launch {
            _detailsState.value = Response.Loading()
            async { deleteOrganizationFromFavoriteUseCase(id) }.await()
            fetchOrganizationDetails(id)
        }
    }
}
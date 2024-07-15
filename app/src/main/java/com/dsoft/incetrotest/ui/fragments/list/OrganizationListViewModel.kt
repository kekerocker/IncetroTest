package com.dsoft.incetrotest.ui.fragments.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsoft.domain.model.Organization
import com.dsoft.domain.usecase.AddOrganizationToFavoriteUseCase
import com.dsoft.domain.usecase.DeleteOrganizationFromFavoriteUseCase
import com.dsoft.domain.usecase.GetOrganizationListUseCase
import com.dsoft.domain.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrganizationListViewModel @Inject constructor(
    private val getOrganizationListUseCase: GetOrganizationListUseCase,
    private val addOrganizationToFavoriteUseCase: AddOrganizationToFavoriteUseCase,
    private val deleteOrganizationFromFavoriteUseCase: DeleteOrganizationFromFavoriteUseCase,
) : ViewModel() {

    private val _organizationsList: MutableStateFlow<Response<List<Organization>>> = MutableStateFlow(Response.Success(emptyList()))
    val organizationList: StateFlow<Response<List<Organization>>> get() = _organizationsList.asStateFlow()

    private val _filterFavorite: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val filterFavorite: StateFlow<Boolean> get() = _filterFavorite.asStateFlow()

    fun fetchOrganizationsList() {
        viewModelScope.launch {
            _organizationsList.value = Response.Loading()
            _organizationsList.value = getOrganizationListUseCase.invoke()
        }
    }

    fun switchFavoriteState() {
        _filterFavorite.value = !filterFavorite.value
    }

    fun addToFavorite(id: Int) {
        viewModelScope.launch {
            _organizationsList.value = Response.Loading()
            async { addOrganizationToFavoriteUseCase(id) }.await()
            fetchOrganizationsList()
        }
    }

    fun deleteFromFavorite(id: Int) {
        viewModelScope.launch {
            _organizationsList.value = Response.Loading()
            async { deleteOrganizationFromFavoriteUseCase(id) }.await()
            fetchOrganizationsList()
        }

    }
}
package org.fasheep.fair.feature.assignment

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.fasheep.fair.core.data.repository.CardRoomRepository
import javax.inject.Inject

const val TAG = "AssignmentVM"

@HiltViewModel
class AssignmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _roles = mutableStateListOf<RoleVM>()
    val roles: List<RoleVM>
        get() = _roles

    private val _names = mutableStateListOf<String>()
    val names: List<String>
        get() = _names

    fun addRole(roleVM: RoleVM) {
        _roles.add(roleVM)
    }

    fun assign() {

    }

    fun addName(name: String) {
        _names.add(name)
    }
}

data class RoleVM(val name: String, val num: Int)

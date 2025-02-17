package org.fasheep.fair.feature.assignment

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.fasheep.fair.core.blockchain.EthereumRepository
import org.fasheep.fair.core.blockchain.model.Role
import org.fasheep.fair.core.network.GraphRepository
import javax.inject.Inject

const val TAG = "AssignmentVM"

@HiltViewModel
class AssignmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val ethereumRepository: EthereumRepository,
    private val graphRepository: GraphRepository
) : ViewModel() {
    private val _roles = mutableStateListOf<RoleVM>()
    val roles: List<RoleVM>
        get() = _roles

    init {
        Log.d(TAG, "VM: init")
    }
    private val _names = mutableStateListOf<String>()
    val names: List<String>
        get() = _names

    fun addRole(roleVM: RoleVM): Boolean {
        if (_roles.contains(roleVM) || roleVM.name.isBlank()) return false
        _roles.add(roleVM)
        return true
    }

    fun addName(name: String): Boolean {
        if (_names.contains(name) || name.isBlank()) return false
        _names.add(name)
        return true
    }

    fun assign(callback: (String) -> Unit) {
        viewModelScope.launch {
            val hash = ethereumRepository.assignRole(names, roles.map { Role(it.name, it.num) })
            if (hash == null) return@launch
            delay(3000)
            var temp = graphRepository.findAssignmentById(hash)
            for (i in 1..10) {
                if (temp != null) break
                delay(1000)
                temp = graphRepository.findAssignmentById(hash)
            }
            println(temp)
            callback(hash) // Navigation
        }
    }
}

data class RoleVM(val name: String, val num: Int)

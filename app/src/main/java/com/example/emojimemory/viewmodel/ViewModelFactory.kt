package com.example.emojimemory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.emojimemory.data.repository.CategoriaRepository
import com.example.emojimemory.data.repository.PartidaRepository
import com.example.emojimemory.data.repository.UsuarioRepository

// O Android não sabe criar um ViewModel com parâmetros no construtor.
// Esta Factory entrega os Repositories certos para cada ViewModel.
class ViewModelFactory(
    private val partidaRepository: PartidaRepository,
    private val categoriaRepository: CategoriaRepository,
    private val usuarioRepository: UsuarioRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(JogoViewModel::class.java) ->
            JogoViewModel(partidaRepository, categoriaRepository, usuarioRepository) as T
        modelClass.isAssignableFrom(AdminViewModel::class.java) ->
            AdminViewModel(partidaRepository, usuarioRepository) as T
        else -> throw IllegalArgumentException("ViewModel desconhecido")
    }
}

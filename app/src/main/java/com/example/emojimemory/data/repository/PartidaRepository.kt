package com.example.emojimemory.data.repository

import com.example.emojimemory.data.local.Partida
import com.example.emojimemory.data.local.PartidaDAO
import kotlinx.coroutines.flow.Flow

// Único ponto que fala com o PartidaDAO. O ViewModel só conhece o Repository.
class PartidaRepository(private val dao: PartidaDAO) {

    fun buscarTodos(): Flow<List<Partida>> = dao.buscarTodos()

    // devolve null se deu certo, ou a mensagem de erro
    suspend fun inserir(partida: Partida): String? {
        return try {
            dao.inserir(partida)
            null
        } catch (e: Exception) {
            "Não foi possível salvar a partida."
        }
    }
}

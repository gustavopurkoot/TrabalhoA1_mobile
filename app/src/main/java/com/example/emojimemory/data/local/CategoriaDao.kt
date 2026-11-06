package com.example.emojimemory.data.local

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDAO {

    @Insert
    suspend fun inserir(categoria: Categoria)

    @Query("SELECT * FROM categorias")
    fun buscarTodos(): Flow<List<Categoria>>

    // usado para não cadastrar a mesma categoria duas vezes
    @Query("SELECT COUNT(*) FROM categorias WHERE nome = :nome")
    suspend fun contarPorNome(nome: String): Int
}

package com.example.emojimemory.data.local

import androidx.room3.Entity
import androidx.room3.PrimaryKey

// Entidade 1: usuário cadastrado no app (jogador ou administrador).
@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val email: String,
    val senha: String,
    val admin: Boolean = false
)

// Entidade 2: uma partida terminada. Guarda o id do usuário e o nome na hora da partida.
@Entity(tableName = "partidas")
data class Partida(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val usuarioId: Int,
    val jogador: String,
    val pontuacao: Int,
    val tentativas: Int,
    val categoria: String
)

// Entidade 3: categoria de emojis. O jogo sorteia uma a cada partida.
// Os emojis ficam em um único texto, separados por espaço.
@Entity(tableName = "categorias")
data class Categoria(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val emojis: String
)

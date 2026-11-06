package com.example.emojimemory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.emojimemory.data.local.AppDatabase
import com.example.emojimemory.data.remote.EmojiApi
import com.example.emojimemory.data.repository.CategoriaRepository
import com.example.emojimemory.data.repository.PartidaRepository
import com.example.emojimemory.data.repository.UsuarioRepository
import com.example.emojimemory.ui.EmojiMemoryTheme
import com.example.emojimemory.ui.TelaPrincipal
import com.example.emojimemory.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // monta o banco, a API, os Repositories e a Factory aqui, para nenhuma tela tocar no DAO
        val db = AppDatabase.getDatabase(this)
        val partidaRepo = PartidaRepository(db.partidaDAO())
        val categoriaRepo = CategoriaRepository(db.categoriaDAO(), EmojiApi.service)
        val usuarioRepo = UsuarioRepository(db.usuarioDAO())
        val factory = ViewModelFactory(partidaRepo, categoriaRepo, usuarioRepo)

        setContent {
            EmojiMemoryTheme {
                TelaPrincipal(factory)
            }
        }
    }
}

package com.example.emojimemory.data.local

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase

@Database(entities = [Usuario::class, Partida::class, Categoria::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDAO(): UsuarioDAO
    abstract fun partidaDAO(): PartidaDAO
    abstract fun categoriaDAO(): CategoriaDAO

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "emoji_memory_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

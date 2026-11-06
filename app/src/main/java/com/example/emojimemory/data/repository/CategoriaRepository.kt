package com.example.emojimemory.data.repository

import com.example.emojimemory.data.local.Categoria
import com.example.emojimemory.data.local.CategoriaDAO
import com.example.emojimemory.data.remote.EmojiApiService
import kotlinx.coroutines.flow.Flow

// Resposta da busca na API: ou vem a lista de emojis, ou vem a mensagem de erro
data class ResultadoEmojis(
    val emojis: List<String> = emptyList(),
    val erro: String? = null
)

// Categorias que já vêm com o app (funcionam sem internet)
private val CATEGORIAS_PADRAO = listOf(
    Categoria(nome = "Animais", emojis = "🐶 🐱 🐭 🐹 🐰 🦊 🐻 🐼 🐨 🐯"),
    Categoria(nome = "Comidas", emojis = "🍎 🍌 🍇 🍓 🍕 🍔 🍟 🍩 🍪 🍦"),
    Categoria(nome = "Esportes", emojis = "⚽ 🏀 🏈 ⚾ 🎾 🏐 🏉 🎱 🏓 🥊"),
    Categoria(nome = "Veículos", emojis = "🚗 🚕 🚌 🚓 🚑 🚒 🚜 🚲 🚂 🚀"),
    Categoria(nome = "Natureza", emojis = "🌵 🌲 🌴 🌸 🌻 🍀 🍄 🌈 🌙 🔥"),
    Categoria(nome = "Carinhas", emojis = "😀 😂 😍 😎 😴 😡 😱 🤔 🥳 😇")
)

// Categorias extras buscadas na API: nome no app -> grupo na API
private val GRUPOS_API = listOf(
    "Animais (API)" to "animals-and-nature",
    "Comidas (API)" to "food-and-drink",
    "Viagens (API)" to "travel-and-places",
    "Atividades (API)" to "activities"
)

// Fala com o banco (CategoriaDAO) e com a internet (EmojiApiService).
// Nenhuma falha de banco ou de rede escapa daqui: tudo é tratado com try-catch.
class CategoriaRepository(
    private val dao: CategoriaDAO,
    private val api: EmojiApiService
) {

    fun buscarTodos(): Flow<List<Categoria>> = dao.buscarTodos()

    // Cadastra as categorias locais que ainda não existem no banco
    suspend fun prepararCategorias(): String? {
        return try {
            for (categoria in CATEGORIAS_PADRAO) {
                if (dao.contarPorNome(categoria.nome) == 0) {
                    dao.inserir(categoria)
                }
            }
            null
        } catch (e: Exception) {
            "Não foi possível carregar as categorias."
        }
    }

    // Tenta acrescentar mais categorias vindas da API (Retrofit).
    // Sem internet, o jogo continua funcionando com as categorias locais.
    suspend fun importarCategoriasDaApi(): String? {
        return try {
            var erro: String? = null
            for ((nome, grupo) in GRUPOS_API) {
                if (dao.contarPorNome(nome) > 0) continue

                val resultado = buscarEmojisDaApi(grupo)
                if (resultado.erro != null) {
                    erro = resultado.erro
                    break   // falhou: não adianta tentar as outras agora
                }
                dao.inserir(Categoria(nome = nome, emojis = resultado.emojis.joinToString(" ")))
            }
            erro
        } catch (e: Exception) {
            "Não foi possível salvar as categorias da API."
        }
    }

    private suspend fun buscarEmojisDaApi(grupo: String): ResultadoEmojis {
        return try {
            val emojis = api.buscarPorCategoria(grupo)
                .filter { it.unicode.size == 1 }          // só emojis simples (1 código)
                .map { converterParaEmoji(it.unicode[0]) }
                .distinct()
                .shuffled()
                .take(10)

            if (emojis.size < 8) {
                ResultadoEmojis(erro = "A API não devolveu emojis suficientes.")
            } else {
                ResultadoEmojis(emojis = emojis)
            }
        } catch (e: Exception) {
            ResultadoEmojis(erro = "Sem conexão com a API de emojis: usando só as categorias do app.")
        }
    }

    // "U+1F436" -> número 0x1F436 -> caractere 🐶
    private fun converterParaEmoji(codigo: String): String {
        val numero = codigo.removePrefix("U+").toInt(16)
        return String(Character.toChars(numero))
    }
}

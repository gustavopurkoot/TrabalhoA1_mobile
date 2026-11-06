package com.example.emojimemory.data.repository

import com.example.emojimemory.data.local.Usuario
import com.example.emojimemory.data.local.UsuarioDAO
import kotlinx.coroutines.flow.Flow

// Resposta do login: ou vem o usuário (null = nome/senha errados), ou vem a mensagem de erro
data class ResultadoLogin(
    val usuario: Usuario? = null,
    val erro: String? = null
)

// Único ponto que fala com o UsuarioDAO.
// As funções que escrevem devolvem null (deu certo) ou o texto do erro para a tela.
class UsuarioRepository(private val dao: UsuarioDAO) {

    fun buscarTodos(): Flow<List<Usuario>> = dao.buscarTodos()

    suspend fun login(nome: String, senha: String): ResultadoLogin {
        return try {
            ResultadoLogin(usuario = dao.buscarLogin(nome, senha))
        } catch (e: Exception) {
            ResultadoLogin(erro = "Erro ao acessar o banco de dados.")
        }
    }

    // CREATE: não deixa criar dois usuários com o mesmo nome
    suspend fun inserir(usuario: Usuario): String? {
        return try {
            if (dao.buscarPorNome(usuario.nome) != null) {
                "Esse nome de usuário já existe."
            } else {
                dao.inserir(usuario)
                null
            }
        } catch (e: Exception) {
            "Não foi possível criar a conta."
        }
    }

    // UPDATE: o nome pode continuar igual, mas não pode ser o de outro usuário
    suspend fun atualizar(usuario: Usuario): String? {
        return try {
            val existente = dao.buscarPorNome(usuario.nome)
            if (existente != null && existente.id != usuario.id) {
                "Esse nome de usuário já está em uso."
            } else {
                dao.atualizar(usuario)
                null
            }
        } catch (e: Exception) {
            "Não foi possível atualizar o usuário."
        }
    }

    // UPDATE do próprio perfil: busca o usuário atual, troca nome/e-mail e só troca a senha se vier uma nova
    suspend fun atualizarPerfil(id: Int, nome: String, email: String, novaSenha: String): String? {
        return try {
            val atual = dao.buscarPorId(id)
            if (atual == null) {
                "Usuário não encontrado."
            } else {
                atualizar(
                    atual.copy(
                        nome = nome,
                        email = email,
                        senha = if (novaSenha.isBlank()) atual.senha else novaSenha
                    )
                )
            }
        } catch (e: Exception) {
            "Não foi possível atualizar o perfil."
        }
    }

    // Cria o administrador padrão (admin / admin) se ele ainda não existir
    suspend fun garantirAdmin(): String? {
        return try {
            if (dao.buscarPorNome("admin") == null) {
                dao.inserir(
                    Usuario(nome = "admin", email = "admin@email.com", senha = "admin", admin = true)
                )
            }
            null
        } catch (e: Exception) {
            "Não foi possível preparar o administrador."
        }
    }
}

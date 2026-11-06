# Emoji Memory

Jogo da memória com emojis para Android, feito em **Kotlin + Jetpack Compose**, seguindo a arquitetura **MVVM** vista em sala (base: Projeto MAF 4).

## O que o app faz

- **Login e Criar Conta:** os usuários ficam salvos no Room. Existe um administrador padrão: usuário `admin`, senha `admin`.
- **Início:** mostra os pontos do jogador e o botão Jogar.
- **Jogo:** tabuleiro fixo 4x4 (16 cartas). A categoria de emojis é sorteada automaticamente a cada partida. Cada par vale +100 pontos e cada erro tira 10.
- **Resultado:** pontuação e tentativas da partida (toda partida é salva).
- **Perfil:** estatísticas, conquistas, últimas partidas e botão **Sair da Conta**.
- **Painel ADM (só administrador):** Últimas Partidas e Gestão de Usuários (editar nome, e-mail e senha).

## Estrutura de pastas

```
app/src/main/java/com/example/emojimemory/
├── MainActivity.kt
├── data/
│   ├── local/        Models.kt (Usuario, Partida, Categoria), UsuarioDao.kt, PartidaDao.kt, CategoriaDao.kt, AppDatabase.kt
│   ├── remote/       EmojiApi.kt (Retrofit)
│   └── repository/   UsuarioRepository.kt, PartidaRepository.kt, CategoriaRepository.kt
├── viewmodel/        JogoViewModel.kt, AdminViewModel.kt, ViewModelFactory.kt
└── ui/               Rotas, AppNavigation, TelaPrincipal, BottomNavigation, Componentes, Cores, Tema
                      TelaLogin, TelaCriarConta, TelaInicio, TelaJogo, TelaResultado, TelaPerfil, TelaAdmin
```

## Requisitos da A1

| Requisito | Onde está |
|---|---|
| Padrão MVVM, 4 pacotes (ui, data.local, data.repository, data.remote) | pastas acima |
| 2 ViewModels, cada um com seu `UiState` (data class) | `JogoViewModel.kt`, `AdminViewModel.kt` |
| `ViewModelFactory` injetando os Repositories | `ViewModelFactory.kt`, `MainActivity.kt` |
| Tratamento de exceções com try-catch no Repository | os 3 Repositories (o erro chega no `UiState`) |
| Entidades Room (mínimo 2) | `Models.kt`: `Usuario`, `Partida`, `Categoria` |
| Flow nos DAOs, coletado no ViewModel | `buscarTodos()` nos DAOs, `init` dos ViewModels |
| CRUD | Usuario: criar (`TelaCriarConta`), listar e editar (`TelaAdmin`). Partida: criada no jogo e listada no admin e no perfil |
| Retrofit | `EmojiApi.kt`, `CategoriaRepository.importarCategoriasDaApi` (categorias extras da API EmojiHub) |
| 7 telas e Bottom Navigation | `AppNavigation.kt`, `BottomNavigation.kt` |
| Lógica Kotlin com `filter`, `map`, `sortedBy` | `JogoViewModel`, `AdminViewModel`, `BottomNavigation`, `TelaPerfil` |
| LazyColumn / LazyVerticalGrid | `TelaAdmin`, `TelaPerfil`, `TelaJogo` |
| Bifurcação de perfil (Jogador x Admin) | campo `admin` do `Usuario`, aba Admin em `BottomNavigation`, `TelaAdmin` |

## Como abrir no Android Studio

1. Extraia o ZIP.
2. Em **Open**, escolha a pasta `Emoji_Memory_A1` (a que contém `settings.gradle.kts`).
3. Aguarde o Gradle Sync.
4. Se o app já estava instalado no celular/emulador, **desinstale antes** (o banco mudou).
5. Rode em um emulador ou celular com depuração USB.

O app funciona sem internet com 6 categorias já incluídas. Com internet, na primeira abertura ele acrescenta mais 4 categorias vindas da API.

## Prints e vídeo

_(adicionar aqui os prints das telas e o vídeo curto do app funcionando)_

## Integrantes

_(adicionar os nomes e os usuários do GitHub de todos os integrantes)_

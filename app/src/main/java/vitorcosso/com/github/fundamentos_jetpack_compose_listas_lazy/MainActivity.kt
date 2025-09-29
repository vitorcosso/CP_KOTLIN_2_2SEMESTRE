package vitorcosso.com.github.fundamentos_jetpack_compose_listas_lazy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vitorcosso.com.github.fundamentos_jetpack_compose_listas_lazy.components.GameCard
import vitorcosso.com.github.fundamentos_jetpack_compose_listas_lazy.components.StudioCard
import vitorcosso.com.github.fundamentos_jetpack_compose_listas_lazy.model.Game
import vitorcosso.com.github.fundamentos_jetpack_compose_listas_lazy.repository.getAllGames
import vitorcosso.com.github.fundamentos_jetpack_compose_listas_lazy.repository.getGamesByStudio
import vitorcosso.com.github.fundamentos_jetpack_compose_listas_lazy.ui.theme.FundamentosjetpackcomposelistaslazyTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FundamentosjetpackcomposelistaslazyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GamesScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun GamesScreen(modifier: Modifier = Modifier) {
    // Instancia o estado que guarda o texto digitado no campo de busca.
    var textInserted by remember { mutableStateOf("") }

    // Instancia o estado que guarda a lista atualmente exibida na tela.
    // Tambem instancia a lista de jogos que sera exibida na tela
    var listaDeJogos by remember { mutableStateOf(getAllGames()) }

    Column(modifier = modifier.padding(16.dp)) {
        // Cabeçalho/título da tela.
        Text(
            text = "Meus jogos favoritos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Filtro entra em atividade quando clicado.
        OutlinedTextField(
            value = textInserted,
            onValueChange = { textInserted = it }, // Quanto o texto muda, muda tambem o filtro dinamicamente
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Nome do estúdio") },
            trailingIcon = {
                IconButton(onClick = {
                    listaDeJogos = getGamesByStudio(textInserted) // realiza o filtro, passando o texto como inserido no filtro como parametro
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = ""
                    )
                }
            }
        )

        // Botão de limpar o filtro
        // O botão de limpar filtros é exposto qnd há texto no presente campo
        // E também quando a lista atual é diferente do que está presente no repository (getAllGames())
        if (textInserted.isNotEmpty() || listaDeJogos != getAllGames()) {
            Text(
                text = "Limpar filtro",
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .clickable {
                        // Quando clicado, limpa o campo
                        textInserted = "" // reseta o campo com uma string vazia
                        listaDeJogos = getAllGames()
                    },
                fontWeight = FontWeight.SemiBold, // fonte da texto de limpar filtros
                color = androidx.compose.ui.graphics.Color.Blue // Aqui coloca a cor do limpar filtro
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        // Aplica um loop usando items() Utilidade exclusiva do lazy row
        // e nesse loop, para cada jogo da lista listaDeJogos ele cria um car
        // com as informacoes do jogo
        LazyRow() {
            items(listaDeJogos) { jogo ->
                StudioCard(
                    game = jogo,
                    onClick = {
                        // Clique no estúdio: reflete no campo de busca e aplica o filtro correspondente.
                        textInserted = jogo.studio
                        listaDeJogos = getGamesByStudio(jogo.studio)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostra a lista com o filtro (seja fazia, todos, alguns ou nenhum).
        LazyColumn() {
            items(listaDeJogos) { game ->
                GameCard(game = game)
            }
        }
    }
}

/**
 * Previews para visualizar a tela e os componentes no Android Studio, sem rodar a app.
 */
@Preview(showBackground = true, name = "Games Screen Preview")
@Composable
fun PreviewGamesScreen() {
    FundamentosjetpackcomposelistaslazyTheme {
        GamesScreen()
    }
}

@Preview(showBackground = true, name = "Studio Card Preview")
@Composable
fun PreviewStudioCard() {
    FundamentosjetpackcomposelistaslazyTheme {
        StudioCard(game = Game(1, "Example Game", "Example Studio", 2023))
    }
}

@Preview(showBackground = true, name = "Game Card Preview")
@Composable
fun PreviewGameCard() {
    FundamentosjetpackcomposelistaslazyTheme {
        GameCard(game = Game(1, "Example Game", "Example Studio", 2023))
    }
}

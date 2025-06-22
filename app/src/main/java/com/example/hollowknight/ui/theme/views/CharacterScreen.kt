package com.example.hollowknight.ui.theme.views

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hollowknight.R
import com.example.hollowknight.data.Character
import com.example.hollowknight.network.BASE_URL

@Composable
fun CharacterScreen(
    characterViewModel: CharacterViewModel = viewModel(),
    onCharacterClick: (Character) -> Unit
) {
    val uiState by characterViewModel.uiState.collectAsState()
    when (uiState) {
        is CharacterUiState.Loading -> LoadingScreen()
        is CharacterUiState.Success -> {
            val characters = (uiState as CharacterUiState.Success).characters
            CharacterList(characters, onCharacterClick)
        }
        is CharacterUiState.Error -> ErrorScreen()
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Carregando personagens...")
    }
}

@Composable
fun ErrorScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Erro ao carregar personagens")
    }
}
@Composable
fun CharacterList(
    characters: List<Character>,
    onCharacterClick: (Character) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF0F0F1A), Color(0xFF1C1C2E))
            )))
        {
        Image(
            painter = painterResource(id = R.drawable.fundo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.1f
        )
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(2)
        ) {
            items(characters) { character ->
                CharacterEntry(character = character, onClick = { onCharacterClick(character) })
            }
        }
    }
}

@Composable
fun CharacterEntry(
    character: Character,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFF5C90E8), RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C2E)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color(0xFF1C1C2E))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(BASE_URL.trimEnd('/') + character.thumbnailImage)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.placeholder),
                contentDescription = character.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
            )

            Text(
                text = character.name ?: "Sem nome",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

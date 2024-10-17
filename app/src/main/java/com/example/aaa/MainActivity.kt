package com.example.aaa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeManagerApp()
        }
    }
}

@Composable
fun RecipeManagerApp() {
    var recipes by remember { mutableStateOf(listOf<Recipe>()) }

    Column(modifier = Modifier.padding(16.dp)) {
        RecipeInput { recipe ->
            recipes = recipes + recipe
        }
        RecipeList(recipes) { recipe ->
            recipes = recipes.filter { it != recipe }
        }
    }
}

@Composable
fun RecipeInput(onAddRecipe: (Recipe) -> Unit) {
    var name by remember { mutableStateOf("") }
    var preparationTime by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre del plato") }
        )
        TextField(
            value = preparationTime,
            onValueChange = { preparationTime = it },
            label = { Text("Tiempo de preparación") }
        )
        TextField(
            value = ingredients,
            onValueChange = { ingredients = it },
            label = { Text("Ingredientes principales (separados por comas)") }
        )
        TextField(
            value = calories,
            onValueChange = { calories = it },
            label = { Text("Calorías por porción") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("URL de imagen") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (name.isNotBlank() && preparationTime.isNotBlank() && ingredients.isNotBlank() && calories.isNotBlank() && imageUrl.isNotBlank()) {
                onAddRecipe(
                    Recipe(
                        name = name,
                        preparationTime = preparationTime,
                        ingredients = ingredients.split(","),
                        calories = calories.toInt(),
                        imageUrl = imageUrl
                    )
                )
                // Limpiar los campos
                name = ""
                preparationTime = ""
                ingredients = ""
                calories = ""
                imageUrl = ""
            }
        }) {
            Text("Agregar Receta")
        }
    }
}

@Composable
fun RecipeList(recipes: List<Recipe>, onDeleteRecipe: (Recipe) -> Unit) {
    LazyColumn {
        items(recipes.size) { index ->
            RecipeItem(recipe = recipes[index], onDeleteRecipe = onDeleteRecipe)
        }
    }
}

@Composable
fun RecipeItem(recipe: Recipe, onDeleteRecipe: (Recipe) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = recipe.name, style = MaterialTheme.typography.titleLarge)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(1f)) {
                LazyRow {
                    item {
                        Text("Tiempo: ${recipe.preparationTime} min")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Calorías: ${recipe.calories}")
                    }
                }
                Text("Ingredientes:")
                LazyRow {
                    items(recipe.ingredients.size) { index ->
                        Text(recipe.ingredients[index])
                        if (index < recipe.ingredients.size - 1) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(",")
                        }
                    }
                }
            }

            // Botón de eliminar alineado a la derecha
            Button(onClick = { showDialog = true }) {
                Text("Eliminar")
            }
        }

        Image(
            painter = rememberAsyncImagePainter(recipe.imageUrl),
            contentDescription = "Imagen del plato",
            modifier = Modifier.size(128.dp)
        )

        // Diálogo de confirmación
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Eliminar Receta") },
                text = { Text("¿Estás seguro de que deseas eliminar esta receta?") },
                confirmButton = {
                    Button(onClick = {
                        onDeleteRecipe(recipe)
                        showDialog = false
                    }) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

package com.taipt.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.toLowerCase
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.taipt.pokedex.pokemondetail.PokemonDetailScreen
import com.taipt.pokedex.pokemonlist.PokemonListScreen
import com.taipt.pokedex.ui.theme.PokedexTheme
import com.taipt.pokedex.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedexTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Constants.POKEMON_LIST_SCREEN
                ) {
                    composable(Constants.POKEMON_LIST_SCREEN) {
                        PokemonListScreen(navController = navController)
                    }
                    composable("${Constants.POKEMON_DETAIL_SCREEN}/{${Constants.DOMINANT_COLOR}}/{${Constants.POKEMON_NAME}}",
                        arguments = listOf(
                            navArgument(Constants.DOMINANT_COLOR) {
                                type = NavType.IntType
                            },
                            navArgument(Constants.POKEMON_NAME) {
                                type = NavType.StringType
                            }
                        )
                    ) {
                        val dominantColor = remember {
                            val color = it.arguments?.getInt(Constants.DOMINANT_COLOR)
                            color?.let { Color(it) } ?: Color.White
                        }
                        val pokemonName = remember {
                            it.arguments?.getString(Constants.POKEMON_NAME)
                        }
                        PokemonDetailScreen(
                            dominantColor = dominantColor,
                            pokemonName = pokemonName?.lowercase() ?: "",
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

package com.alekhin.beetea.onboarding.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alekhin.beetea.onboarding.screen.Greeting
import com.alekhin.beetea.onboarding.screen.OnBoardingScreen

@ExperimentalFoundationApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String
    ) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Welcome.route) {
            OnBoardingScreen(
                navController = navController
            )
        }
        composable(route = Screen.Home.route) {
            Greeting("Android")
        }
    }
}
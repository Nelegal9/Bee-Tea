package com.alekhin.beetea

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.alekhin.beetea.onboarding.navigation.SetupNavGraph
import com.alekhin.beetea.splash.viewmodel.SplashViewModel
import com.alekhin.beetea.onboarding.viewmodel.viewModelFactory
import com.alekhin.beetea.ui.theme.BeeTeaTheme

@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {
    lateinit var splashViewModel: SplashViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { !splashViewModel.isLoading.value } // FIXME: The onboarding flashes for a brief moment after completing onboarding process.
        setContent {
            BeeTeaTheme {
                splashViewModel = viewModel<SplashViewModel>(
                    factory = viewModelFactory { SplashViewModel(MyApplication.mainModule.provideDataStoreRepository) }
                )
                val navController = rememberNavController()
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    SetupNavGraph(
                        navController = navController,
                        startDestination = splashViewModel.startDestination.value
                    )
                }
            }
        }
    }
}
package com.alekhin.beetea

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.alekhin.beetea.R.string.app_name
import com.alekhin.beetea.ui.theme.BeeTeaTheme

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            BeeTeaTheme {
                Scaffold(
                    topBar = { TopAppBar(title = { Text(text = stringResource(id = app_name)) }) },
                    bottomBar = { /* TODO: Add bottom bar. */ },
                    floatingActionButton = { /* TODO: Add extended floating action button. */  }
                ) { paddingValues ->
                    Text(text = stringResource(id = app_name), modifier = Modifier.padding(paddingValues))
                }
            }
        }
    }
}
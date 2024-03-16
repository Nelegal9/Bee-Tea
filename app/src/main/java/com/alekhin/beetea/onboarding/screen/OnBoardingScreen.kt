package com.alekhin.beetea.onboarding.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.alekhin.beetea.MyApplication
import com.alekhin.beetea.onboarding.util.OnBoardingPage
import com.alekhin.beetea.onboarding.viewmodel.OnBoardingViewModel
import com.alekhin.beetea.onboarding.navigation.Screen
import com.alekhin.beetea.onboarding.viewmodel.viewModelFactory

@ExperimentalFoundationApi
@Composable
fun OnBoardingScreen(navController: NavHostController, onBoardingViewModel: OnBoardingViewModel = viewModel<OnBoardingViewModel>(
    factory = viewModelFactory { OnBoardingViewModel(MyApplication.mainModule.provideDataStoreRepository) }
)) {
    val pages = listOf(
        OnBoardingPage.FirstPage,
        OnBoardingPage.SecondPage,
        OnBoardingPage.ThirdPage
    )
    val pagerState = rememberPagerState { pages.size }

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(10f),
            verticalAlignment = Alignment.Top
        ) { position ->
            OnBoardingPager(onBoardingPage = pages[position])
        }
        HorizontalPagerIndicator(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(1f),
            pagerState = pagerState
        )
        FinishButton(
            modifier = Modifier.weight(1f),
            pagerState = pagerState
        ) {
            onBoardingViewModel.saveOnBoardingState(completed = true)
            navController.popBackStack()
            navController.navigate(Screen.Home.route)
        }
    }
}

@Composable
fun OnBoardingPager(onBoardingPage: OnBoardingPage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.7f),
            painter = painterResource(id = onBoardingPage.image),
            contentDescription = "Pager Image"
        )
        Text(
            modifier =  Modifier.fillMaxWidth(),
            text = onBoardingPage.title,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .padding(top = 20.dp),
            text = onBoardingPage.description,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )
    }
}

@ExperimentalFoundationApi
@Composable
fun HorizontalPagerIndicator(modifier: Modifier, pagerState: PagerState) {
    Row(
        modifier
            .wrapContentHeight()
            .fillMaxWidth()

            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(8.dp)
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun FinishButton(
    modifier: Modifier,
    pagerState: PagerState,
    onCLick: () -> Unit
) {
    Row(
        modifier = modifier.padding(horizontal = 40.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        AnimatedVisibility(
            visible = pagerState.currentPage >= 2,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(
                onClick = onCLick,
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White
                )
            ) {
                Text(text = "Finish")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FirstOnBoardingPagePreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        OnBoardingPager(onBoardingPage = OnBoardingPage.FirstPage)
    }
}

@Preview(showBackground = true)
@Composable
fun SecondOnBoardingPagePreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        OnBoardingPager(onBoardingPage = OnBoardingPage.SecondPage)
    }
}

@Preview(showBackground = true)
@Composable
fun ThirdOnBoardingPagePreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        OnBoardingPager(onBoardingPage = OnBoardingPage.ThirdPage)
    }
}
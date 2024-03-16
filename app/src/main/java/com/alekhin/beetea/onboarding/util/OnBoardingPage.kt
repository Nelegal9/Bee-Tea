package com.alekhin.beetea.onboarding.util

import androidx.annotation.DrawableRes
import com.alekhin.beetea.R

sealed class OnBoardingPage(
    @DrawableRes val image: Int,
    val title: String,
    val description: String
) {
    data object FirstPage : OnBoardingPage(
        image = R.drawable.ic_launcher_foreground,
        title = "Title 1",
        description = "Description 1"
    )

    data object SecondPage : OnBoardingPage(
        image = R.drawable.ic_launcher_foreground,
        title = "Title 2",
        description = "Description 2"
    )

    data object ThirdPage : OnBoardingPage(
        image = R.drawable.ic_launcher_foreground,
        title = "Title 3",
        description = "Description 3"
    )
}
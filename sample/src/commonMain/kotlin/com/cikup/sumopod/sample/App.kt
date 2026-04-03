package com.cikup.sumopod.sample

import androidx.compose.runtime.Composable
import com.cikup.sumopod.sample.navigation.AppNavigation
import com.cikup.sumopod.sample.theme.SumopodTheme

@Composable
fun App() {
    SumopodTheme {
        AppNavigation()
    }
}

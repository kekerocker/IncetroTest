package com.dsoft.incetrotest.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dsoft.incetrotest.R
import com.dsoft.incetrotest.ui.theme.backgroundMain

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundMain),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(text = stringResource(id = R.string.loading))
            Text(text = stringResource(id = R.string.wait))
        }
    }
}
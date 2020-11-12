package com.troy.tersive.ui.admin

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.ui.tooling.preview.Preview
import com.troy.tersive.ui.base.AppTheme

@Composable
fun AdminMenuPage() {
//todo broken in Koin 2.2.0    val viewModel: AdminMenuViewModel = getViewModel()  //by viewModel()
    val viewModel: AdminMenuViewModel = viewModel()
    AppTheme {
        Scaffold(topBar = { AppBar() }) {
            AdminMenu(viewModel)
        }
    }
}

@Composable
private fun AppBar() {
    TopAppBar(
        title = {
            Text("Admin Menu")
        },
    )
}

@Composable
@Preview(showBackground = true)
fun AdminMenu(viewModel: AdminMenuViewModel? = null) {
    Column(modifier = Modifier.fillMaxSize().padding(30.dp), verticalArrangement = Arrangement.spacedBy(30.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedButton(onClick = { viewModel?.onEditClick() }) {
            Text(text = "Edit Database")
        }
        OutlinedButton(onClick = { viewModel?.onUploadClick() }) {
            Text(text = "Upload Database")
        }
    }
}

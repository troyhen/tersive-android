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
import androidx.ui.tooling.preview.Preview
import com.troy.tersive.ui.base.AppTheme
import org.koin.androidx.compose.getViewModel

@Composable
fun AdminMenuPage() {
    val viewModel: AdminMenuViewModel = getViewModel()  //by viewModel()
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

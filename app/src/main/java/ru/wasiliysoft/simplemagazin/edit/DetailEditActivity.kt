package ru.wasiliysoft.simplemagazin.edit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import ru.wasiliysoft.simplemagazin.data.SimpleItem
import ru.wasiliysoft.simplemagazin.ui.theme.AppTheme

class DetailEditActivity : ComponentActivity() {

    private val viewModel: DetailEditViewModel by lazy {
        val fa = ViewModelProvider.AndroidViewModelFactory(this.application)
        ViewModelProvider(this, fa)[DetailEditViewModel::class.java]
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Scaffold(
                    modifier = Modifier.imePadding(),
//                    topBar = {
//                        TopAppBar(
//                            title = { },
//                            actions = { },
//                            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
//                        )
//                    },
                    content = { paddingValues ->
                        Surface(
                            modifier = Modifier.padding(paddingValues),
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {
                                var text by rememberSaveable { mutableStateOf("") }
                                OutlinedTextField(
                                    value = text,
                                    onValueChange = { text = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1.0f),
                                )

                                Row(Modifier.fillMaxWidth()) {
                                    Spacer(modifier = Modifier.weight(1.0f))
                                    Button(onClick = {
                                        viewModel.addItem(SimpleItem(title = text))
                                        finish()
                                    }) {
                                        Text("Complete")
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }

}
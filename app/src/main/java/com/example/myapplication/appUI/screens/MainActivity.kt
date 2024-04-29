package com.example.myapplication.appUI.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.appUI.components.ServicesWindow
import com.example.myapplication.data.Service
import com.example.myapplication.network.getServices
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController: NavHostController = rememberNavController()
                    MyApp(nav = navController)
                }
            }
        }
    }
}//main initializes the new navigation control in firebase content,
//By default, navigation prioritizes the login page, and logs in to the homescreen.
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MyApp(nav:NavHostController) {
    val loginViewModel: LoginViewModel = rememberLoginViewModel()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("NaveLink")
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Bottom app bar",
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ){innerPadding->
        NavHost(navController = nav, startDestination = "LoginScreen") {
            composable("HomeScreen") {
                HomeScreen(innerPadding)
            }
            composable("LoginScreen") {
                LoginScreen(loginViewModel,nav)
            }
        }

    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleOutlinedTextFieldSample() {
    var text by remember { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = { text = it },
    )
}

@Composable
fun HomeScreen(innerPadding:PaddingValues) {
    Column(
        modifier = Modifier
            .padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {}/*{
                SimpleOutlinedTextFieldSample()
                Button(onClick = { /* handle search click here */ }) {
                    Text("Search")
                }
            }*/
        LoadServices()
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadServices(){
    val serviceState = remember { mutableStateOf<List<Service>>(emptyList()) }
    var keywordState = remember {
        mutableStateOf("")
    }
    LaunchedEffect(true) {
        //LaunchedEffect for handling errors
        getServices { serviceList ->
            serviceList?.let {
                // state gets updated when data == available()
                serviceState.value = it
            }
        }
    }

    val filteredServices = serviceState.value.filter { service ->
        service.keywords.contains(keywordState.value, ignoreCase = true)}
    Column {
        // Add a TextField for user input
        TextField(
            value = keywordState.value,
            onValueChange = { keywordState.value = it },
            label = { Text("Enter keyword") },
            modifier = Modifier.padding(16.dp)
        )
        ServicesWindow(filteredServices)
    }
}







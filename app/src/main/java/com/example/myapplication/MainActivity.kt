package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class MainActivity : ComponentActivity() {
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
                    LoginScreen(viewModel)
                }
            }
        }
    }
}

class LoginViewModel : ViewModel() {
    var username: String by mutableStateOf("")
    var password: String by mutableStateOf("")

    fun onUsernameChange(newValue: String) {
        username = newValue
    }

    fun onPasswordChange(newValue: String) {
        password = newValue
    }

    fun onLoginClick() {
        // 处理登录逻辑，可以与服务器通信等
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login",style = TextStyle(
            fontSize = 24.sp, // Set the font size to make it larger
            fontWeight = FontWeight.Bold, // Make it bold
            color = Color.Blue // Change the text color if desired
        )
        )
        TextField(
            value = viewModel.username,
            onValueChange = viewModel::onUsernameChange,
            label = { Text("Username") }
        )
        TextField(
            value = viewModel.password,
            onValueChange = viewModel::onPasswordChange,
            visualTransformation = PasswordVisualTransformation(), // 使用PasswordVisualTransformation
            label = { Text("Password") }
        )
        Button(onClick = viewModel::onLoginClick) {
            Text(text = "Login")
        }
    }
}

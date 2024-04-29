package com.example.myapplication.appUI.screens

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


@Composable
fun rememberLoginViewModel(): LoginViewModel {
    return viewModel {
        LoginViewModel()
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

    fun onLoginClick(onLoginResult: (Boolean, String?) -> Unit) {
        // get users' email and passwords
        val email = username
        val userPassword = password

        // Call login logic
        performLogin(email, userPassword) { success, errorMessage ->
            if (success) {
                // success log in，operation after successful execution
                onLoginResult(true, null)
            } else {
                // failed log in，display an error message or perform an action after a failure
                onLoginResult(false, errorMessage)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel,navController: NavHostController) {
    var errorMessage by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Blue
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
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Password") }
        )
        Button(onClick = { viewModel.onLoginClick { success, errorMsg ->
            if (success) {
                // Log in successfully, perform the operations after success, and navigate to the next interface.
                navController.navigate("HomeScreen")
            } else {
                // Login fails, displays error message or performs actions after failure
                //
                errorMessage = errorMsg ?: "Login failed"
            }
        } }) {
            Text(text = "Login")
        }
        Text(
            text = errorMessage,
            color = Color.Red, // 错误消息的颜色可以根据需要调整
            fontWeight = FontWeight.Bold
        )
    }
}

fun performLogin(email: String, password: String, onLoginResult: (Boolean, String?) -> Unit) {
    val auth = FirebaseAuth.getInstance()

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // login success
                onLoginResult(true, null)
            } else {
                // login fails
                val errorMessage = task.exception?.message
                onLoginResult(false, errorMessage)
            }
        }
}

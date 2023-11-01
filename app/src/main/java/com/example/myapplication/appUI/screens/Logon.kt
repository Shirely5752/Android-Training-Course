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
        // 获取用户输入的邮箱和密码
        val email = username
        val userPassword = password

        // 调用登录逻辑
        performLogin(email, userPassword) { success, errorMessage ->
            if (success) {
                // 登录成功，执行成功后的操作
                onLoginResult(true, null)
            } else {
                // 登录失败，显示错误消息或执行失败后的操作
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
                // 登录成功，执行成功后的操作，例如导航到下一个界面
                navController.navigate("HomeScreen")
            } else {
                // 登录失败，显示错误消息或执行失败后的操作
                // errorMessage 包含登录失败的错误消息
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
                // 登录成功
                onLoginResult(true, null)
            } else {
                // 登录失败
                val errorMessage = task.exception?.message
                onLoginResult(false, errorMessage)
            }
        }
}

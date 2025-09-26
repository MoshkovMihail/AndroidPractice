package com.example.androidpractice


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class FirstActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Column {
                    val currentText = remember{mutableStateOf("")}

                    TextField(
                        value = currentText.value,
                        onValueChange = { currentText.value = it },
                    )

                    RowWithTwoButtons(currentText.value)
                }

            }
        }

    }





    @Composable
    private fun RowWithTwoButtons(text: String) {
        Spacer(modifier = Modifier.height(3.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                val intent = Intent(this@FirstActivity, SecondActivity::class.java)
                if (text.isNotEmpty()) {
                    intent.putExtra("text", text)
                }
                startActivity(intent)
            }) {
                Text(text = getString(R.string.SecondActivity))
            }

            Spacer(modifier = Modifier.width(14.5.dp))

            Button(onClick = {
                val intent = Intent(this@FirstActivity, ThirdActivity::class.java)
                if (text.isNotEmpty()) {
                    intent.putExtra("text", text)
                }
                startActivity(intent)

            }) {
                Text(text = getString(R.string.ThirdActivity))
            }
        }
    }
}
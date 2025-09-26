package com.example.androidpractice

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

class ThirdActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val myText = intent.getStringExtra("text") ?: getString(R.string.default3)

            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Column {
                    Text(text = myText, textAlign = TextAlign.Center, modifier = Modifier.width(261.dp) .height(50.dp) .background(Color.LightGray) .wrapContentHeight())

                    MyButton()
                }

            }
        }
    }

@Composable
private fun MyButton() {
    Spacer(modifier = Modifier.height(3.dp))

    Button(onClick = {
        val intent = Intent(this, FirstActivity::class.java)
        startActivity(intent)

    }, modifier = Modifier.width(261.dp)) {
        Text(getString(R.string.FirstActivity))
    }
}
}
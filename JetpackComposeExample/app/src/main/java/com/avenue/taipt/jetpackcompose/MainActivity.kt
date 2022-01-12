package com.avenue.taipt.jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.avenue.taipt.jetpackcompose.ui.theme.JetpackComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // WIDTH + HEIGHT //
//        setContent {
//            Row(
//                modifier = Modifier
//                    .width(200.dp)
//                    .fillMaxHeight(0.7f)
//                    .background(Color.Green),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceAround
//            ) {
//                Text(text = "Hello")
//                Text(text = "World")
//                Text(text = "!!!!!")
//            }
//        }


        // DRAW BORDER AND OFFSET
//        setContent {
//            Column(
//                modifier = Modifier
//                    .background(Color.Green)
//                    .fillMaxHeight(0.7f)
//                    .fillMaxWidth()
//                    .padding(top = 32.dp)
//                    .border(5.dp, Color.Magenta)
//                    .padding(5.dp)
//                    .border(5.dp, Color.Blue)
//                    .padding(5.dp)
//                    .border(10.dp, Color.Red)
//                    .padding(10.dp)
//                    .width(600.dp)
//                    .requiredWidth(300.dp)
//            ) {
//                Text(text = "Hello", modifier = Modifier.offset(0.dp, 20.dp))
//                Text(text = "Hello", modifier = Modifier
//                    .border(5.dp, Color.Yellow)
//                    .padding(5.dp)
//                    .offset(20.dp, 20.dp)
//                    .border(10.dp, Color.Cyan)
//                    .padding(10.dp))
//                Text(text = "Hello")
//                Spacer(modifier = Modifier.height(50.dp))
//                Text(text = "World")
//                Text(text = "!!!!!")
//            }
//        }

        // Card: Image with Text and Gradient color //
//        setContent {
//            val painter = painterResource(id = R.drawable.cat)
//            val description = "A cute cat"
//            val title = "A cute white cat"
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth(0.5f)
//                    .padding(16.dp)
//            ) {
//                ImageCard(
//                    painter = painter,
//                    contentDescription = description,
//                    title = title
//                )
//            }
//        }

        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF101010))
            ) {
                Text(
                    text = "Jetpack Compose",
                    color = Color.White,
                    fontSize = 30.sp
                )
            }
        }
    }
}

@Composable
fun ImageCard(
    painter: Painter,
    contentDescription: String,
    title: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp
    ) {
        Box(
            modifier = Modifier.height(200.dp)
        ) {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = 300f
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    text = title,
                    style = TextStyle(color = Color.White, fontSize = 16.sp)
                )
            }
        }
    }
}

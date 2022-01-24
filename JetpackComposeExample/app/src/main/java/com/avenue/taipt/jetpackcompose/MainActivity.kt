package com.avenue.taipt.jetpackcompose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.avenue.taipt.jetpackcompose.ui.theme.JetpackComposeTheme
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //--- part 1 + 2 - WIDTH + HEIGHT ---//
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


        //--- part 3 - DRAW BORDER AND OFFSET ---//
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

        //--- part 4 - CARD: Image with Text and Gradient color ---//
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

        // FONT FAMILY
        val fontFamily = FontFamily(
            Font(R.font.oxygen_bold, FontWeight.Bold),
            Font(R.font.oxygen_light, FontWeight.Thin),
            Font(R.font.oxygen_regular, FontWeight.Normal)
        )

        //--- part 5: STYLING TEXT ---//
//        setContent {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color(0xFF101010))
//                    .padding(16.dp)
//            ) {
//                Text(
//                    text = buildAnnotatedString {
//                        withStyle(
//                            style = SpanStyle(
//                                color = Color.Green,
//                                fontSize = 50.sp
//                            )
//                        ) {
//                            append("J")
//                        }
//                        append("etpack ")
//                        withStyle(
//                            style = SpanStyle(
//                                color = Color.Green,
//                                fontSize = 50.sp
//                            )
//                        ) {
//                            append("C")
//                        }
//                        append("ompose")
//                    },
//                    color = Color.White,
//                    fontSize = 30.sp,
//                    fontFamily = fontFamily,
//                    fontWeight = FontWeight.Bold,
//                    fontStyle = FontStyle.Italic,
//                    textAlign = TextAlign.Center,
//                    textDecoration = TextDecoration.Underline
//                )
//            }
//        }

        //--- part 6: STATE ---//
//        setContent {
//            Column(Modifier.fillMaxSize()) {
//                val color = remember {
//                    mutableStateOf(Color.Yellow)
//                }
//
//                ColorBox(
//                    Modifier
//                        .weight(1f)
//                        .fillMaxSize()
//                ) {
//                    color.value = it
//                }
//                Box(
//                    modifier = Modifier
//                        .background(color.value)
//                        .weight(1f)
//                        .fillMaxSize()
//                )
//            }
//        }

        //--- part 7 - Textfields, Buttons & Showing Snackbars ---//
//        setContent {
//            val scaffoldState = rememberScaffoldState()
//            var textFieldState by remember {
//                mutableStateOf("")
//            }
//            val scope = rememberCoroutineScope()
//
//            Scaffold(
//                modifier = Modifier.fillMaxSize(),
//                scaffoldState = scaffoldState
//            ) {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center,
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(30.dp)
//                ) {
//                    TextField(
//                        value = textFieldState,
//                        label = {
//                            Text(text = "Enter your name")
//                        },
//                        onValueChange = {
//                            textFieldState = it
//                        },
//                        singleLine = true,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Button(onClick = {
//                        scope.launch {
//                            scaffoldState.snackbarHostState.showSnackbar("Hello $textFieldState")
//                        }
//                    }) {
//                        Text(text = "Please greet me!")
//                    }
//                }
//            }
//        }

        //--- part 8 - List ---//
//        setContent {
//            LazyColumn {
//
//                items(5000) {
//                    Text(
//                        text = "Item $it",
//                        fontSize = 24.sp,
//                        fontWeight = FontWeight.Bold,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 24.dp)
//                    )
//                }
//
//                itemsIndexed(
//                    listOf("Pham", "The", "Tai")
//                ) { index, string ->
//                    Text(
//                        text = string,
//                        fontSize = 24.sp,
//                        fontWeight = FontWeight.Bold,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 24.dp)
//                    )
//                }
//            }
//        }

        //--- part 9 - Constraint Layout ---//
//        setContent {
//            val constraints = ConstraintSet {
//                val greenBox = createRefFor("greenbox")
//                val redBox = createRefFor("redbox")
//                val guideline = createGuidelineFromTop(0.5f)
//
//                constrain(greenBox) {
//                    top.linkTo(guideline)
//                    start.linkTo(parent.start)
//                    width = Dimension.value(100.dp)
//                    height = Dimension.value(100.dp)
//                }
//                constrain(redBox) {
//                    top.linkTo(parent.top)
//                    start.linkTo(greenBox.end)
//                    end.linkTo(parent.end)
//                    width = Dimension.value(100.dp)
//                    height = Dimension.value(100.dp)
//                }
//                createHorizontalChain(greenBox, redBox, chainStyle = ChainStyle.Spread)
//            }
//            ConstraintLayout(constraintSet = constraints, modifier = Modifier.fillMaxSize()) {
//                Box(modifier = Modifier
//                    .background(Color.Green)
//                    .layoutId("greenbox")
//                )
//                Box(modifier = Modifier
//                    .background(Color.Red)
//                    .layoutId("redbox")
//                )
//            }
//        }

        //--- part 10: Side Effects & Effect Handlers ---//
//        setContent {
//
//        }
    }
}

@Preview
@Composable
fun ImageCardPreview() {
    Button(onClick = { /*TODO*/ }) {
        Text(text = "Click me")
    }
}

@Preview
@Composable
fun ImageCardPreview2() {
    Button(onClick = { /*TODO*/ }) {
        Text(text = "Click me")
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

@Composable
fun ColorBox(
    modifier: Modifier = Modifier,
    updateColor: (Color) -> Unit
) {

    Box(modifier = modifier
        .background(Color.Red)
        .clickable {
            updateColor(
                Color(
                    Random.nextFloat(),
                    Random.nextFloat(),
                    Random.nextFloat(),
                    1f
                )
            )
        }
    )
}

@Composable
fun MyComposable() {
    Button(onClick = { /*TODO*/ }) {
        Text(text = "Click me")
    }
}
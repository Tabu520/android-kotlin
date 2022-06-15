package com.avenue.taipt.jetpackcompose

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.avenue.taipt.jetpackcompose.ui.BottomNavItem
import com.avenue.taipt.jetpackcompose.ui.Navigation
import com.avenue.taipt.jetpackcompose.ui.theme.BottomNavWithBadgesTheme
import com.avenue.taipt.jetpackcompose.ui.theme.ComposeParallaxScrollTheme
import com.avenue.taipt.jetpackcompose.ui.theme.JetpackComposeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.roundToInt
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
//            val scaffoldState = rememberScaffoldState()
//            val scope = rememberCoroutineScope()
//
//            Scaffold(scaffoldState = scaffoldState) {
//                var counter = produceState(initialValue = 0) {
//                    delay(3000L)
//                    value = 4
//                }
//                if (counter.value > 0 && counter.value % 5 == 0) {
//                    LaunchedEffect(key1 = scaffoldState.snackbarHostState) {
//                        scaffoldState.snackbarHostState.showSnackbar("Hello")
//                    }
//                }
//                Button(onClick = {  }) {
//                    Text(text = "Click me - ${counter.value}")
//                }
//            }
//        }

        //--- part 11: Simple Animations ---//
//        setContent {
//            var sizeState by remember { mutableStateOf(200.dp) }
//            val size by animateDpAsState(
//                targetValue = sizeState,
//                tween(
//                    durationMillis = 3000,
//                    delayMillis = 300,
//                    easing = FastOutSlowInEasing
//                )
//                spring(
//                    Spring.DampingRatioHighBouncy
//                )
//                keyframes {
//                    durationMillis = 5000
//                    sizeState at 0 with LinearEasing
//                    sizeState * 1.5f at 1000 with FastOutLinearInEasing
//                    sizeState * 2f at 5000
//                }
//            )
//            val infiniteTransition = rememberInfiniteTransition()
//            val color by infiniteTransition.animateColor(
//                initialValue = Color.Red,
//                targetValue = Color.Green,
//                animationSpec = infiniteRepeatable(
//                    tween(durationMillis = 2000),
//                    repeatMode = RepeatMode.Reverse
//                )
//            )
//            Box(
//                modifier = Modifier
//                    .size(size)
//                    .background(color),
//                contentAlignment = Alignment.Center
//            ) {
//                Button(onClick = {
//                    sizeState += 50.dp
//                }) {
//                    Text(text = "Increase Size")
//                }
//            }
//        }

        //--- part 12: Animated Circular Progress Bar ---//
//        setContent {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressBar(percentage = 0.8f, number = 100)
//            }
//        }

        //--- part 13: Draggable Music Knob ---//
//        setContent {
//            Box(
//                contentAlignment = Alignment.Center,
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color(0xFF101010))
//            ) {
//                Row(
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier
//                        .border(1.dp, Color.Green, RoundedCornerShape(10.dp))
//                        .padding(30.dp)
//                ) {
//                    var volume by remember {
//                        mutableStateOf(0f)
//                    }
//                    val barCount = 20
//                    MusicKnob(
//                        modifier = Modifier.size(100.dp)
//                    ) {
//                        volume = it
//                    }
//                    Spacer(modifier = Modifier.width(20.dp))
//                    VolumeBar(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(30.dp),
//                        activeBars = (barCount * volume).roundToInt(),
//                        barCount = barCount
//                    )
//                }
//            }
//        }

        //--- part 16: 3D Animated Drop Down ---//
//        setContent {
//            Surface(
//                color = Color(0xFF101010),
//                modifier = Modifier.fillMaxSize()
//            ) {
//                DropDown(
//                    text = "Hello World!",
//                    modifier = Modifier.padding(15.dp)
//                ) {
//                    Text(
//                        text = "This is now revealed!",
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(100.dp)
//                            .background(Color.Green)
//                    )
//                }
//            }
//        }

        // Navigation
//        setContent {
//            Navigation()
//        }

        // Bottom Navigation
//        setContent {
//            BottomNavWithBadgesTheme {
//                val navController = rememberNavController()
//                Scaffold(
//                    bottomBar = {
//                        BottomNavigationBar(
//                            items = listOf(
//                                BottomNavItem(
//                                    name = "Home",
//                                    route = "home",
//                                    icon = Icons.Default.Home
//                                ),
//                                BottomNavItem(
//                                    name = "Chat",
//                                    route = "chat",
//                                    icon = Icons.Default.Notifications,
//                                    badgeCount = 20
//                                ),
//                                BottomNavItem(
//                                    name = "Settings",
//                                    route = "settings",
//                                    icon = Icons.Default.Settings,
//                                    badgeCount = 1
//                                )
//                            ),
//                            navController = navController,
//                            onItemClick = {
//                                navController.navigate(it.route)
//                            }
//                        )
//                    }
//                ) {
//                    MyBottomNavigation(navHostController = navController)
//                }
//            }
//        }

        // Multi-Layer Parallax Scroll Effect
        setContent {
            ComposeParallaxScrollTheme {

                val moonScrollSpeed = 0.08f
                val midBgScrollSpeed = 0.03f

                val imageHeight = (LocalConfiguration.current.screenHeightDp * (2f / 3f)).dp

                var moonOffset by remember {
                    mutableStateOf(0f)
                }
                var midBgOffset by remember {
                    mutableStateOf(0f)
                }

                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {

                }
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
fun MyComposable(backPressedDispatcher: OnBackPressedDispatcher) {
    val callback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Do something
            }
        }
    }
    DisposableEffect(key1 = backPressedDispatcher) {
        backPressedDispatcher.addCallback(callback)
        onDispose {
            callback.remove()
        }
    }
    Button(onClick = { /*TODO*/ }) {
        Text(text = stringResource(R.string.click_me))
    }
}

@Composable
fun CircularProgressBar(
    percentage: Float,
    number: Int,
    fontSize: TextUnit = 28.sp,
    radius: Dp = 50.dp,
    color: Color = Color.Green,
    strokeWidth: Dp = 8.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val currentPercentage = animateFloatAsState(
        targetValue = if (animationPlayed) percentage else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = animDelay
        )
    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(radius * 2f)
    ) {
        Canvas(modifier = Modifier.size(radius * 2f)) {
            drawArc(
                color = color,
                -90f,
                360 * currentPercentage.value,
                false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        Text(
            text = (currentPercentage.value * number).toInt().toString(),
            color = Color.Black,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun VolumeBar(
    modifier: Modifier = Modifier,
    activeBars: Int = 0,
    barCount: Int = 10
) {
    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        val barWidth = remember {
            constraints.maxWidth / (2f * barCount)
        }
        Canvas(modifier = modifier) {
            for (i in 0 until barCount) {
                drawRoundRect(
                    color = if (i in 0..activeBars) Color.Green else Color.Gray,
                    topLeft = Offset(i * barWidth * 2f + barWidth / 2f, 0f),
                    size = Size(barWidth, constraints.maxHeight.toFloat()),
                    cornerRadius = CornerRadius(0f)
                )
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun MusicKnob(
    modifier: Modifier = Modifier,
    limitingAngle: Float = 25f,
    onValueChange: (Float) -> Unit
) {
    var rotation by remember {
        mutableStateOf(limitingAngle)
    }
    var touchX by remember {
        mutableStateOf(0f)
    }
    var touchY by remember {
        mutableStateOf(0f)
    }
    var centerX by remember {
        mutableStateOf(0f)
    }
    var centerY by remember {
        mutableStateOf(0f)
    }

    Image(
        painter = painterResource(id = R.drawable.music_knob),
        contentDescription = stringResource(R.string.music_knob),
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned {
                val windowBounds = it.boundsInWindow()
                centerX = windowBounds.size.width / 2f
                centerY = windowBounds.size.height / 2f
            }
            .pointerInteropFilter { event ->
                touchX = event.x
                touchY = event.y
                val angle = -atan2(centerX - touchX, centerY - touchY) * (180f / PI).toFloat()

                when (event.action) {
                    MotionEvent.ACTION_DOWN,
                    MotionEvent.ACTION_MOVE -> {
                        if (angle !in -limitingAngle..limitingAngle) {
                            val fixedAngle = if (angle in -180f..-limitingAngle) {
                                360f + angle
                            } else {
                                angle
                            }
                            rotation = fixedAngle

                            val percent = (fixedAngle - limitingAngle) / (360f - 2 * limitingAngle)
                            onValueChange(percent)
                            true
                        } else false
                    }
                    else -> false
                }
            }
            .rotate(rotation)
    )
}

@Composable
fun DropDown(
    text: String,
    modifier: Modifier = Modifier,
    initiallyOpened: Boolean = false,
    content: @Composable () -> Unit
) {
    var isOpen by remember {
        mutableStateOf(initiallyOpened)
    }
    val alpha = animateFloatAsState(
        targetValue = if (isOpen) 1f else 0f,
        animationSpec = tween(
            durationMillis = 300
        )
    )
    val rotateX = animateFloatAsState(
        targetValue = if (isOpen) 0f else -90f,
        animationSpec = tween(
            durationMillis = 300
        )
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = text, color = Color.White, fontSize = 16.sp)
            Icon(imageVector = Icons.Default.ArrowDropDown,
                contentDescription = stringResource(R.string.open_or_close_drop_down),
                tint = Color.White,
                modifier = Modifier
                    .clickable {
                        isOpen = !isOpen
                    }
                    .scale(1f, if (isOpen) -1f else 1f)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    transformOrigin = TransformOrigin(0.5f, 0f)
                    rotationX = rotateX.value
                }
                .alpha(alpha = alpha.value)
        ) {
            content()
        }
    }
}

@Composable
fun MyBottomNavigation(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = stringResource(R.string.home)) {
        composable(route = "home") {
            HomeScreen()
        }
        composable(route = "chat") {
            ChatScreen()
        }
        composable(route = "settings") {
            SettingsScreen()
        }
    }
}

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(
        modifier = modifier,
        backgroundColor = Color.DarkGray,
        elevation = 5.dp
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            BottomNavigationItem(
                selected = selected,
                onClick = {
                    onItemClick(item)
                },
                selectedContentColor = Color.Green,
                unselectedContentColor = Color.Gray,
                icon = {
                    Column(
                        horizontalAlignment = CenterHorizontally
                    ) {
                        if (item.badgeCount > 0) {
                            BadgedBox(
                                badge = {
                                    Text(
                                        text = item.badgeCount.toString(),
                                        color = Color.Red,
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.name
                                )
                            }
                        } else {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.name
                            )
                        }
                        if (selected) {
                            Text(
                                text = item.name,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Home screen")
    }
}

@Composable
fun ChatScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Chat screen")
    }
}

@Composable
fun SettingsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Settings screen")
    }
}
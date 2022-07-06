package com.avenue.taipt.jetpackcompose

import android.Manifest
import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalContext
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
import androidx.constraintlayout.compose.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.avenue.taipt.jetpackcompose.destinations.PostScreenDestination
import com.avenue.taipt.jetpackcompose.destinations.ProfileScreenDestination
import com.avenue.taipt.jetpackcompose.model.MenuItem
import com.avenue.taipt.jetpackcompose.model.MyListItem
import com.avenue.taipt.jetpackcompose.model.User
import com.avenue.taipt.jetpackcompose.ui.*
import com.avenue.taipt.jetpackcompose.ui.theme.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.roundToInt
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // region Part 1 + 2
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
        //endregion

        //region part 3
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
        //endregion

        //region Part 4
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
        //endregion

        // FONT FAMILY
        val fontFamily = FontFamily(
            Font(R.font.oxygen_bold, FontWeight.Bold),
            Font(R.font.oxygen_light, FontWeight.Thin),
            Font(R.font.oxygen_regular, FontWeight.Normal)
        )

        //region Part 5
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
        //endregion

        //region Part 6
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
        //endregion

        //region Part 7
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
        //endregion

        //region Part 8
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
        //endregion

        //region Part 9
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
        //endregion

        //region Part 10
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
        //endregion

        //region Part 11
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
        //endregion

        //region Part 12
        //--- part 12: Animated Circular Progress Bar ---//
//        setContent {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressBar(percentage = 0.8f, number = 100)
//            }
//        }
        //endregion

        //region Part 13
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
        //endregion

        //region Part 16
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
        //endregion

        //region Navigation
        // Navigation
//        setContent {
//            Navigation()
//        }
        //endregion

        //region Bottom Navigation
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
        //endregion

        //region Multi-Layer Parallax Scroll Effect
//        setContent {
//            ComposeParallaxScrollTheme {
//
//                val moonScrollSpeed = 0.08f
//                val midBgScrollSpeed = 0.03f
//
//                val imageHeight = (LocalConfiguration.current.screenHeightDp * (2f / 5f)).dp
//                val lazyListState = rememberLazyListState()
//
//                var moonOffset by remember {
//                    mutableStateOf(0f)
//                }
//                var midBgOffset by remember {
//                    mutableStateOf(0f)
//                }
//
//                val nestedScrollConnection = object : NestedScrollConnection {
//                    override fun onPreScroll(
//                        available: Offset,
//                        source: NestedScrollSource
//                    ): Offset {
//                        val delta = available.y
//                        val layoutInfo = lazyListState.layoutInfo
//                        // Check if the first item is visible
//                        if (lazyListState.firstVisibleItemIndex == 0) {
//                            return Offset.Zero
//                        }
//                        if (layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1) {
//                            return Offset.Zero
//                        }
//                        moonOffset += delta * moonScrollSpeed
//                        midBgOffset += delta * midBgScrollSpeed
//                        return Offset.Zero
//                    }
//                }
//
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .nestedScroll(nestedScrollConnection),
//                    state = lazyListState
//                ) {
//                    items(10) {
//                        Text(
//                            text = "Sample item",
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp)
//                        )
//                    }
//
//                    item {
//                        Box(
//                            modifier = Modifier
//                                .clipToBounds()
//                                .fillMaxWidth()
//                                .height(imageHeight + midBgOffset.toDp())
//                                .background(
//                                    Brush.verticalGradient(
//                                        listOf(
//                                            Color(0xFFf36b21),
//                                            Color(0xFFf9a521),
//                                        )
//                                    )
//                                )
//                        ) {
//                            Image(
//                                painter = painterResource(id = R.drawable.ic_moonbg),
//                                contentDescription = "moon",
//                                contentScale = ContentScale.FillWidth,
//                                alignment = BottomCenter,
//                                modifier = Modifier
//                                    .matchParentSize()
//                                    .graphicsLayer {
//                                        translationY = moonOffset
//                                    }
//                            )
//                            Image(
//                                painter = painterResource(id = R.drawable.ic_midbg),
//                                contentDescription = "mid bg",
//                                contentScale = ContentScale.FillWidth,
//                                alignment = BottomCenter,
//                                modifier = Modifier
//                                    .matchParentSize()
//                                    .graphicsLayer {
//                                        translationY = midBgOffset
//                                    }
//                            )
//                            Image(
//                                painter = painterResource(id = R.drawable.ic_outerbg),
//                                contentDescription = "outer bg",
//                                contentScale = ContentScale.FillWidth,
//                                alignment = BottomCenter,
//                                modifier = Modifier.matchParentSize()
//                            )
//                        }
//                    }
//
//                    items(20) {
//                        Text(
//                            text = "Sample item",
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp)
//                        )
//                    }
//                }
//            }
//        }
        //endregion

        //region Multi-Select LazyColumn

//        setContent {
//            ComposeMultiSelectTheme {
//                var items by remember {
//                    mutableStateOf(
//                        (1..20).map {
//                            MyListItem(title = "Item $it", isSelected = false)
//                        }
//                    )
//                }
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxSize()
//                ) {
//                    items(items.size) { i ->
//                        Row(modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable {
//                                items = items.mapIndexed {j, item ->
//                                    if (i == j) {
//                                        item.copy(isSelected = !item.isSelected)
//                                    } else {
//                                        item
//                                    }
//                                }
//                            }
//                            .padding(16.dp),
//                            horizontalArrangement = Arrangement.SpaceBetween,
//                            verticalAlignment = Alignment.CenterVertically) {
//                            Text(text = items[i].title)
//                            if (items[i].isSelected) {
//                                Icon(
//                                    imageVector = Icons.Default.Check,
//                                    contentDescription = "Selected",
//                                    tint = Color.Green,
//                                    modifier = Modifier.size(20.dp)
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }

        //endregion

        //region Permission Handling

//        setContent {
//            PermissionHandlingComposeTheme {
//                val permissionState = rememberMultiplePermissionsState(
//                    permissions = listOf(
//                        Manifest.permission.CAMERA,
//                        Manifest.permission.RECORD_AUDIO
//                    )
//                )
//                val lifecycleOwner = LocalLifecycleOwner.current
//                DisposableEffect(
//                    key1 = lifecycleOwner,
//                    effect = {
//                        val observer = LifecycleEventObserver { _, event ->
//                            if (event == Lifecycle.Event.ON_START) {
//                                permissionState.launchMultiplePermissionRequest()
//                            }
//                        }
//                        lifecycleOwner.lifecycle.addObserver(observer)
//
//                        onDispose {
//                            lifecycleOwner.lifecycle.removeObserver(observer)
//                        }
//                    })
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    permissionState.permissions.forEach { perm ->
//                        when (perm.permission) {
//                            Manifest.permission.CAMERA -> {
//                                when {
//                                    perm.status.isGranted -> {
//                                        Text(text = "Camera permission accepted!")
//                                    }
//                                    perm.status.shouldShowRationale -> {
//                                        Text(text = "Camera permission is needed to access the camera!")
//                                    }
//                                    perm.status.isPermanentlyDenied() -> {
//                                        Text(text = "Camera permission was permanently denied. You can enable it in the app settings!")
//                                    }
//                                }
//                            }
//                            Manifest.permission.RECORD_AUDIO -> {
//                                when {
//                                    perm.status.isGranted -> {
//                                        Text(text = "Record audio permission accepted!")
//                                    }
//                                    perm.status.shouldShowRationale -> {
//                                        Text(text = "Record audio permission is needed to access the camera!")
//                                    }
//                                    perm.status.isPermanentlyDenied() -> {
//                                        Text(text = "Record audio permission was permanently denied. You can enable it in the app settings!")
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }

        //endregion

        //region Compose Navigation with Library

//        setContent {
//            ComposeNavDestinationsDemoTheme {
//                val navController = rememberNavController()
//                NavHost(
//                    navController = navController,
//                    startDestination = "login"
//                ) {
//                    //Login Screen
//                    composable("login") {
//                        LoginScreen(navController = navController)
//                    }
//
//                    //Profile Screen
//                    composable(
//                        route = "profile/{name}/{userId}/{timestamp}",
//                        arguments = listOf(
//                            navArgument("name") {
//                                type = NavType.StringType
//                            },
//                            navArgument("userId") {
//                                type = NavType.StringType
//                            },
//                            navArgument("timestamp") {
//                                type = NavType.LongType
//                            }
//                        )
//                    ) {
//                        val name = it.arguments?.getString("name") ?: "Null"
//                        val userId = it.arguments?.getString("userId") ?: "Null"
//                        val timestamp = it.arguments?.getLong("timestamp") ?: 0
//                        ProfileScreen(
//                            navController = navController,
//                            name = name,
//                            userId = userId,
//                            created = timestamp
//                        )
//                    }
//
//                    //Post Screen
//                    composable(
//                        route = "post/{showOnlyPostByUser}",
//                        arguments = listOf(
//                            navArgument("showOnlyPostByUser") {
//                                type = NavType.BoolType
//                                defaultValue = false
//                            }
//                        )
//                    ) {
//                        val showOnlyPostByUser = it.arguments?.getBoolean("showOnlyPostByUser") ?: false
//                        PostScreen(showOnlyPostByUser)
//                    }
//                }

//
//                DestinationsNavHost(navGraph = NavGraphs.root)
//            }
//        }

        //endregion

        //region Support All Screen Sizes

//        setContent {
//            SupportAllScreenSizesComposeTheme {
//                val windowInfo = rememberWindowInfo()
//                if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) {
//                    LazyColumn(
//                        modifier = Modifier.fillMaxSize()
//                    ) {
//                        // List 1
//                        items(10) {
//                            Text(
//                                text = "Item $it",
//                                fontSize = 25.sp,
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .background(Color.Cyan)
//                                    .padding(16.dp)
//                            )
//                        }
//                        // List 2
//                        items(10) {
//                            Text(
//                                text = "Item $it",
//                                fontSize = 25.sp,
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .background(Color.Green)
//                                    .padding(16.dp)
//                            )
//                        }
//                    }
//                } else {
//                    Row(modifier = Modifier.fillMaxWidth()) {
//                        LazyColumn(
//                            modifier = Modifier.weight(1f)
//                        ) {
//                            // List 1
//                            items(10) {
//                                Text(
//                                    text = "Item $it",
//                                    fontSize = 25.sp,
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .background(Color.Cyan)
//                                        .padding(16.dp)
//                                )
//                            }
//                        }
//                        LazyColumn(
//                            modifier = Modifier.weight(1f)
//                        ) {
//                            // List 2
//                            items(10) {
//                                Text(
//                                    text = "Item $it",
//                                    fontSize = 25.sp,
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .background(Color.Green)
//                                        .padding(16.dp)
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }

        //endregion

        //region Complex Animations With MotionLayout

//        setContent {
//            MotionLayoutComposeTheme {
//                Column {
//                    var progress by remember {
//                        mutableStateOf(0f)
//                    }
//                    ProfileHeader(progress = progress)
//                    Spacer(modifier = Modifier.height(32.dp))
//                    Slider(
//                        value = progress, onValueChange = {
//                            progress = it
//                        },
//                        modifier = Modifier.padding(horizontal = 32.dp)
//                    )
//                }
//            }
//        }

        //endregion

        //region Pagination

//        setContent {
//            ComposePagingYTTheme {
//                val viewModel = viewModel<MainViewModel>()
//                val state = viewModel.state
//                LazyColumn(modifier = Modifier.fillMaxSize()) {
//                    items(state.items.size) { i ->
//                        val item = state.items[i]
//                        if (i >= state.items.size - 1 && !state.endReached && !state.isLoading) {
//                            viewModel.loadNextItems()
//                        }
//                        Column(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp)
//                        ) {
//                            Text(
//                                text = item.title,
//                                fontSize = 20.sp,
//                                color = Color.Black,
//                            )
//                            Spacer(modifier = Modifier.height(8.dp))
//                            Text(text = item.description)
//                        }
//                    }
//                    item {
//                        if (state.isLoading) {
//                            Row(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(8.dp),
//                                horizontalArrangement = Arrangement.Center
//                            ) {
//                                CircularProgressIndicator()
//                            }
//                        }
//                    }
//                }
//            }
//        }

        //endregion

        //region Bottom Sheet

//        setContent {
//            BottomSheetComposeTheme {
//                val sheetState = rememberBottomSheetState(
//                    initialValue = BottomSheetValue.Collapsed,
//                    // animation here
//                    animationSpec = spring(
//                        dampingRatio = Spring.StiffnessMedium
//                    )
//                )
//                val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
//                val scope = rememberCoroutineScope()
//                BottomSheetScaffold(
//                    scaffoldState = scaffoldState,
//                    sheetContent = {
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(300.dp),
//                            contentAlignment = Center
//                        ) {
//                            Text(text = "Bottom sheet", fontSize = 60.sp)
//                        }
//                    },
//                    sheetBackgroundColor = Color.Green,
//                    sheetPeekHeight = 0.dp
//                ) {
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Center
//                    ) {
//                        Button(onClick = {
//                            scope.launch {
//                                if (sheetState.isCollapsed) {
//                                    sheetState.expand()
//                                } else {
//                                    sheetState.collapse()
//                                }
//                            }
//                        }) {
//                            Text(text = "Bottom sheet fraction: ${sheetState.progress.fraction}")
//                        }
//                    }
//                }
//            }
//        }

        //endregion

        //region Navigation Drawer

//        setContent {
//            NavigationDrawerComposeTheme {
//                val scope = rememberCoroutineScope()
//                val scaffoldState = rememberScaffoldState()
//                Scaffold(
//                    scaffoldState = scaffoldState,
//                    topBar = {
//                        AppBar {
//                            scope.launch {
//                                scaffoldState.drawerState.open()
//                            }
//                        }
//                    },
//                    drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
//                    drawerContent = {
//                        DrawerHeader()
//                        DrawerBody(
//                            items = listOf(
//                                MenuItem(
//                                    id = "home",
//                                    title = "Home",
//                                    contentDescription = "Go to Home Screen",
//                                    icon = Icons.Default.Home
//                                ),
//                                MenuItem(
//                                    id = "settings",
//                                    title = "Settings",
//                                    contentDescription = "Go to Settings Screen",
//                                    icon = Icons.Default.Settings
//                                ),
//                                MenuItem(
//                                    id = "help",
//                                    title = "Help",
//                                    contentDescription = "Get help",
//                                    icon = Icons.Default.Info
//                                ),
//                            ),
//                            onItemClick = {
//                                println("Click on ${it.title}")
//                            }
//                        )
//                    }
//                ) {
//
//                }
//            }
//        }

        //endregion

        //region Lazy Grid

        setContent {
            LazyVerticalGridComposeTheme {
                val state = rememberLazyGridState(
                    initialFirstVisibleItemIndex = 99
                )
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(100.dp),
                    state = state,
                    content = {
                        items(100) { i ->
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(Color.Green),
                                contentAlignment = Center
                            ) {
                                Text(text = "Item $i")
                            }
                        }
                    })
            }
        }

        //endregion

    }

    private fun Float.toDp(): Dp {
        return (this / Resources.getSystem().displayMetrics.density).dp
    }

}

@OptIn(ExperimentalMotionApi::class)
@Composable
fun ProfileHeader(progress: Float) {
    val context = LocalContext.current
    val motionScene = remember {
        context.resources
            .openRawResource(R.raw.motion_scene)
            .readBytes()
            .decodeToString()
    }
    MotionLayout(
        motionScene = MotionScene(content = motionScene),
        progress = progress,
        modifier = Modifier.fillMaxWidth()
    ) {
        val properties = motionProperties(id = "profile_pic")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .layoutId("box")
        )
        Image(
            painter = painterResource(id = R.drawable.cat),
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = properties.value.color("background"),
                    shape = CircleShape
                )
                .layoutId("profile_pic")
        )
        Text(
            text = "Tai, Pham The",
            fontSize = 24.sp,
            modifier = Modifier.layoutId("username"),
            color = properties.value.color("background")
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@RootNavGraph(start = true)
@Destination
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Text(text = "Login Screen")
        Button(onClick = {
            navigator.navigate(
                ProfileScreenDestination(
                    User(
                        name = "Tai, Pham The",
                        id = "tai",
                        created = LocalDateTime.now()
                    )
                )
            )
        }) {
            Text(text = "Go to Profile Screen")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator,
    user: User
) {
//    val user = remember {
//        User(
//            name = name,
//            id = userId,
//            created = LocalDateTime.ofInstant(Instant.ofEpochMilli(created), ZoneId.systemDefault())
//        )
//    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Text(text = "Profile screen $user", textAlign = TextAlign.Center)
        Button(onClick = {
            navigator.navigate(
                PostScreenDestination(showOnlyPostByUser = true)
            )
        }) {
            Text(text = "Go to Post Screen")
        }
    }
}

@Destination
@Composable
fun PostScreen(
    showOnlyPostByUser: Boolean = false
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Center
    ) {
        Text(text = "Post Screen, $showOnlyPostByUser")
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
        contentAlignment = Center,
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
        contentAlignment = Center,
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
            contentAlignment = Center,
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
        contentAlignment = Center
    ) {
        Text(text = "Home screen")
    }
}

@Composable
fun ChatScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Center
    ) {
        Text(text = "Chat screen")
    }
}

@Composable
fun SettingsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Center
    ) {
        Text(text = "Settings screen")
    }
}
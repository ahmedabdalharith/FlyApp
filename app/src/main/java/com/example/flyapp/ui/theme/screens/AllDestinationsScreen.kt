package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.navigition.Screen
import com.example.flyapp.ui.theme.theme.DarkNavyBlue
import com.example.flyapp.ui.theme.theme.DeepBlue
import com.example.flyapp.ui.theme.theme.GoldColor
import com.example.flyapp.ui.theme.theme.MediumBlue

data class Destination(
    val id: String,
    val city: String,
    val country: String,
    val code: String,
    val imageRes: Int,
    val rating: Float,
    val isFeatured: Boolean = false,
    val description: String,
    val popularity: Int
)

@Composable
fun AllDestinationsScreen(
    navController: NavController
) {
    // Sample destinations data
    val destinations = remember {
        mutableStateOf(
            listOf(
                Destination(
                    id = "LON001",
                    city = "London",
                    country = "United Kingdom",
                    code = "LHR",
                    imageRes = R.drawable.tokyo,
                    rating = 4.7f,
                    isFeatured = true,
                    description = "Historic landmarks, cultural diversity, and vibrant city life",
                    popularity = 98
                ),
                Destination(
                    id = "PAR001",
                    city = "Paris",
                    country = "France",
                    code = "CDG",
                    imageRes = R.drawable.tokyo,
                    rating = 4.8f,
                    isFeatured = true,
                    description = "City of lights, art, cuisine, and romantic atmosphere",
                    popularity = 98
                ),
                Destination(
                    id = "NYC001",
                    city = "New York",
                    country = "USA",
                    code = "JFK",
                    imageRes = R.drawable.tokyo,
                    rating = 4.6f,
                    isFeatured = true,
                    description = "Iconic skyline, diverse neighborhoods, and endless activities",
                    popularity = 98
                ),
                Destination(
                    id = "TYO001",
                    city = "Tokyo",
                    country = "Japan",
                    code = "HND",
                    imageRes = R.drawable.tokyo,
                    rating = 4.9f,
                    description = "Dynamic blend of traditional culture and futuristic technology",
                    popularity = 98
                ),
                Destination(
                    id = "SYD001",
                    city = "Sydney",
                    country = "Australia",
                    code = "SYD",
                    imageRes = R.drawable.paris,
                    rating = 4.5f,
                    description = "Iconic harbor, beaches, and relaxed outdoor lifestyle",
                    popularity = 98
                ),
                Destination(
                    id = "ROM001",
                    city = "Rome",
                    country = "Italy",
                    code = "FCO",
                    imageRes = R.drawable.tokyo,
                    rating = 4.6f,
                    description = "Ancient history, art, architecture, and exquisite cuisine",
                    popularity = 98
                ),
                Destination(
                    id = "DXB001",
                    city = "Dubai",
                    country = "UAE",
                    code = "DXB",
                    imageRes = R.drawable.paris,
                    rating = 4.8f,
                    isFeatured = true,
                    description = "Futuristic architecture, luxury experiences, and desert adventures",
                    popularity = 98
                ),
                Destination(
                    id = "IST001",
                    city = "Istanbul",
                    country = "Turkey",
                    code = "IST",
                    imageRes = R.drawable.tokyo,
                    rating = 4.5f,
                    description = "Unique blend of European and Asian cultures and historic sites",
                    popularity = 98
                ),
                Destination(
                    id = "SIN001",
                    city = "Singapore",
                    country = "Singapore",
                    code = "SIN",
                    imageRes = R.drawable.tokyo,
                    rating = 4.7f,
                    description = "Modern city-state with beautiful gardens and diverse neighborhoods",
                    popularity = 98
                ),
                Destination(
                    id = "AMS001",
                    city = "Amsterdam",
                    country = "Netherlands",
                    code = "AMS",
                    imageRes = R.drawable.tokyo,
                    rating = 4.4f,
                    description = "Picturesque canals, historic buildings, and vibrant culture",
                    popularity = 98
                ),
                Destination(
                    id = "BCN001",
                    city = "Barcelona",
                    country = "Spain",
                    code = "BCN",
                    imageRes = R.drawable.tokyo,
                    rating = 4.6f,
                    description = "Unique architecture, Mediterranean beaches, and lively atmosphere",
                    popularity = 98
                ),
                Destination(
                    id = "HKG001",
                    city = "Hong Kong",
                    country = "China",
                    code = "HKG",
                    imageRes = R.drawable.tokyo,
                    rating = 4.5f,
                    description = "Skyline views, vibrant street life, and diverse cuisine",
                    popularity = 98
                )
            )
        )
    }

    // Animation for the background effect (matching existing screens)
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val hologramGlow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hologram_glow"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DeepBlue,
                        MediumBlue,
                        DarkNavyBlue
                    )
                )
            )
    ) {
        // Security pattern background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // Draw security pattern lines (like passport security pattern)
            val pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f, 3f), 0f)
            drawLine(
                color = Color.White.copy(alpha = 0.05f),
                start = Offset(0f, 0f),
                end = Offset(canvasWidth, canvasHeight),
                strokeWidth = 1f,
                pathEffect = pathEffect
            )

            drawLine(
                color = Color.White.copy(alpha = 0.05f),
                start = Offset(canvasWidth, 0f),
                end = Offset(0f, canvasHeight),
                strokeWidth = 1f,
                pathEffect = pathEffect
            )

            // Draw circular watermark
            val stroke = androidx.compose.ui.graphics.drawscope.Stroke(width = 1f, pathEffect = pathEffect)
            for (i in 1..5) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.03f),
                    radius = canvasHeight / 3f * i / 5f,
                    center = Offset(canvasWidth / 2f, canvasHeight / 2f),
                    style = stroke
                )
            }
        }

        // Main content
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top app bar
            DestinationsTopAppBar(navController = navController)

            // Featured destinations section
            FeaturedDestinationsSection(
                destinations = destinations.value.filter { it.isFeatured },
                onDestinationSelected = { destination ->
                    // Navigate to destination details or search screen with this destination
                    navController.navigate("${Screen.SearchResultsScreen.route}?destination=${destination.code}")
                }
            )

            // All destinations grid
            Text(
                text = "EXPLORE DESTINATIONS",
                color = GoldColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(destinations.value) { destination ->
                    DestinationCard(
                        destination = destination,
                        onDestinationSelected = {
                            // Navigate to destination details or search screen with this destination
                            navController.navigate("${Screen.SearchResultsScreen.route}?destination=${destination.code}")
                        }
                    )
                }

                // Add some padding at the bottom
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun DestinationsTopAppBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back button with gold accent
        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier
                .size(40.dp)
                .border(1.dp, GoldColor.copy(alpha = 0.8f), CircleShape)
                .background(DarkNavyBlue.copy(alpha = 0.7f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = GoldColor
            )
        }

        // Screen title
        Text(
            text = "DESTINATIONS",
            color = GoldColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        // Search button
        IconButton(
            onClick = { navController.navigate(Screen.SearchResultsScreen.route) },
            modifier = Modifier
                .size(40.dp)
                .border(1.dp, GoldColor.copy(alpha = 0.8f), CircleShape)
                .background(DarkNavyBlue.copy(alpha = 0.7f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = GoldColor
            )
        }
    }
}

@Composable
fun FeaturedDestinationsSection(
    destinations: List<Destination>,
    onDestinationSelected: (Destination) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Section title
        Text(
            text = "FEATURED DESTINATIONS",
            color = GoldColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Featured destinations carousel (simplified as row for now)
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
        ) {
            items(destinations.size) { index ->
                FeaturedDestinationCard(
                    destination = destinations[index],
                    onDestinationSelected = onDestinationSelected
                )
            }
        }
    }
}

@Composable
fun FeaturedDestinationCard(
    destination: Destination,
    onDestinationSelected: (Destination) -> Unit
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(160.dp)
            .shadow(8.dp, RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        GoldColor.copy(alpha = 0.7f),
                        GoldColor.copy(alpha = 0.3f)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onDestinationSelected(destination) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.85f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            // Destination image placeholder (would be replaced with actual image)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                DarkNavyBlue.copy(alpha = 0.9f)
                            ),
                            startY = 50f
                        )
                    )
            ){
                // Placeholder for image
                Image(
                    painter = painterResource(id = destination.imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Destination info
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                // Destination name and country
                Text(
                    text = destination.city,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = destination.country,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = destination.code,
                        color = GoldColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = GoldColor,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = destination.rating.toString(),
                        color = GoldColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Featured badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(GoldColor, RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "FEATURED",
                    color = DarkNavyBlue,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun DestinationCard(
    destination: Destination,
    onDestinationSelected: (Destination) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.8f)
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        GoldColor.copy(alpha = 0.5f),
                        GoldColor.copy(alpha = 0.2f)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onDestinationSelected(destination) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.85f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Destination image placeholder (would be replaced with actual image)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                DarkNavyBlue.copy(alpha = 0.9f)
                            ),
                            startY = 100f
                        )
                    )
            ){
                // Placeholder for image
                Image(
                    painter = painterResource(id = destination.imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Destination info
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                // Destination name
                Text(
                    text = destination.city,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Country and airport code
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = destination.country,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = destination.code,
                        color = GoldColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = GoldColor,
                        modifier = Modifier.size(12.dp)
                    )

                    Spacer(modifier = Modifier.width(2.dp))

                    Text(
                        text = destination.rating.toString(),
                        color = GoldColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AllDestinationsScreenPreview() {
    MaterialTheme {
        Surface {
            AllDestinationsScreen(rememberNavController())
        }
    }
}
//package com.example.flyapp.ui.theme.screens
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.animation.core.tween
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.slideInVertically
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Favorite
//import androidx.compose.material.icons.filled.FavoriteBorder
//import androidx.compose.material.icons.filled.LocationOn
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material.icons.filled.Star
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.FloatingActionButton
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.alpha
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.rememberNavController
//import com.example.flyapp.R
//import com.example.flyapp.ui.theme.components.ParticleEffectBackground
//import com.example.flyapp.ui.theme.navigition.Screen
//import kotlinx.coroutines.delay
//
//// Data class for destinations
//data class Destination(
//    val id: String,
//    val name: String,
//    val country: String,
//    val description: String,
//    val imageResId: Int,
//    val price: Double,
//    val rating: Float,
//    val features: List<DestinationFeature>,
//    val travelTime: String,
//    val isFavorite: Boolean = false
//)
//
//enum class DestinationFeature {
//    BEACH, MOUNTAIN, CITY, HISTORIC, ADVENTURE, RELAXATION, FOOD, SHOPPING, NIGHTLIFE
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AllDestinationsScreen(navController: NavHostController) {
//    // Sample destinations data
//    val destinations = remember {
//        listOf(
//            Destination(
//                id = "paris",
//                name = "Paris",
//                country = "France",
//                description = "Known as the City of Love, Paris offers iconic landmarks like the Eiffel Tower, world-class museums, and charming cafés.",
//                imageResId = R.drawable.paris,
//                price = 799.99,
//                rating = 4.7f,
//                features = listOf(DestinationFeature.CITY, DestinationFeature.HISTORIC, DestinationFeature.FOOD),
//                travelTime = "7h 30m"
//            ),
//            Destination(
//                id = "bali",
//                name = "Bali",
//                country = "Indonesia",
//                description = "A tropical paradise with beautiful beaches, ancient temples, and lush rice terraces. Perfect for relaxation and adventure alike.",
//                imageResId = R.drawable.emirates_logo,
//                price = 1099.99,
//                rating = 4.8f,
//                features = listOf(DestinationFeature.BEACH, DestinationFeature.RELAXATION, DestinationFeature.ADVENTURE),
//                travelTime = "16h 45m",
//                isFavorite = true
//            ),
//            Destination(
//                id = "tokyo",
//                name = "Tokyo",
//                country = "Japan",
//                description = "A vibrant metropolis blending ultramodern with traditional. Experience cutting-edge technology, amazing food, and ancient temples.",
//                imageResId = R.drawable.tokyo,
//                price = 1299.99,
//                rating = 4.9f,
//                features = listOf(DestinationFeature.CITY, DestinationFeature.FOOD, DestinationFeature.SHOPPING),
//                travelTime = "12h 15m"
//            ),
//            Destination(
//                id = "santorini",
//                name = "Santorini",
//                country = "Greece",
//                description = "Famous for its stunning sunsets, white-washed buildings with blue domes, and crystal clear waters.",
//                imageResId = R.drawable.tokyo,
//                price = 899.99,
//                rating = 4.8f,
//                features = listOf(DestinationFeature.BEACH, DestinationFeature.RELAXATION, DestinationFeature.HISTORIC),
//                travelTime = "5h 20m",
//                isFavorite = true
//            ),
//            Destination(
//                id = "new_york",
//                name = "New York",
//                country = "United States",
//                description = "The Big Apple offers world-famous attractions, diverse neighborhoods, Broadway shows, and incredible dining experiences.",
//                imageResId = R.drawable.new_york,
//                price = 899.99,
//                rating = 4.6f,
//                features = listOf(DestinationFeature.CITY, DestinationFeature.SHOPPING, DestinationFeature.NIGHTLIFE),
//                travelTime = "8h 00m"
//            ),
//            Destination(
//                id = "maldives",
//                name = "Maldives",
//                country = "Maldives",
//                description = "A tropical paradise with overwater bungalows, crystal clear lagoons, and vibrant coral reefs perfect for diving and snorkeling.",
//                imageResId = R.drawable.tokyo,
//                price = 1599.99,
//                rating = 4.9f,
//                features = listOf(DestinationFeature.BEACH, DestinationFeature.RELAXATION, DestinationFeature.ADVENTURE),
//                travelTime = "14h 30m"
//            ),
//            Destination(
//                id = "rome",
//                name = "Rome",
//                country = "Italy",
//                description = "The Eternal City is home to ancient ruins, world-class art, and delicious Italian cuisine.",
//                imageResId = R.drawable.tokyo,
//                price = 749.99,
//                rating = 4.7f,
//                features = listOf(DestinationFeature.CITY, DestinationFeature.HISTORIC, DestinationFeature.FOOD),
//                travelTime = "4h 15m"
//            ),
//            Destination(
//                id = "machu_picchu",
//                name = "Machu Picchu",
//                country = "Peru",
//                description = "The ancient Incan citadel set among breathtaking mountain scenery offers a glimpse into a fascinating civilization.",
//                imageResId = R.drawable.tokyo,
//                price = 1199.99,
//                rating = 4.8f,
//                features = listOf(DestinationFeature.HISTORIC, DestinationFeature.ADVENTURE, DestinationFeature.MOUNTAIN),
//                travelTime = "18h 30m"
//            )
//        )
//    }
//
//    var searchQuery by remember { mutableStateOf("") }
//    var isSearchActive by remember { mutableStateOf(false) }
//    var filteredDestinations by remember { mutableStateOf(destinations) }
//
//    // Animation states
//    var showContent by remember { mutableStateOf(false) }
//    val contentAlpha by animateFloatAsState(
//        targetValue = if (showContent) 1f else 0f,
//        animationSpec = tween(durationMillis = 800),
//        label = "content_alpha"
//    )
//
//    // Filter destinations based on search query
//    LaunchedEffect(searchQuery) {
//        filteredDestinations = if (searchQuery.isEmpty()) {
//            destinations
//        } else {
//            destinations.filter {
//                it.name.contains(searchQuery, ignoreCase = true) ||
//                        it.country.contains(searchQuery, ignoreCase = true) ||
//                        it.description.contains(searchQuery, ignoreCase = true)
//            }
//        }
//    }
//
//    // Trigger content animation
//    LaunchedEffect(key1 = true) {
//        delay(300)
//        showContent = true
//    }
//
//    // Scroll state
//    val lazyListState = rememberLazyListState()
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                brush = Brush.verticalGradient(
//                    colors = listOf(
//                        Color(0xFF001034),
//                        Color(0xFF003045),
//                        Color(0xFF004D40)
//                    )
//                )
//            )
//    ) {
//        // Enhanced background animations
//        ParticleEffectBackground()
//
//        Scaffold(
//            containerColor = Color.Transparent,
//            topBar = {
//                TopAppBar(
//                    title = {
//                        if (!isSearchActive) {
//                            Text(
//                                text = "Explore Destinations",
//                                style = MaterialTheme.typography.titleMedium.copy(
//                                    fontWeight = FontWeight.Bold,
//                                    color = Color.White
//                                )
//                            )
//                        } else {
//                            TextField(
//                                value = searchQuery,
//                                onValueChange = { searchQuery = it },
//                                placeholder = {
//                                    Text(
//                                        "Search destinations...",
//                                        color = Color.White.copy(alpha = 0.6f)
//                                    )
//                                },
//                                modifier = Modifier.fillMaxWidth(),
//                                singleLine = true,
//                                colors = TextFieldDefaults.colors(
//                                    focusedContainerColor = Color.Transparent,
//                                    unfocusedContainerColor = Color.Transparent,
//                                    disabledContainerColor = Color.Transparent,
//                                    focusedIndicatorColor = Color.White,
//                                    unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f),
//                                    focusedTextColor = Color.White,
//                                    unfocusedTextColor = Color.White
//                                )
//                            )
//                        }
//                    },
//                    navigationIcon = {
//                        IconButton(onClick = { navController.popBackStack() }) {
//                            Icon(
//                                imageVector = Icons.Default.ArrowBack,
//                                contentDescription = "Back",
//                                tint = Color.White
//                            )
//                        }
//                    },
//                    actions = {
//                        IconButton(
//                            onClick = { isSearchActive = !isSearchActive }
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Search,
//                                contentDescription = "Search",
//                                tint = Color.White
//                            )
//                        }
//                        IconButton(onClick = { /* Show filters */ }) {
//                            Icon(
//                                painterResource(R.drawable.filter_ic),
//                                contentDescription = "Filter",
//                                tint = Color.White
//                            )
//                        }
//                    },
//                    colors = TopAppBarDefaults.topAppBarColors(
//                        containerColor = Color.Transparent
//                    )
//                )
//            },
//        ) { paddingValues ->
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(paddingValues)
//                    .alpha(contentAlpha)
//            ) {
//                if (filteredDestinations.isEmpty()) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(32.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//                            Icon(
//                                painter = painterResource(R.drawable.plane_ticket),
//                                contentDescription = null,
//                                modifier = Modifier.size(64.dp),
//                                tint = Color.White.copy(alpha = 0.7f)
//                            )
//                            Spacer(modifier = Modifier.height(16.dp))
//                            Text(
//                                text = "No destinations found",
//                                fontSize = 20.sp,
//                                fontWeight = FontWeight.Bold,
//                                color = Color.White,
//                                textAlign = TextAlign.Center
//                            )
//                            Spacer(modifier = Modifier.height(8.dp))
//                            Text(
//                                text = "Try adjusting your search or filters",
//                                fontSize = 16.sp,
//                                color = Color.White.copy(alpha = 0.7f),
//                                textAlign = TextAlign.Center
//                            )
//                        }
//                    }
//                } else {
//                    LazyColumn(
//                        state = lazyListState,
//                        contentPadding = PaddingValues(16.dp),
//                        verticalArrangement = Arrangement.spacedBy(16.dp)
//                    ) {
//                        items(filteredDestinations) { destination ->
//                            AnimatedVisibility(
//                                visible = showContent,
//                                enter = fadeIn(tween(800)) + slideInVertically(
//                                    initialOffsetY = { it / 2 },
//                                    animationSpec = tween(durationMillis = 800)
//                                )
//                            ) {
//                                DestinationCard(
//                                    destination = destination,
//                                    onDestinationClick = {
//                                        // Navigate to destination details
//                                        navController.navigate(Screen.FlightDetailsScreen.route)
//                                    },
//                                    onFavoriteClick = {
//                                        // Handle favorite click
//                                        // Update the destination's favorite status
//                                        val updatedDestinations = filteredDestinations.map {
//                                            if (it.id == destination.id) {
//                                                it.copy(isFavorite = !it.isFavorite)
//                                            } else {
//                                                it
//                                            }
//                                        }
//                                        filteredDestinations = updatedDestinations
//                                    }
//                                )
//                            }
//                        }
//                        // Add some space at the bottom for FAB
//                        item { Spacer(modifier = Modifier.height(80.dp)) }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun DestinationCard(
//    destination: Destination,
//    onDestinationClick: () -> Unit,
//    onFavoriteClick: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable(onClick = onDestinationClick)
//            .shadow(8.dp, RoundedCornerShape(16.dp)),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color(0xFF1A3546).copy(alpha = 0.9f)
//        )
//    ) {
//        Column(
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            // Image with overlay
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(180.dp)
//            ) {
//                // Replace with actual image loading from resources
//                Image(
//                    painter = painterResource(id = R.drawable.plane_ticket), // Placeholder image
//                    contentDescription = destination.name,
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier.fillMaxSize()
//                )
//
//                // Gradient overlay
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(
//                            brush = Brush.verticalGradient(
//                                colors = listOf(
//                                    Color.Transparent,
//                                    Color(0xFF1A3546).copy(alpha = 0.7f)
//                                )
//                            )
//                        )
//                )
//
//                // Favorite button and price tag
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(16.dp)
//                ) {
//                    // Price tag
//                    Surface(
//                        modifier = Modifier
//                            .align(Alignment.TopStart),
//                        color = Color(0xFF4CAF50),
//                        shape = RoundedCornerShape(8.dp)
//                    ) {
//                        Text(
//                            text = "$${destination.price}",
//                            color = Color.White,
//                            fontWeight = FontWeight.Bold,
//                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
//                        )
//                    }
//
//                    // Favorite button
//                    IconButton(
//                        onClick = onFavoriteClick,
//                        modifier = Modifier
//                            .size(40.dp)
//                            .align(Alignment.TopEnd)
//                            .background(Color.White.copy(alpha = 0.3f), CircleShape)
//                    ) {
//                        Icon(
//                            imageVector = if (destination.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
//                            contentDescription = "Favorite",
//                            tint = if (destination.isFavorite) Color(0xFFE91E63) else Color.White
//                        )
//                    }
//
//                    // Location and country
//                    Column(
//                        modifier = Modifier
//                            .align(Alignment.BottomStart)
//                    ) {
//                        Text(
//                            text = destination.name,
//                            color = Color.White,
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.LocationOn,
//                                contentDescription = null,
//                                tint = Color.White.copy(alpha = 0.7f),
//                                modifier = Modifier.size(16.dp)
//                            )
//                            Spacer(modifier = Modifier.width(4.dp))
//                            Text(
//                                text = destination.country,
//                                color = Color.White.copy(alpha = 0.7f),
//                                fontSize = 14.sp
//                            )
//                        }
//                    }
//                }
//            }
//
//            // Destination details
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            ) {
//                // Description
//                Text(
//                    text = destination.description,
//                    color = Color.White.copy(alpha = 0.8f),
//                    fontSize = 14.sp,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Rating and travel time
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    // Rating
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Star,
//                            contentDescription = null,
//                            tint = Color(0xFFFFD700),
//                            modifier = Modifier.size(20.dp)
//                        )
//                        Spacer(modifier = Modifier.width(4.dp))
//                        Text(
//                            text = destination.rating.toString(),
//                            color = Color.White,
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 16.sp
//                        )
//                    }
//
//                    // Travel time
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            painter = painterResource(R.drawable.plane_ticket),
//                            contentDescription = null,
//                            tint = Color(0xFF4CAF50),
//                            modifier = Modifier.size(20.dp)
//                        )
//                        Spacer(modifier = Modifier.width(4.dp))
//                        Text(
//                            text = destination.travelTime,
//                            color = Color.White,
//                            fontSize = 14.sp
//                        )
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                // Features
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    destination.features.take(3).forEach { feature ->
//                        FeatureChip(feature = feature)
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun FeatureChip(feature: DestinationFeature) {
//    val (featureText, featureColor) = when (feature) {
//        DestinationFeature.BEACH -> Pair("Beach", Color(0xFF03A9F4))
//        DestinationFeature.MOUNTAIN -> Pair("Mountain", Color(0xFF795548))
//        DestinationFeature.CITY -> Pair("City", Color(0xFF9C27B0))
//        DestinationFeature.HISTORIC -> Pair("Historic", Color(0xFFFF9800))
//        DestinationFeature.ADVENTURE -> Pair("Adventure", Color(0xFFE91E63))
//        DestinationFeature.RELAXATION -> Pair("Relaxation", Color(0xFF4CAF50))
//        DestinationFeature.FOOD -> Pair("Food", Color(0xFFFF5722))
//        DestinationFeature.SHOPPING -> Pair("Shopping", Color(0xFF2196F3))
//        DestinationFeature.NIGHTLIFE -> Pair("Nightlife", Color(0xFF673AB7))
//    }
//
//    Surface(
//        color = featureColor.copy(alpha = 0.2f),
//        shape = RoundedCornerShape(16.dp)
//    ) {
//        Text(
//            text = featureText,
//            color = featureColor,
//            fontSize = 12.sp,
//            fontWeight = FontWeight.Medium,
//            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun AllDestinationsScreenPreview() {
//    AllDestinationsScreen(navController = rememberNavController())
//}
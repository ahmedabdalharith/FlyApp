import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankingAppScreen() {
    // Color Palette
    val primaryOrange = Color(0xFFFF6B01)
    val backgroundColor = Color(0xFF121212)
    val cardBackground = Color(0xFF1E1E1E)

    // State for managing card swipe and additional interactions
    var activeCardIndex by remember { mutableStateOf(0) }
    var cardOffset by remember { mutableStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()

    // Main Screen Layout
    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = { Text("Your Cards", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back navigation */ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle more options */ }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                // Card Count
                Text(
                    text = "You have 3 Active Cards",
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Draggable Card Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                ) {
                    // Multiple Card Layers with Offset
                    listOf(0, 1, 2).forEach { index ->
                        CreditCard(
                            cardNumber = "4562 1122 4595 7852",
                            expiryDate = "24-2020",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .offset {
                                    // Create a stacked, slightly offset card view
                                    IntOffset(
                                        x = if (index == activeCardIndex) cardOffset.toInt() else
                                            (index - activeCardIndex) * 20,
                                        y = (index - activeCardIndex) * 20
                                    )
                                }
                                .draggable(
                                    orientation = Orientation.Horizontal,
                                    state = rememberDraggableState { delta ->
                                        cardOffset += delta
                                    },
                                    onDragStopped = { velocity ->
                                        coroutineScope.launch {
                                            // Logic for card swipe
                                            if (cardOffset > 100) {
                                                // Swipe right
                                                if (activeCardIndex > 0) {
                                                    activeCardIndex--
                                                }
                                            } else if (cardOffset < -100) {
                                                // Swipe left
                                                if (activeCardIndex < 2) {
                                                    activeCardIndex++
                                                }
                                            }
                                            cardOffset = 0f
                                        }
                                    }
                                )
                                .zIndex(3f - index)
                        )
                    }

                    // Add Card Button
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 8.dp, end = 8.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(primaryOrange)
                            .clickable { /* Add new card logic */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add Card",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Recent Transactions Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Transactions",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "See All",
                        color = primaryOrange,
                        modifier = Modifier.clickable { /* See all transactions logic */ }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Transaction Items with Swipe-to-Dismiss
                val transactions = remember {
                    mutableStateListOf(
                        TransactionData("Salary", "Billing Transaction", "+$12,00"),
                        TransactionData("Paypal", "Withdrawal", "-$10,00"),
                        TransactionData("Car Repair", "Car Engine Repair", "-$232,00")
                    )
                }

                transactions.forEachIndexed { index, transaction ->
                    var dismissState by remember { mutableStateOf(0f) }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        // Background for swipe actions
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(Color.Red.copy(alpha = 0.5f), shape = RoundedCornerShape(12.dp))
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete Transaction",
                                tint = Color.White
                            )
                        }

                        // Swipeable Transaction Item
                        TransactionItem(
                            icon = when (index) {
                                0 -> Icons.Filled.Info
                                1 -> Icons.Filled.Info
                                2 -> Icons.Filled.Delete
                                else -> Icons.Filled.Delete
                            },
                            title = transaction.title,
                            description = transaction.description,
                            amount = transaction.amount,
                            color = primaryOrange,
                            modifier = Modifier
                                .offset { IntOffset(dismissState.toInt(), 0) }
                                .draggable(
                                    orientation = Orientation.Horizontal,
                                    state = rememberDraggableState { delta ->
                                        dismissState += delta
                                    },
                                    onDragStopped = { velocity ->
                                        if (dismissState < -200) {
                                            // Remove transaction
                                            transactions.removeAt(index)
                                        } else {
                                            // Snap back
                                            dismissState = 0f
                                        }
                                    }
                                )
                        )
                    }
                }
            }
        }
    }
}

// Data class for transactions
data class TransactionData(
    val title: String,
    val description: String,
    val amount: String
)

@Composable
fun CreditCard(
    cardNumber: String,
    expiryDate: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFF6B01),
                        Color(0xFFFF8D01)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mastercard",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "VISA",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Column {
                Text(
                    text = cardNumber.chunked(4).joinToString(" "),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = expiryDate,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun TransactionItem(
    icon: ImageVector,
    title: String,
    description: String,
    amount: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1E1E1E))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left side - Icon and Text
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.1f))
                    .padding(8.dp)
            )
            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
        }

        // Right side - Amount
        Text(
            text = amount,
            color = if (amount.startsWith("+")) Color.Green else Color.Red,
            fontWeight = FontWeight.Bold
        )
    }
}
@Preview(showBackground = true)
@Composable
fun BankingAppScreenPreview() {
    BankingAppScreen()
}
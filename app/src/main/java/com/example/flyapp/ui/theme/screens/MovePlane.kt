import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.flyapp.R
import kotlin.random.Random

@Composable
fun AirplaneAnimationScreen() {
    // تتبع حجم الشاشة
    var screenSize by remember { mutableStateOf(IntSize(0, 0)) }

    // إنشاء حالة الرسوم المتحركة للطائرة
    val airplaneAnimation = rememberInfiniteTransition(label = "airplane")
    val planeXPosition = airplaneAnimation.animateFloat(
        initialValue = -200f,
        targetValue = screenSize.width + 200f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "planePosition"
    )

    // إنشاء مجموعة من الغيوم مع مواقع وأحجام عشوائية
    val clouds = remember {
        List(8) {
            Cloud(
                xOffsetRatio = Random.nextFloat(),
                yOffsetRatio = 0.1f + Random.nextFloat() * 0.5f,
                size = 0.6f + Random.nextFloat() * 0.8f
            )
        }
    }

    // تحريك الغيوم في الاتجاه المعاكس
    val cloudAnimation = rememberInfiniteTransition(label = "clouds")
    val cloudOffset = cloudAnimation.animateFloat(
        initialValue = 0f,
        targetValue = screenSize.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cloudPosition"
    )

    // تأثير التذبذب للطائرة
    val oscillation = airplaneAnimation.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "oscillation"
    )

    // منظر الرسوم المتحركة
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF96C7FF),  // سماء زرقاء فاتحة
                        Color(0xFF0A6EBD)   // سماء زرقاء داكنة
                    )
                )
            )
            .onSizeChanged { screenSize = it }
    ) {
        // رسم الغيوم
        clouds.forEach { cloud ->
            val cloudX = (cloud.xOffsetRatio * screenSize.width + cloudOffset.value) % screenSize.width
            val reverseCloudX = screenSize.width - cloudX

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                drawCloud(
                    Offset(reverseCloudX, cloud.yOffsetRatio * size.height),
                    Size(
                        cloud.size * 250f,
                        cloud.size * 120f
                    )
                )
            }
        }

        // استخدام صورة الطائرة بدلاً من رسمها
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val planeY = screenSize.height * 0.4f + oscillation.value
            val planeWidth = 380.dp
            val planeHeight = 120.dp

            // استخدام صورة من الموارد
            // لاحظ: يجب أن تضيف صورة اسمها airplane في مجلد res/drawable
            Image(
                painter = painterResource(id = R.drawable.myplane),
                contentDescription = "طائرة",
                modifier = Modifier
                    .offset(
                        x = planeXPosition.value.dp - planeWidth / 2,
                        y = planeY.dp - planeHeight / 2
                    )
                    .rotate(oscillation.value / 3)  // إضافة تأثير ميلان بسيط للطائرة
            )
        }
    }
}

// فئة لتمثيل السحابة
data class Cloud(
    val xOffsetRatio: Float,  // نسبة الموقع الأفقي (0-1)
    val yOffsetRatio: Float,  // نسبة الموقع الرأسي (0-1)
    val size: Float           // معامل الحجم (مقياس)
)

// رسم سحابة
fun DrawScope.drawCloud(position: Offset, cloudSize: Size) {
    val cloudColor = Color(0xE6FFFFFF)  // لون أبيض مع شفافية

    // رسم أجزاء السحابة كدوائر متداخلة
    drawCircle(
        color = cloudColor,
        radius = cloudSize.width * 0.25f,
        center = position + Offset(cloudSize.width * 0.25f, cloudSize.height * 0.5f)
    )

    drawCircle(
        color = cloudColor,
        radius = cloudSize.width * 0.2f,
        center = position + Offset(cloudSize.width * 0.1f, cloudSize.height * 0.5f)
    )

    drawCircle(
        color = cloudColor,
        radius = cloudSize.width * 0.3f,
        center = position + Offset(cloudSize.width * 0.5f, cloudSize.height * 0.5f)
    )

    drawCircle(
        color = cloudColor,
        radius = cloudSize.width * 0.25f,
        center = position + Offset(cloudSize.width * 0.7f, cloudSize.height * 0.5f)
    )

    drawCircle(
        color = cloudColor,
        radius = cloudSize.width * 0.2f,
        center = position + Offset(cloudSize.width * 0.85f, cloudSize.height * 0.5f)
    )

    // رسم جزء علوي للسحابة
    drawCircle(
        color = cloudColor,
        radius = cloudSize.width * 0.18f,
        center = position + Offset(cloudSize.width * 0.4f, cloudSize.height * 0.3f)
    )

    drawCircle(
        color = cloudColor,
        radius = cloudSize.width * 0.18f,
        center = position + Offset(cloudSize.width * 0.6f, cloudSize.height * 0.3f)
    )
}

@Preview(showBackground = true, widthDp = 500, heightDp = 300)
@Composable
fun AirplaneAnimationPreview() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AirplaneAnimationScreen()
        }
    }
}

@Composable
fun AirplaneAnimationApp() {
    MaterialTheme {
        AirplaneAnimationScreen()
    }
}
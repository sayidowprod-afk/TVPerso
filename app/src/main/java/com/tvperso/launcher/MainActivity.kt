package com.tvperso.launcher

import android.content.ActivityNotFoundException
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.Card
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TvPersoTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun TvPersoTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val apps = remember { queryTvApps(context.packageManager) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 48.dp, vertical = 32.dp),
        ) {
            Text(
                text = "TVPERSO",
                color = Color.White,
                fontSize = 32.sp,
                modifier = Modifier.padding(bottom = 24.dp),
            )

            AppRow(apps = apps)
        }
    }
}

@Composable
fun AppRow(apps: List<AppInfo>) {
    val context = LocalContext.current

    TvLazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(apps) { app ->
            AppTile(
                app = app,
                onClick = {
                    try {
                        context.startActivity(app.launchIntent)
                    } catch (e: ActivityNotFoundException) {
                        // App removed or intent invalid since the list was built; ignore.
                    }
                },
            )
        }
    }
}

@Composable
fun AppTile(app: AppInfo, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .size(160.dp)
            .padding(4.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            val bitmap = remember(app.packageName) { app.icon.toBitmap(96, 96) }
            androidx.compose.foundation.Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = app.label,
                modifier = Modifier.size(64.dp),
            )
            Text(
                text = app.label,
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

private fun Drawable.toBitmap(width: Int, height: Int): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}

package com.tvperso.launcher

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

data class AppInfo(
    val label: String,
    val packageName: String,
    val icon: Drawable,
    val launchIntent: Intent,
)

fun queryTvApps(packageManager: PackageManager): List<AppInfo> {
    val leanbackIntent = Intent(Intent.ACTION_MAIN).apply {
        addCategory("android.intent.category.LEANBACK_LAUNCHER")
    }

    return packageManager
        .queryIntentActivities(leanbackIntent, PackageManager.MATCH_DEFAULT_ONLY)
        .mapNotNull { resolveInfo ->
            val activityInfo = resolveInfo.activityInfo ?: return@mapNotNull null
            val packageName = activityInfo.packageName

            val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
                ?: Intent(leanbackIntent).apply {
                    setClassName(packageName, activityInfo.name)
                }

            AppInfo(
                label = resolveInfo.loadLabel(packageManager).toString(),
                packageName = packageName,
                icon = resolveInfo.loadIcon(packageManager),
                launchIntent = launchIntent,
            )
        }
        .distinctBy { it.packageName }
        .sortedBy { it.label.lowercase() }
}

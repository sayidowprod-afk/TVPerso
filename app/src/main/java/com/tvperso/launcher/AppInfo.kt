package com.tvperso.launcher

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable

data class AppInfo(
    val label: String,
    val packageName: String,
    val icon: Drawable,
    val launchIntent: Intent,
)

/** Apps TV (déclarent LEANBACK_LAUNCHER). Vide sur un appareil sans aucune app TV installée. */
fun queryTvApps(packageManager: PackageManager): List<AppInfo> {
    val leanbackIntent = Intent(Intent.ACTION_MAIN).apply {
        addCategory("android.intent.category.LEANBACK_LAUNCHER")
    }
    return resolveApps(packageManager, leanbackIntent)
}

/** Toutes les apps normales (catégorie LAUNCHER) — utile pour tester le rendu sur téléphone. */
fun queryAllLauncherApps(packageManager: PackageManager): List<AppInfo> {
    val launcherIntent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
    }
    return resolveApps(packageManager, launcherIntent)
}

private fun resolveApps(packageManager: PackageManager, intent: Intent): List<AppInfo> {
    return packageManager
        .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        .mapNotNull { resolveInfo -> resolveInfo.toAppInfo(packageManager, intent) }
        .distinctBy { it.packageName }
        .sortedBy { it.label.lowercase() }
}

private fun ResolveInfo.toAppInfo(packageManager: PackageManager, sourceIntent: Intent): AppInfo? {
    val activityInfo = activityInfo ?: return null
    val packageName = activityInfo.packageName

    val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        ?: Intent(sourceIntent).apply {
            setClassName(packageName, activityInfo.name)
        }

    return AppInfo(
        label = loadLabel(packageManager).toString(),
        packageName = packageName,
        icon = loadIcon(packageManager),
        launchIntent = launchIntent,
    )
}

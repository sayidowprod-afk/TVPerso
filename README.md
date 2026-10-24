# TVPERSO

Launcher Android TV personnalisé, écrit avec **Jetpack Compose for TV**.

## Ce que fait ce squelette

- Remplace l'écran d'accueil d'Android TV (catégories `HOME` + `LEANBACK_LAUNCHER` dans le manifeste).
- Liste les applications installées compatibles TV (`LEANBACK_LAUNCHER`) dans une rangée focusable.
- Lance l'application sélectionnée au clic/validation télécommande.

## Structure

- `app/src/main/java/com/tvperso/launcher/MainActivity.kt` — écran d'accueil (Compose for TV).
- `app/src/main/java/com/tvperso/launcher/AppInfo.kt` — récupération des apps TV installées via `PackageManager`.
- `app/src/main/AndroidManifest.xml` — déclaration du launcher.

## Build

```bash
./gradlew assembleDebug
```

L'APK est généré dans `app/build/outputs/apk/debug/`.

## Installer sur une Android TV / émulateur

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

Puis, sur le device : **Paramètres > Applications > Application d'accueil par défaut**, choisir **TVPERSO**.

## Pistes d'évolution

- Rangées par catégorie (apps récentes, favoris, recommandations).
- Fond d'écran dynamique / horloge.
- Tri personnalisé et épinglage d'apps.
- Vrai banner/icône (320×180 pour le `android:banner` TV) au lieu du placeholder vectoriel actuel.

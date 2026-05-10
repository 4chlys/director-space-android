# FilmApp Cinema Theme Design

**Date:** 2026-05-10
**Status:** Approved

## Overview

Replace the default Material3 purple/pink template theme with a distinctive cinema-themed design. The current theme is invisible on Android 12+ devices because `dynamicColor = true` overrides all custom colors. This spec covers the full redesign: colors, typography, and multilanguage fixes.

## Design Decisions

- **Palette:** Velvet Screen — deep indigo background, violet-purple gradient accents, soft lavender text
- **Typography:** Bold Geometric — heavy weight + uppercase headlines, light weight body, high letter-spacing on labels
- **Fonts:** System fonts only (Roboto) — custom weights and letter-spacing achieve the bold geometric feel without downloading external fonts

## Color Tokens

| Role | Token name | Hex | Usage |
|---|---|---|---|
| Background / Surface | `VelvetBackground` | `#1C1B2E` | Screen background |
| Surface container | `VelvetContainer` | `#252438` | Cards, dialogs |
| Primary | `VelvetPrimary` | `#7B2D8B` | Buttons, FAB, active states |
| Secondary | `VelvetSecondary` | `#4A1FD4` | Gradient pair with primary |
| On-primary | `VelvetOnPrimary` | `#FFFFFF` | Text on primary buttons |
| Accent / on-surface | `VelvetAccent` | `#C084FC` | Labels, chips, icons |
| On-surface (bright) | `VelvetOnSurface` | `#E0D4F7` | Primary text |
| On-surface (muted) | `VelvetMuted` | `#A89EC9` | Secondary text, subtitles |
| Surface variant | `VelvetSurfaceVariant` | `#252438` | Input field backgrounds |
| On-surface variant | `VelvetOnSurfaceVariant` | `#A89EC9` | Placeholder / hint text |
| Error | `VelvetError` | `#CF6679` | Validation errors |
| On-error | `VelvetOnError` | `#FFFFFF` | Text on error elements |

The app is dark-mode only — no light scheme needed (cinema is a dark-room experience). `isSystemInDarkTheme()` always returns the dark scheme.

## Typography Scale

All styles use `FontFamily.Default` (Roboto). The Bold Geometric character comes from weight and letter-spacing, not a font file.

| Style | Weight | Size | Letter-spacing | Transform |
|---|---|---|---|---|
| `displayLarge` | Black (900) | 32sp | 0sp | Uppercase |
| `headlineMedium` | Black (900) | 24sp | 0.5sp | Uppercase |
| `titleLarge` | Bold (700) | 18sp | 1sp | Uppercase |
| `titleSmall` | Bold (700) | 13sp | 2sp | Uppercase |
| `bodyLarge` | Light (300) | 16sp | 0sp | Normal |
| `bodyMedium` | Light (300) | 14sp | 0sp | Normal |
| `labelLarge` | Bold (700) | 11sp | 2sp | Uppercase |
| `labelSmall` | Bold (700) | 9sp | 3sp | Uppercase |

## Files to Change

### `Color.kt`
- Delete all existing purple/pink tokens
- Add all Velvet tokens listed above

### `Theme.kt`
- Set `dynamicColor = false` (critical fix — currently overrides all custom colors on Android 12+)
- Remove light color scheme (use dark only)
- Wire `darkColorScheme()` with all Velvet tokens

### `Type.kt`
- Define all 8 type styles in the table above using `TextStyle`

### `AndroidManifest.xml`
- Add `android:localeConfig="@xml/locales_config"` to `<application>` tag

### `res/xml/locales_config.xml` (new file)
- Declare supported locales: `en` and `nl`

## Multilanguage Fix

Two issues prevent Android 13+ per-app language settings from working:

1. **Missing `res/xml/locales_config.xml`** — must list all supported locales
2. **Missing `android:localeConfig` in manifest** — points to the locales config

Both fixes are mechanical; the string resources (`values/` and `values-nl/`) are already correct and complete.

## Out of Scope

- Light mode / dynamic color support
- Custom downloadable fonts
- Changes to composable layout or logic

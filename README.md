# FilmApp — Director Space

An Android app built with Jetpack Compose that lets you browse a curated collection of notable film directors.
The app follows the Art Space codelab structure from the Android Basics in Compose course.

**Dataset:** Film directors and their filmographies  
**Main entity:** Director | **Sub-entity:** Film

---

## Release: week_0-1

### Features

- Browse 17 film directors (Kubrick, Lynch, Nolan, Tarkovsky, Scorsese, …) one at a time using Previous / Next navigation
- Each director card shows:
  - Portrait photo (loaded from TMDB via Coil)
  - Name and nationality
  - Birth date and country of origin
  - Career period (e.g. "Career: 1970 – 1999" or "Career: 1980 – present")
  - Rating out of 10 with an Active / Inactive badge
  - Number of films in the dataset (e.g. "🎬 3 films")
- Edit a director's rating via a validated dialog (0.0 – 10.0, decimal keyboard)
- Responsive layout: dedicated portrait and landscape compositions
- Counter indicator showing current position (e.g. "3 / 17")

### What works

- Full navigation through all 17 directors with correct Previous / Next button states (disabled at boundaries)
- Rating edit is validated, persists for the session, and survives screen rotation (ViewModel + StateFlow)
- Portrait layout: vertical stack (image → info card → controls)
- Landscape layout: image left, scrollable info + controls right
- Material3 dynamic colour theming with dark mode support
- All user-facing strings are string resources (including plurals for film count)
- Correct package structure (`be.kdg.filmapp`) aligned with build namespace

### What is notable / creative

- Real director portrait photos sourced from TMDB (`image.tmdb.org`) — the dataset feels like a real production app
- The Active / Inactive badge uses `primaryContainer` vs `errorContainer` colours from the Material3 colour scheme, giving immediate visual feedback without any custom colours
- Career period is derived from `careerStart` and `careerEnd` (nullable) and rendered differently for active vs retired directors
- Film count uses Android plural string resources (`<plurals>`) so "1 film" vs "2 films" is handled correctly
- Architecture follows the Unit 4 UI Layer pattern: `DirectorUiState` data class + `DirectorViewModel` with `StateFlow`, no mutable state leaks into composables

### Known problems / still struggling with

- The `db.json` file in the `data/` directory is not yet read by the app — data is hardcoded in `DataSource.kt`. Reading JSON at runtime is planned for a later week.
- Fields present in the data model (`bio`, `genres`) are not yet displayed in the UI.

### Not done

- Scrollable list screen using `LazyColumn` (Unit 3 content — planned for a later release)
- Film detail view (title, year, poster, awards)
- Persistent storage — rating edits are lost when the app is closed

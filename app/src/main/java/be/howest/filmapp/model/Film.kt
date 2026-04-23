package be.howest.filmapp.model

enum class Genre {
    Drama, Comedy, Thriller, Horror, SciFi, Action, Crime, Romance,
    Historical, Experimental, Mystery, Family
}

data class Film(
    val id: Int,
    val directorId: Int,
    val title: String,
    val releaseYear: Int,
    val rating: Double,
    val durationMinutes: Int,
    val genre: Genre,
    val inColor: Boolean,
    val awards: List<String>,
    val imageUrl: String
)

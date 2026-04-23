package be.howest.filmapp.model

data class Director(
    val id: Int,
    val name: String,
    val country: String,
    val birthDate: String,
    val careerStart: Int,
    val careerEnd: Int?,
    val active: Boolean,
    val rating: Double,
    val genres: List<Genre>,
    val imageUrl: String,
    val bio: String,
    val nationality: String
)

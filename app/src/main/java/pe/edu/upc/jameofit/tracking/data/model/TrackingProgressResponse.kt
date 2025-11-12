package pe.edu.upc.jameofit.tracking.data.model

data class TrackingProgressResponse(
    val consumed: MacronutrientValuesDto,
    val target: MacronutrientValuesDto
)
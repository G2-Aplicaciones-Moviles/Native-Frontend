package pe.edu.upc.jameofit.goals.data.model

import pe.edu.upc.jameofit.goals.model.DietPreset

data class DietTypeConfigRequest(
    val preset: String   // "OMNIVORE" | "VEGETARIAN" | "VEGAN" | "LOW_CARB" | "HIGH_PROTEIN" | "MEDITERRANEAN"
) {
    companion object {
        fun fromPreset(preset: DietPreset): DietTypeConfigRequest {
            return DietTypeConfigRequest(preset = preset.name)
        }
    }
}
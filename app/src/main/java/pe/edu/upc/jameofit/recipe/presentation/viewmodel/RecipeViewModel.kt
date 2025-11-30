package pe.edu.upc.jameofit.recipe.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.jameofit.recipe.data.model.CategoryResponse
import pe.edu.upc.jameofit.recipe.data.model.CreateRecipeRequest
import pe.edu.upc.jameofit.recipe.data.model.NutritionistResponse
import pe.edu.upc.jameofit.recipe.data.model.RecipeResponse
import pe.edu.upc.jameofit.recipe.data.model.RecipeTemplateResponse
import pe.edu.upc.jameofit.recipe.data.model.RecipeTypeResponse
import pe.edu.upc.jameofit.recipe.data.repository.RecipeRepository

class RecipeViewModel(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _recipes = MutableStateFlow<List<RecipeResponse>>(emptyList())
    val recipes: StateFlow<List<RecipeResponse>> = _recipes

    private val _selectedRecipe = MutableStateFlow<RecipeResponse?>(null)
    val selectedRecipe: StateFlow<RecipeResponse?> = _selectedRecipe

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _categories = MutableStateFlow<List<CategoryResponse>>(emptyList())
    val categories: StateFlow<List<CategoryResponse>> = _categories

    private val _recipeTypes = MutableStateFlow<List<RecipeTypeResponse>>(emptyList())
    val recipeTypes: StateFlow<List<RecipeTypeResponse>> = _recipeTypes
    private val _recipeName = MutableStateFlow("")
    val recipeName: StateFlow<String> = _recipeName

    private val _recipeDescription = MutableStateFlow("")
    val recipeDescription: StateFlow<String> = _recipeDescription

    private val _preparationTime = MutableStateFlow("")
    val preparationTime: StateFlow<String> = _preparationTime // Tiempo en minutos como String
    private val _allNutritionists = MutableStateFlow<List<NutritionistResponse>>(emptyList())
    val allNutritionists: StateFlow<List<NutritionistResponse>> = _allNutritionists
    private val _difficulty = MutableStateFlow("")
    val difficulty: StateFlow<String> = _difficulty
    private val _selectedCategory = MutableStateFlow<CategoryResponse?>(null)
    val selectedCategory: StateFlow<CategoryResponse?> = _selectedCategory
    private val _detailedTemplates = MutableStateFlow<List<RecipeTemplateResponse>>(emptyList())
    val detailedTemplates: StateFlow<List<RecipeTemplateResponse>> = _detailedTemplates

    private val _filterNutritionistId = MutableStateFlow<Long?>(null)
    val filterNutritionistId: StateFlow<Long?> = _filterNutritionistId
    private val _selectedRecipeType = MutableStateFlow<RecipeTypeResponse?>(null)
    val selectedRecipeType: StateFlow<RecipeTypeResponse?> = _selectedRecipeType
    private val _recipeCreationSuccess = MutableStateFlow<Boolean?>(null)
    val recipeCreationSuccess: StateFlow<Boolean?> = _recipeCreationSuccess
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadAllRecipes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _recipes.value = repository.getAllRecipes() ?: emptyList()
            } catch (e: Exception) {
                _error.value = "Error loading recipes: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadRecipeById(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _selectedRecipe.value = repository.getRecipeById(id)
            } catch (e: Exception) {
                _error.value = "Error loading recipe: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Método para crear una receta personal
    fun createRecipe(userId: Long, request: CreateRecipeRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.createRecipeForUser(userId, request)
                loadAllRecipes() // Refresh list
            } catch (e: Exception) {
                _error.value = "Error creating recipe: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Método para crear una plantilla (template)
    fun createRecipeTemplate(nutritionistId: Long, request: CreateRecipeRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.createRecipeForNutritionist(nutritionistId, request)
                loadAllRecipes() // Refresh list
            } catch (e: Exception) {
                _error.value = "Error creating template: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Método para asignar una plantilla a un usuario
    fun assignTemplate(recipeId: Int, profileId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val newRecipe = repository.assignTemplateToProfile(recipeId, profileId)
                // Opcionalmente, manejar el newRecipe
            } catch (e: Exception) {
                _error.value = "Error assigning template: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadRecipesByCategoryId(categoryId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Llama directamente al nuevo método del repositorio
                _recipes.value = repository.getRecipesByCategoryId(categoryId) ?: emptyList()
            } catch (e: Exception) {
                _error.value = "Error loading recipes by category: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateRecipeName(name: String) { _recipeName.value = name }
    fun updateRecipeDescription(description: String) { _recipeDescription.value = description }
    fun updatePreparationTime(time: String) { _preparationTime.value = time }
    fun updateDifficulty(difficulty: String) { _difficulty.value = difficulty }
    fun selectCategory(category: CategoryResponse) { _selectedCategory.value = category }
    fun selectRecipeType(recipeType: RecipeTypeResponse) { _selectedRecipeType.value = recipeType }
    fun resetRecipeCreationSuccess() { _recipeCreationSuccess.value = null }
    fun loadCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _categories.value = repository.getAllCategories() ?: emptyList()
            } catch (e: Exception) {
                _error.value = "Error loading categories: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 🆕 Función para cargar tipos de receta
    fun loadRecipeTypes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _recipeTypes.value = repository.getAllRecipeTypes() ?: emptyList()
            } catch (e: Exception) {
                _error.value = "Error loading recipe types: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 🆕 Función para crear una receta
    fun createRecipe(userId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _recipeCreationSuccess.value = null // Resetear antes de intentar
            _error.value = null

            // Validaciones básicas
            if (recipeName.value.isBlank() || recipeDescription.value.isBlank() ||
                preparationTime.value.isBlank() || difficulty.value.isBlank() ||
                selectedCategory.value == null || selectedRecipeType.value == null) {
                _error.value = "Todos los campos son obligatorios."
                _isLoading.value = false
                return@launch
            }

            val prepTimeInt = preparationTime.value.toIntOrNull()
            if (prepTimeInt == null || prepTimeInt <= 0) {
                _error.value = "El tiempo de preparación debe ser un número entero positivo."
                _isLoading.value = false
                return@launch
            }

            try {
                val request = CreateRecipeRequest(
                    name = recipeName.value,
                    description = recipeDescription.value,
                    preparationTime = prepTimeInt,
                    difficulty = difficulty.value,
                    categoryId = selectedCategory.value!!.id,
                    recipeTypeId = selectedRecipeType.value!!.id
                )

                val newRecipe = repository.createRecipeForUser(userId, request)
                if (newRecipe != null) {
                    _recipeCreationSuccess.value = true
                    // Limpiar formulario después de éxito
                    _recipeName.value = ""
                    _recipeDescription.value = ""
                    _preparationTime.value = ""
                    _difficulty.value = ""
                    _selectedCategory.value = null
                    _selectedRecipeType.value = null
                } else {
                    _error.value = "Error al crear la receta: respuesta nula."
                }
            } catch (e: Exception) {
                _error.value = "Error al crear la receta: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun resetErrorMessage() {
        _error.value = null
    }
    fun setFilterNutritionistId(id: Long?) {
        _filterNutritionistId.value = if (id == 0L) null else id
        loadTemplatesWithAuthor()
    }
    // Dentro de RecipeViewModel.kt
    fun loadAllNutritionists() {
        viewModelScope.launch {
            try {
                val loadedList = repository.getAllNutritionists() ?: emptyList()

                // 🆕 INYECCIÓN DE DATOS DE PRUEBA:
                val testList = mutableListOf<NutritionistResponse>()
                testList.addAll(loadedList)

                // Si la lista está vacía, añade un nutricionista de prueba
                if (testList.isEmpty()) {
                    testList.add(NutritionistResponse(id = 100L, name = "Dr. Test Dummy", userId = 99L))
                }

                _allNutritionists.value = testList.toList()
            } catch (e: Exception) {
                // ...
            }
        }
    }
    fun loadTemplatesWithAuthor() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val nutritionistId = _filterNutritionistId.value

                val result = if (nutritionistId != null && nutritionistId > 0L) {
                    repository.getTemplatesByNutritionistWithAuthor(nutritionistId)
                } else {
                    repository.getAllTemplatesWithAuthor()
                }

                _detailedTemplates.value = result ?: emptyList()

            } catch (e: Exception) {
                _error.value = "Error loading detailed templates: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
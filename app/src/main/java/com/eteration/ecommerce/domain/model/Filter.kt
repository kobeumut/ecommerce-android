package com.eteration.ecommerce.domain.model

/**
 * Domain model representing filter options
 */
data class Filter(
    val sortBy: SortType = SortType.OLD_TO_NEW,
    val selectedBrands: Set<String> = emptySet(),
    val selectedModels: Set<String> = emptySet()
)

/**
 * Enum for sort types
 */
enum class SortType {
    OLD_TO_NEW,
    NEW_TO_OLD,
    PRICE_LOW_TO_HIGH,
    PRICE_HIGH_TO_LOW
}
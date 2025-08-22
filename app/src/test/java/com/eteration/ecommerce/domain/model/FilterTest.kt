package com.eteration.ecommerce.domain.model

import org.junit.Assert.*
import org.junit.Test

class FilterTest {

    @Test
    fun `Filter should be created with default values`() {
        // When
        val filter = Filter()

        // Then
        assertEquals(SortType.OLD_TO_NEW, filter.sortBy)
        assertTrue(filter.selectedBrands.isEmpty())
        assertTrue(filter.selectedModels.isEmpty())
    }

    @Test
    fun `Filter should be created with custom values`() {
        // Given
        val sortBy = SortType.PRICE_HIGH_TO_LOW
        val selectedBrands = setOf("Apple", "Samsung")
        val selectedModels = setOf("iPhone 13", "Galaxy S22")

        // When
        val filter = Filter(sortBy, selectedBrands, selectedModels)

        // Then
        assertEquals(sortBy, filter.sortBy)
        assertEquals(selectedBrands, filter.selectedBrands)
        assertEquals(selectedModels, filter.selectedModels)
    }

    @Test
    fun `Filter should be created with only sortBy`() {
        // Given
        val sortBy = SortType.NEW_TO_OLD

        // When
        val filter = Filter(sortBy = sortBy)

        // Then
        assertEquals(sortBy, filter.sortBy)
        assertTrue(filter.selectedBrands.isEmpty())
        assertTrue(filter.selectedModels.isEmpty())
    }

    @Test
    fun `Filter should be created with only selectedBrands`() {
        // Given
        val selectedBrands = setOf("Apple", "Samsung")

        // When
        val filter = Filter(selectedBrands = selectedBrands)

        // Then
        assertEquals(SortType.OLD_TO_NEW, filter.sortBy)
        assertEquals(selectedBrands, filter.selectedBrands)
        assertTrue(filter.selectedModels.isEmpty())
    }

    @Test
    fun `Filter should be created with only selectedModels`() {
        // Given
        val selectedModels = setOf("iPhone 13", "Galaxy S22")

        // When
        val filter = Filter(selectedModels = selectedModels)

        // Then
        assertEquals(SortType.OLD_TO_NEW, filter.sortBy)
        assertTrue(filter.selectedBrands.isEmpty())
        assertEquals(selectedModels, filter.selectedModels)
    }

    @Test
    fun `SortType enum should have correct values`() {
        // Then
        assertEquals(4, SortType.values().size)
        assertNotNull(SortType.OLD_TO_NEW)
        assertNotNull(SortType.NEW_TO_OLD)
        assertNotNull(SortType.PRICE_LOW_TO_HIGH)
        assertNotNull(SortType.PRICE_HIGH_TO_LOW)
    }
}
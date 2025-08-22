package com.eteration.ecommerce.presentation.utils

import org.junit.Assert.*
import org.junit.Test

class ViewStateTest {

    @Test
    fun `Loading state should be created correctly`() {
        // When
        val loadingState = ViewState.Loading

        // Then
        assertTrue(loadingState is ViewState.Loading)
    }

    @Test
    fun `Success state should be created with correct data`() {
        // Given
        val data = listOf("item1", "item2", "item3")

        // When
        val successState = ViewState.Success(data)

        // Then
        assertTrue(successState is ViewState.Success)
        assertEquals(data, successState.data)
    }

    @Test
    fun `Error state should be created with correct message`() {
        // Given
        val errorMessage = "Something went wrong"

        // When
        val errorState = ViewState.Error(errorMessage)

        // Then
        assertTrue(errorState is ViewState.Error)
        assertEquals(errorMessage, errorState.message)
    }

    @Test
    fun `ViewState instances should be different`() {
        // Given
        val loadingState = ViewState.Loading
        val successState = ViewState.Success("data")
        val errorState = ViewState.Error("error")

        // Then
        assertNotEquals(loadingState, successState)
        assertNotEquals(loadingState, errorState)
        assertNotEquals(successState, errorState)
    }

    @Test
    fun `Success states with same data should be equal`() {
        // Given
        val data = "test data"
        val successState1 = ViewState.Success(data)
        val successState2 = ViewState.Success(data)

        // Then
        assertEquals(successState1, successState2)
    }

    @Test
    fun `Error states with same message should be equal`() {
        // Given
        val errorMessage = "error message"
        val errorState1 = ViewState.Error(errorMessage)
        val errorState2 = ViewState.Error(errorMessage)

        // Then
        assertEquals(errorState1, errorState2)
    }
}
package org.example.Barnes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BarnesAndNobleTest {

    // SPECIFICATION-BASED TESTS

    @Test
    @DisplayName("specification-based - returns null when order is null")
    void testGetPriceForCart_NullOrder() {
        BookDatabase mockDatabase = mock(BookDatabase.class);
        BuyBookProcess mockProcess = mock(BuyBookProcess.class);
        BarnesAndNoble bn = new BarnesAndNoble(mockDatabase, mockProcess);

        // According to spec, null input should return null
        PurchaseSummary result = bn.getPriceForCart(null);

        assertNull(result);
    }

    @Test
    @DisplayName("specification-based - calculates total price correctly for one book in cart")
    void testGetPriceForCart_ValidOrder() {
        BookDatabase mockDatabase = mock(BookDatabase.class);
        BuyBookProcess mockProcess = mock(BuyBookProcess.class);

        Book book = new Book("111", 10, 5);
        when(mockDatabase.findByISBN("111")).thenReturn(book);

        BarnesAndNoble bn = new BarnesAndNoble(mockDatabase, mockProcess);

        Map<String, Integer> order = new HashMap<>();
        order.put("111", 3); // buy 3 copies

        PurchaseSummary result = bn.getPriceForCart(order);

        assertNotNull(result);
        assertEquals(30, result.getTotalPrice()); // 3 * 10
        verify(mockProcess, times(1)).buyBook(book, 3);
    }

    // STRUCTURAL-BASED TESTS

    @Test
    @DisplayName("structural-based - handles case when requested quantity exceeds stock")
    void testGetPriceForCart_QuantityExceedsStock() {
        BookDatabase mockDatabase = mock(BookDatabase.class);
        BuyBookProcess mockProcess = mock(BuyBookProcess.class);

        // Book only has 2 copies available
        Book book = new Book("222", 15, 2);
        when(mockDatabase.findByISBN("222")).thenReturn(book);

        BarnesAndNoble bn = new BarnesAndNoble(mockDatabase, mockProcess);

        // Request 5 copies
        Map<String, Integer> order = new HashMap<>();
        order.put("222", 5);

        PurchaseSummary result = bn.getPriceForCart(order);

        assertNotNull(result);
        assertEquals(30, result.getTotalPrice()); // only 2 * 15
        assertFalse(result.getUnavailable().isEmpty());
        verify(mockProcess, times(1)).buyBook(book, 2);
    }

    @Test
    @DisplayName("structural-based - processes multiple ISBN entries in the order map")
    void testGetPriceForCart_MultipleBooks() {
        BookDatabase mockDatabase = mock(BookDatabase.class);
        BuyBookProcess mockProcess = mock(BuyBookProcess.class);

        Book bookA = new Book("A", 10, 5);
        Book bookB = new Book("B", 20, 5);

        when(mockDatabase.findByISBN("A")).thenReturn(bookA);
        when(mockDatabase.findByISBN("B")).thenReturn(bookB);

        BarnesAndNoble bn = new BarnesAndNoble(mockDatabase, mockProcess);

        Map<String, Integer> order = new HashMap<>();
        order.put("A", 2);
        order.put("B", 3);

        PurchaseSummary result = bn.getPriceForCart(order);

        assertNotNull(result);
        assertEquals(2 * 10 + 3 * 20, result.getTotalPrice());
        verify(mockProcess, times(1)).buyBook(bookA, 2);
        verify(mockProcess, times(1)).buyBook(bookB, 3);
    }
}

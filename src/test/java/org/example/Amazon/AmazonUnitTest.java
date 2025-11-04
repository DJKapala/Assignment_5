package org.example.Amazon;

import org.example.Amazon.Cost.ItemType;
import org.example.Amazon.Cost.PriceRule;
import org.example.Amazon.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AmazonUnitTest {

    private Amazon amazon;
    private ShoppingCart mockCart;
    private PriceRule mockRule1;
    private PriceRule mockRule2;

    @BeforeEach
    void setup() {
        mockCart = mock(ShoppingCart.class);
        mockRule1 = mock(PriceRule.class);
        mockRule2 = mock(PriceRule.class);

        // Amazon uses a list of mocked rules
        amazon = new Amazon(mockCart, List.of(mockRule1, mockRule2));
    }

    @Test
    @DisplayName("specification-based")
    void testCalculateTotalWithMockedRules() {
        Item item1 = new Item(ItemType.OTHER, "Book", 1, 10.0);
        Item item2 = new Item(ItemType.ELECTRONIC, "Laptop", 1, 1000.0);

        // Mock the shopping cart to return our items
        when(mockCart.getItems()).thenReturn(List.of(item1, item2));

        // Mock the rules to return fixed prices
        when(mockRule1.priceToAggregate(anyList())).thenReturn(100.0);
        when(mockRule2.priceToAggregate(anyList())).thenReturn(50.0);

        // Total should sum the mocked rule outputs
        double total = amazon.calculate();
        assertEquals(150.0, total, 0.001);

        // Verify each rule was called with the items
        verify(mockRule1).priceToAggregate(List.of(item1, item2));
        verify(mockRule2).priceToAggregate(List.of(item1, item2));
    }

    @Test
    @DisplayName("structural-based")
    void testAddToCartCallsCartAdd() {
        Item item = new Item(ItemType.OTHER, "Book", 2, 10.0);

        amazon.addToCart(item);

        // Verify add was called on the mock shopping cart
        verify(mockCart).add(item);
    }
}

package org.example.Amazon;

import org.example.Amazon.Cost.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AmazonIntegrationTest {

    private Amazon amazon;
    private ShoppingCartAdaptor cart;
    private Database database;

    @BeforeEach
    void setup() {
        database = new Database();
        database.resetDatabase();

        cart = new ShoppingCartAdaptor(database);

        // Use realistic pricing rules
        List<PriceRule> rules = List.of(
                new RegularCost(),
                new DeliveryPrice(),
                new ExtraCostForElectronics()
        );

        amazon = new Amazon(cart, rules);
    }

    @Test
    @DisplayName("specification-based")
    void testTotalPriceWithElectronicsSpec() {
        Item book = new Item(ItemType.OTHER, "Book", 2, 10.0);
        Item laptop = new Item(ItemType.ELECTRONIC, "Laptop", 1, 1000.0);

        amazon.addToCart(book);
        amazon.addToCart(laptop);

        // Calculation:
        // RegularCost: 2*10 + 1*1000 = 1020
        // DeliveryPrice: 2 items? Actually 2 items (not counting each unit?), 2+1 = 3? So delivery = 5
        // ExtraCostForElectronics: 7.5
        // Total = 1020 + 5 + 7.5 = 1032.5
        double total = amazon.calculate();
        assertEquals(1032.5, total, 0.001);
    }

    @Test
    @DisplayName("structural-based")
    void testCartReflectsItemsStructural() {
        Item book = new Item(ItemType.OTHER, "Book", 2, 10.0);
        amazon.addToCart(book);

        assertEquals(1, cart.getItems().size());
        assertEquals(book.getName(), cart.getItems().get(0).getName());
        assertEquals(book.getQuantity(), cart.getItems().get(0).getQuantity());
    }
}

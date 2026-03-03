package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CarTest {
    Car car;

    @BeforeEach
    void setUp() {
        this.car = new Car();
        this.car.setCarId("123e4567-e89b-12d3-a456-426614174000");
        this.car.setCarName("Toyota Kijang Innova");
        this.car.setCarColor("Black");
        this.car.setCarQuantity(5);
    }

    @Test
    void testGetCarId() {
        assertEquals("123e4567-e89b-12d3-a456-426614174000", this.car.getCarId());
    }

    @Test
    void testGetCarName() {
        assertEquals("Toyota Kijang Innova", this.car.getCarName());
    }

    @Test
    void testGetCarColor() {
        assertEquals("Black", this.car.getCarColor());
    }

    @Test
    void testGetCarQuantity() {
        assertEquals(5, this.car.getCarQuantity());
    }
}
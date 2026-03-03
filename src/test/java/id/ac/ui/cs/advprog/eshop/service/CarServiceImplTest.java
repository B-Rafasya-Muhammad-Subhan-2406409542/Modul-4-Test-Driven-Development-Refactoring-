package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Spy
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setCarId("123");
        car.setCarName("Mobil Test");
        car.setCarColor("Red");
        car.setCarQuantity(5);
    }

    @Test
    void testCreateAndFind() {
        carService.create(car);
        List<Car> allCars = carService.findAll();
        assertFalse(allCars.isEmpty());

        Car foundCar = carService.findById("123");
        assertEquals("Mobil Test", foundCar.getCarName());
    }

    @Test
    void testUpdate() {
        carService.create(car);

        Car editCar = new Car();
        editCar.setCarName("Mobil Edit");
        editCar.setCarQuantity(15);

        carService.update("123", editCar);
        Car result = carService.findById("123");
        assertEquals("Mobil Edit", result.getCarName());
        assertEquals(15, result.getCarQuantity());
    }

    @Test
    void testDelete() {
        carService.create(car);
        carService.deleteCarById("123"); // Sesuaikan jika kamu pakai deleteCarById atau deleteById di Service
        List<Car> allCars = carService.findAll();
        assertTrue(allCars.isEmpty());
    }
}
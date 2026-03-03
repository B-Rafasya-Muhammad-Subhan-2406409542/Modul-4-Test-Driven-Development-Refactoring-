package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CarRepositoryTest {

    @InjectMocks
    CarRepository carRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateAndFind() {
        Car car = new Car();
        car.setCarId("car-01");
        car.setCarName("Pajero");
        car.setCarColor("White");
        car.setCarQuantity(10);
        carRepository.create(car);

        Iterator<Car> carIterator = carRepository.findAll();
        assertTrue(carIterator.hasNext());
        Car savedCar = carIterator.next();
        assertEquals(car.getCarId(), savedCar.getCarId());
        assertEquals(car.getCarName(), savedCar.getCarName());
        assertEquals(car.getCarColor(), savedCar.getCarColor());
        assertEquals(car.getCarQuantity(), savedCar.getCarQuantity());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Car> carIterator = carRepository.findAll();
        assertFalse(carIterator.hasNext());
    }

    @Test
    void testFindById() {
        Car car = new Car();
        car.setCarName("Honda Civic");
        carRepository.create(car);

        Car foundCar = carRepository.findById(car.getCarId());
        assertNotNull(foundCar);
        assertEquals(car.getCarName(), foundCar.getCarName());
    }

    @Test
    void testFindByIdNotFound() {
        assertNull(carRepository.findById("random-id"));
    }

    @Test
    void testEditCar() {
        Car car = new Car();
        carRepository.create(car);

        Car editCar = new Car();
        editCar.setCarName("Avanza Updated");
        editCar.setCarColor("Silver");
        editCar.setCarQuantity(12);

        Car updatedCar = carRepository.update(car.getCarId(), editCar);

        assertNotNull(updatedCar);
        assertEquals("Avanza Updated", updatedCar.getCarName());
        assertEquals("Silver", updatedCar.getCarColor());
        assertEquals(12, updatedCar.getCarQuantity());
    }

    @Test
    void testEditCarNotFound() {
        Car editCar = new Car();
        editCar.setCarName("Ghost Car");
        Car updatedCar = carRepository.update("fake-id", editCar);
        assertNull(updatedCar);
    }

    @Test
    void testDeleteCar() {
        Car car = new Car();
        carRepository.create(car);

        carRepository.deleteCarById(car.getCarId());
        Iterator<Car> carIterator = carRepository.findAll();
        assertFalse(carIterator.hasNext());
    }
}
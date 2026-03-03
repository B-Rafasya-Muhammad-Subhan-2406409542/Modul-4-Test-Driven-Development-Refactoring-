package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @InjectMocks
    CarController carController;

    @Mock
    CarService carService;

    private static final String REDIRECT_CARLIST = "redirect:listCar";
    private Model model;

    @BeforeEach
    void setUp() {
        model = new ConcurrentModel();
    }

    @Test
    void testCreateCarPage() {
        String viewName = carController.createCarPage(model);
        assertEquals("CreateCar", viewName);
        assertTrue(model.containsAttribute("car"));
        assertInstanceOf(Car.class, model.getAttribute("car"));
    }

    @Test
    void testCreateCarPost() {
        Car car = new Car();
        String viewName = carController.createCarPost(car, model);
        assertEquals(REDIRECT_CARLIST, viewName);
    }

    @Test
    void testCarListPage() {
        when(carService.findAll()).thenReturn(mock(List.class));
        String viewName = carController.carListPage(model);
        assertEquals("CarList", viewName);
        assertTrue(model.containsAttribute("cars"));
    }

    @Test
    void testEditCarPage() {
        String carId = "car-01";
        Car car = new Car();
        car.setCarId(carId);
        when(carService.findById(carId)).thenReturn(car);

        String viewName = carController.editCarPage(carId, model);
        assertEquals("EditCar", viewName);
        assertEquals(car, model.getAttribute("car"));
    }

    @Test
    void testEditCarPost() {
        Car car = new Car();
        String viewName = carController.editCarPost(car, model);
        assertEquals(REDIRECT_CARLIST, viewName);
    }

    @Test
    void testDeleteCar() {
        String carId = "car-01";
        String viewName = carController.deleteCar(carId);
        assertEquals(REDIRECT_CARLIST, viewName);
    }
}
package com.example.sb_car_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class SbCar1Application {

	public static void main(String[] args) {
		SpringApplication.run(SbCar1Application.class, args);
	}

}

class Car {
    private final String id;
    private String brand;
    private String model;
    private String color;
    private String registerNumber;
    private int price;
    private int year;

    public Car() {
        this.id = UUID.randomUUID().toString();
    }

    public Car(String id, String brand, String model, String color, String registerNumber, int price, int year) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.registerNumber = registerNumber;
        this.price = price;
        this.year = year;
    }

    public Car(String brand, String model, String color, String registerNumber, int price, int year) {
        this(UUID.randomUUID().toString(), brand, model, color, registerNumber, price, year);
    }

    public String getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}

@RestController
@RequestMapping("/cars")
class RespApiCarController {
    private final List<Car> cars = new ArrayList<>();

    public RespApiCarController() {
        cars.addAll(List.of(
            new Car("Toyota", "Corolla", "silver", "BBA-3122", 2021, 32000)
        ));
    }

    @GetMapping
    public Iterable<Car> getCars() {
        return cars;
    }

    @GetMapping("/{id}")
    Optional<Car> getCarById(@PathVariable String id) {
        for (Car c : cars) {
            if (c.getId().equals(id)) {
                return Optional.of(c);
            }
        }
        return Optional.empty(); // Placer le retour en dehors de la boucle
    }

    @PostMapping
    Car postCar(@RequestBody Car car) {
        cars.add(car);
        return car;
    }

    @PutMapping("/{id}")
    ResponseEntity<Car> putCar(@PathVariable String id, @RequestBody Car car) {
        for (Car c : cars) {
            if (c.getId().equals(id)) {
                if (car.getBrand() != null) c.setBrand(car.getBrand());
                if (car.getModel() != null) c.setModel(car.getModel());
                if (car.getColor() != null) c.setColor(car.getColor());
                if (car.getRegisterNumber() != null) c.setRegisterNumber(car.getRegisterNumber());
                if (car.getPrice() > 0) c.setPrice(car.getPrice());
                if (car.getYear() > 0) c.setYear(car.getYear());

                return new ResponseEntity<>(car, HttpStatus.OK);

            }
        }
        return new ResponseEntity<>(postCar(car), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteCar(@PathVariable String id) {
        for (Car c : cars) {
            if (c.getId().equals(id)) {
                cars.remove(c);
                return ResponseEntity.ok().build(); // Retourne un code 200 si la suppression est réussie
            }
        }
        return ResponseEntity.notFound().build(); // Retourne un code 404 si l'ID n'existe pas
    }


}


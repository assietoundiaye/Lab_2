package com.example.sb_coffee_1;

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
public class SbCoffee1Application {

    public static void main(String[] args) {
        SpringApplication.run(SbCoffee1Application.class, args);
    }
}

class Coffee {
    private final String id;
    private String name;

    public Coffee(){
        this.id = UUID.randomUUID().toString();
    }
    public Coffee(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Coffee(String name) {
        this(UUID.randomUUID().toString(), name);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

@RestController
@RequestMapping("/coffees")
class RestApiCoffeeController {

    private final List<Coffee> coffees = new ArrayList<>();

    public RestApiCoffeeController() {
        coffees.addAll(List.of(
            new Coffee("Café Nescafé"),
            new Coffee("Café Touba"),
            new Coffee("Café Expresso"),
            new Coffee("Café Crème")
        ));
    }
    @GetMapping
    public Iterable<Coffee> getCoffees() {
        return coffees;
    }

    @GetMapping("/{id}")
    Optional<Coffee> getCoffeeById(@PathVariable String id) {
        for (Coffee c : coffees) {
            if (c.getId().equals(id)) {
                return Optional.of(c);
            }
        }
        return Optional.empty(); // Placer le retour en dehors de la boucle
    }

    @PostMapping
    Coffee postCoffee(@RequestBody Coffee coffee) {
        coffees.add(coffee);
        return coffee;
    }

    @PutMapping("/{id}")
    ResponseEntity<Coffee> putCoffee(@PathVariable String id, @RequestBody Coffee coffee) {
        int coffeeIndex = -1;
        for (Coffee c : coffees) {
            if (c.getId().equals(id)) {
                coffeeIndex= coffees.indexOf(c);
                coffees.set(coffeeIndex, coffee);
            }
        }
        return (coffeeIndex == -1 ) ?
            new ResponseEntity<>(postCoffee(coffee), HttpStatus.CREATED) :
            new ResponseEntity<>(coffee,HttpStatus.OK);

    }
    @DeleteMapping("/{id}")
    void deleteCoffee(@PathVariable String id) {
        coffees.removeIf(c -> c.getId().equals(id));
    }

}


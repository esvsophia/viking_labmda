package ru.mephi.vikingdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingLambdaService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vikings/analytics")
@Tag(name = "Viking Analytics", description = "Аналитика викингов через лямбда-выражения")
public class VikingLambdaController {

    private final VikingLambdaService lambdaService;

    public VikingLambdaController(VikingLambdaService lambdaService) {
        this.lambdaService = lambdaService;
    }

    @GetMapping("/count/older")
    @Operation(summary = "Количество викингов старше определенного возраста")
    public long countOlderThan(@RequestParam int age) {
        return lambdaService.countOlderThan(age);
    }

    @GetMapping("/count/younger")
    @Operation(summary = "Количество викингов младше определенного возраста")
    public long countYoungerThan(@RequestParam int age) {
        return lambdaService.countYoungerThan(age);
    }

    @GetMapping("/count/age-range")
    @Operation(summary = "Количество викингов в диапазоне возраста")
    public long countInAgeRange(@RequestParam int min, @RequestParam int max) {
        return lambdaService.countInAgeRange(min, max);
    }

    @GetMapping("/count/age-outside")
    @Operation(summary = "Количество викингов вне диапазона возраста")
    public long countOutsideAgeRange(@RequestParam int min, @RequestParam int max) {
        return lambdaService.countOutsideAgeRange(min, max);
    }

    @GetMapping("/count/beard-hair")
    @Operation(summary = "Количество викингов по форме бороды и цвету волос")
    public long countByBeardAndHair(@RequestParam BeardStyle beard, @RequestParam HairColor hair) {
        return lambdaService.countByBeardAndHair(beard, hair);
    }

    @GetMapping("/count/axes")
    @Operation(summary = "Количество викингов с заданным числом топоров (1 или 2)")
    public long countByAxes(@RequestParam int quantity) {
        return lambdaService.countByAxeQuantity(quantity);
    }

    @GetMapping("/tall-random")
    @Operation(summary = "Случайный викинг ростом выше 180")
    public Optional<Viking> getRandomTallViking() {
        return lambdaService.getRandomVikingTallerThan180();
    }

    @GetMapping("/legendary")
    @Operation(summary = "Все викинги с легендарным снаряжением")
    public List<Viking> getLegendary() {
        return lambdaService.getVikingsWithLegendaryEquipment();
    }

    @GetMapping("/sorted-red-bearded")
    @Operation(summary = "Сортированный по возрасту список рыжебородых викингов")
    public List<Viking> getSortedRedBearded() {
        return lambdaService.getSortedRedBeardedVikings();
    }

    @PostMapping("/array/max-id")
    @Operation(summary = "Найти максимальный ID из переданного массива")
    public Integer getMaxId(@RequestBody Integer[] ids) {
        return lambdaService.findMaxId(ids);
    }

    @PostMapping("/array/even-ids")
    @Operation(summary = "Найти все четные ID из переданного массива")
    public List<Integer> getEvenIds(@RequestBody Integer[] ids) {
        return lambdaService.findAllEvenIds(ids);
    }

    @GetMapping("/db/max-id")
    @Operation(summary = "Найти максимальный ID среди текущих записей в БД")
    public Integer getMaxIdFromDb() {
        return lambdaService.findMaxId(lambdaService.getAllIdsFromDb());
    }

    @GetMapping("/db/even-ids")
    @Operation(summary = "Найти все четные ID среди текущих записей в БД")
    public List<Integer> getEvenIdsFromDb() {
        return lambdaService.findAllEvenIds(lambdaService.getAllIdsFromDb());
    }
}
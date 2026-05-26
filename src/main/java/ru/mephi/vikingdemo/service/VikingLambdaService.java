package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.repository.VikingStorage;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.Random;

@Service
public class VikingLambdaService {

    private final VikingStorage vikingStorage;
    private final Random random = new Random();

    public VikingLambdaService(VikingStorage vikingStorage) {
        this.vikingStorage = vikingStorage;
    }

    public long countOlderThan(int age) {
        return Arrays.stream(vikingStorage.findAll())
                .filter(v -> v.age() > age)
                .count();
    }

    public long countYoungerThan(int age) {
        return Arrays.stream(vikingStorage.findAll())
                .filter(v -> v.age() < age)
                .count();
    }

    public long countEqualAge(int age) {
        return Arrays.stream(vikingStorage.findAll())
                .filter(v -> v.age() == age)
                .count();
    }

    public long countInAgeRange(int minAge, int maxAge) {
        return Arrays.stream(vikingStorage.findAll())
                .filter(v -> v.age() >= minAge && v.age() <= maxAge)
                .count();
    }

    public long countOutsideAgeRange(int minAge, int maxAge) {
        return Arrays.stream(vikingStorage.findAll())
                .filter(v -> v.age() < minAge || v.age() > maxAge)
                .count();
    }

    public long countByBeardAndHair(BeardStyle beardStyle, HairColor hairColor) {
        return Arrays.stream(vikingStorage.findAll())
                .filter(v -> v.beardStyle() != null && v.beardStyle() == beardStyle && v.hairColor() == hairColor)
                .count();
    }

    public long countByAxeQuantity(int quantity) {
        return Arrays.stream(vikingStorage.findAll())
                .filter(v -> v.equipment().stream()
                        .filter(e -> e.name().equalsIgnoreCase("Axe"))
                        .count() == quantity)
                .count();
    }

    public long countWithOneOrTwoAxes() {
        return Arrays.stream(vikingStorage.findAll())
                .filter(v -> {
                    long axes = v.equipment().stream()
                            .filter(e -> e.name().equalsIgnoreCase("Axe"))
                            .count();
                    return axes == 1 || axes == 2;
                })
                .count();
    }

    public Optional<Viking> getRandomVikingTallerThan180() {
        Viking[] tallVikings = Arrays.stream(vikingStorage.findAll())
                .filter(v -> v.heightCm() > 180)
                .toArray(Viking[]::new);
        if (tallVikings.length == 0) {
            return Optional.empty();
        }
        return Optional.of(tallVikings[random.nextInt(tallVikings.length)]);
    }

    public Viking[] getVikingsWithLegendaryEquipment() {
        return Arrays.stream(vikingStorage.findAll())
                .filter(v -> v.equipment().stream()
                        .anyMatch(e -> e.quality().equalsIgnoreCase("Legendary")))
                .toArray(Viking[]::new);
    }

    public Viking[] getSortedRedBeardedVikings() {
        return Arrays.stream(vikingStorage.findAll())
                .filter(v -> v.hairColor() == HairColor.Red)
                .sorted(Comparator.comparingInt(Viking::age))
                .toArray(Viking[]::new);
    }

    public Integer findMaxId() {
        return Arrays.stream(vikingStorage.getAllID())
                .max(Comparator.naturalOrder())
                .orElse(null);
    }

    public Integer[] findAllEvenIds() {
        return Arrays.stream(vikingStorage.getAllID())
                .filter(id -> id % 2 == 0)
                .toArray(Integer[]::new);
    }
}

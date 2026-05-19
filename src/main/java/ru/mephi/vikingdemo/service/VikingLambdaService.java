package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.repository.VikingStorage;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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
        return vikingStorage.findAll().stream()
                .filter(v -> v.age() > age)
                .count();
    }

    public long countYoungerThan(int age) {
        return vikingStorage.findAll().stream()
                .filter(v -> v.age() < age)
                .count();
    }

    public long countEqualAge(int age) {
        return vikingStorage.findAll().stream()
                .filter(v -> v.age() == age)
                .count();
    }

    public long countInAgeRange(int minAge, int maxAge) {
        return vikingStorage.findAll().stream()
                .filter(v -> v.age() >= minAge && v.age() <= maxAge)
                .count();
    }

    public long countOutsideAgeRange(int minAge, int maxAge) {
        return vikingStorage.findAll().stream()
                .filter(v -> v.age() < minAge || v.age() > maxAge)
                .count();
    }

    public long countByBeardAndHair(BeardStyle beardStyle, HairColor hairColor) {
        return vikingStorage.findAll().stream()
                .filter(v -> v.beardStyle() == beardStyle && v.hairColor() == hairColor)
                .count();
    }

    public long countWithOneOrTwoAxes() {
        return vikingStorage.findAll().stream()
                .filter(v -> {
                    long axes = v.equipment().stream()
                            .filter(e -> e.name().equalsIgnoreCase("Axe") 
                            .count();
                    return axes == 1 || axes == 2;
                })
                .count();
    }

    public Optional<Viking> getRandomVikingTallerThan180() {
        List<Viking> tallVikings = vikingStorage.findAll().stream()
                .filter(v -> v.heightCm() > 180)
                .toList();
        if (tallVikings.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(tallVikings.get(random.nextInt(tallVikings.size())));
    }

    public List<Viking> getVikingsWithLegendaryEquipment() {
        return vikingStorage.findAll().stream()
                .filter(v -> v.equipment().stream()
                        .anyMatch(e -> e.quality().equalsIgnoreCase("Legendary")))
                .toList();
    }

    public List<Viking> getSortedRedBeardedVikings() {
        return vikingStorage.findAll().stream()
                .filter(v -> v.hairColor() == HairColor.Red)
                .sorted(Comparator.comparingInt(Viking::age))
                .toList();
    }

    public Integer findMaxId(Integer[] ids) {
        return Arrays.stream(ids)
                .max(Comparator.naturalOrder())
                .orElse(null);
    }

    public List<Integer> findAllEvenIds(Integer[] ids) {
        return Arrays.stream(ids)
                .filter(id -> id % 2 == 0)
                .toList();
    }

    public Integer[] getAllIdsFromDb() {
        return vikingStorage.findAll().stream()
                .map(Viking::id)
                .filter(java.util.Objects::nonNull)
                .toArray(Integer[]::new);
    }
}

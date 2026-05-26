package ru.mephi.vikingdemo.repository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.mephi.vikingdemo.model.EquipmentItem;
import ru.mephi.vikingdemo.model.EquipmentItemEntity;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.model.VikingEntity;

@Repository
public class VikingStorage {

    private final VikingRepository vikingRepository;
    private final EquipmentItemRepository equipmentItemRepository;
    private final VikingMapper vikingMapper;

    public VikingStorage(
            VikingRepository vikingRepository,
            EquipmentItemRepository equipmentItemRepository,
            VikingMapper vikingMapper
    ) {
        this.vikingRepository = vikingRepository;
        this.equipmentItemRepository = equipmentItemRepository;
        this.vikingMapper = vikingMapper;
    }

    @Transactional
    public Viking save(Viking viking) {
        Integer vikingId = vikingRepository.save(
                vikingMapper.toVikingEntity(viking)
        );

        for (EquipmentItem item : viking.equipment()) {
            equipmentItemRepository.save(
                    vikingMapper.toEquipmentItemEntity(vikingId, item)
            );
        }

        return viking;
    }

    public Viking[] findAll() {
        VikingEntity[] vikingEntities = vikingRepository.findAll();
        EquipmentItemEntity[] equipmentEntities = equipmentItemRepository.findAll();

        Map<Integer, List<EquipmentItemEntity>> equipmentByVikingId = Arrays.stream(equipmentEntities)
                .collect(Collectors.groupingBy(EquipmentItemEntity::vikingId));

        return Arrays.stream(vikingEntities)
                .map(vikingEntity -> vikingMapper.toViking(
                        vikingEntity,
                        equipmentByVikingId.getOrDefault(vikingEntity.id(), List.of())
                ))
                .toArray(Viking[]::new);
    }

    @Transactional
    public void update(Viking viking) {
        vikingRepository.update(vikingMapper.toVikingEntity(viking));
    }

    @Transactional
    public void deleteById(int id) {
        vikingRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        vikingRepository.deleteAll();
    }

    public Integer[] getAllID() {
        return Arrays.stream(vikingRepository.findAll())
                .map(VikingEntity::id)
                .filter(java.util.Objects::nonNull)
                .toArray(Integer[]::new);
    }
}

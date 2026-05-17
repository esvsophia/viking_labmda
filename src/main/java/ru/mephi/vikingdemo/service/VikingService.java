package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.Viking;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import ru.mephi.vikingdemo.repository.VikingStorage;

@Service
public class VikingService {
    private final VikingFactory vikingFactory;
    private final VikingStorage vikingStorage;
    
    
    @Autowired
    public VikingService(
            VikingFactory vikingFactory,
            VikingStorage vikingStorage
    ) {
        this.vikingFactory = vikingFactory;
        this.vikingStorage = vikingStorage;
    }
    
    public List<Viking> findAll() {
        return vikingStorage.findAll();
    }

    public void update(Viking viking) {
        vikingStorage.update(viking);
    }

    public Viking save(Viking viking) {
        return vikingStorage.save(viking);
    }

    public Viking createRandomViking() {
        Viking viking = vikingFactory.createRandomViking();
        return vikingStorage.save(viking);
    }

    public void deleteById(int id) {
        vikingStorage.deleteById(id);
    }

    public void deleteAll() {
        vikingStorage.deleteAll();
    }
}

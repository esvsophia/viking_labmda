package ru.mephi.vikingdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

import java.util.List;

@RestController
@RequestMapping("/api/vikings")
@Tag(name = "Vikings", description = "Операции с викингами")
public class VikingController {
    private final VikingService vikingService;
    private VikingListener vikingListener;

    public VikingController(VikingService vikingService, VikingListener vikingListener) {
        this.vikingService = vikingService;
        this.vikingListener = vikingListener;
    }

    @PostMapping("/post")
    @Operation(summary = "Создать викинга со случайными параметрами",
            operationId = "post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинг успешно создан")
    })
    public void addRndmViking(){
        vikingListener.testAdd();
    }

    @PostMapping
    @Operation(summary = "Добавить конкретного викинга")
    public Viking addViking(@RequestBody Viking viking) {
        return vikingService.save(viking);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить параметры викинга")
    public void updateViking(@PathVariable int id, @RequestBody Viking viking) {
        Viking toUpdate = new Viking(id, viking.name(), viking.age(),
                viking.heightCm(), viking.hairColor(),
                viking.beardStyle(), viking.equipment());
        vikingService.update(toUpdate);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить викинга")
    public void deleteViking(@PathVariable int id) {
        vikingService.deleteById(id);
    }
    
    @GetMapping
    @Operation(summary = "Получить список созданных викингов", 
            operationId = "getAllVikings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список успешно получен")
    })
    public List<Viking> getAllVikings() {
        System.out.println("GET /api/vikings called");
        return vikingService.findAll();
    }

    @GetMapping("/test")
    @Operation(summary = "Получить список тестовых викингов", 
            operationId = "getTest")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список успешно получен")
    })

    public List<String> test() {
        System.out.println("GET /api/vikings/test called");
        return List.of("Ragnar", "Bjorn");
    }
}

package ru.mephi.vikingdemo.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record EquipmentItem(
        @Schema(description = "Название предмета", example = "Iron Axe")
        String name,
        @Schema(description = "Редкость или качество", example = "Rare")
        String quality
) {
@Override
public String toString() {
    return name + " [" + quality + "]";
}
}

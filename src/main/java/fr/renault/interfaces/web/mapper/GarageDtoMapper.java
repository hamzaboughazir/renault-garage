package fr.renault.interfaces.web.mapper;

import fr.renault.domain.model.*;
import fr.renault.interfaces.web.dto.*;

import java.util.*;
import java.util.stream.Collectors;

public class GarageDtoMapper {
    public static Garage toDomain(GarageDto dto) {
        Garage garage = Garage.builder()
                .id(dto.id())
                .name(dto.name())
                .address(dto.address())
                .telephone(dto.telephone())
                .email(dto.email())
                .build();
        if (dto.openingHours()!=null) {
            Map<java.time.DayOfWeek, List<OpeningTime>> map = new HashMap<>();
            dto.openingHours().forEach((k, v) -> map.put(k, v.stream()
                    .map(o -> OpeningTime.builder().startTime(o.startTime()).endTime(o.endTime()).build())
                    .collect(Collectors.toList())));
            garage.setOpeningHours(map);
        }
        if (dto.vehicles()!=null) {
            List<Vehicle> vl = new ArrayList<>();
            for (VehicleDto v : dto.vehicles()) {
                Vehicle vd = Vehicle.builder()
                        .id(v.id())
                        .brand(v.brand())
                        .model(v.model())
                        .yearOfManufacture(v.yearOfManufacture())
                        .fuelType(FuelType.valueOf(v.fuelType().toUpperCase()))
                        .build();
                if (v.accessories()!=null) {
                    List<Accessory> al = new ArrayList<>();
                    for (AccessoryDto a : v.accessories()) {
                        al.add(Accessory.builder()
                                .id(a.id())
                                .name(a.name())
                                .description(a.description())
                                .price(a.price())
                                .type(AccessoryType.valueOf(a.type().toUpperCase()))
                                .build());
                    }
                    vd.setAccessories(al);
                }
                vl.add(vd);
            }
            garage.setVehicles(vl);
        }
        return garage;
    }

    public static GarageDto toDto(Garage garage) {
        Map<java.time.DayOfWeek, List<OpeningTimeDto>> openingT = null;
        if (garage.getOpeningHours()!=null) {

            final Map<java.time.DayOfWeek, List<OpeningTimeDto>> tmp = new HashMap<>();
            garage.getOpeningHours().forEach((k, v) -> tmp.put(k, v.stream()
                    .map(o -> new OpeningTimeDto(o.getStartTime(), o.getEndTime()))
                    .collect(Collectors.toList())));
            openingT = tmp;
        }
        List<VehicleDto> vehicleD = null;
        if (garage.getVehicles()!=null) {
            vehicleD = new ArrayList<>();
            for (Vehicle v : garage.getVehicles()) {
                List<AccessoryDto> accessoryD = null;
                if (v.getAccessories()!=null) {
                    accessoryD = v.getAccessories().stream()
                            .map(accessory -> new AccessoryDto(accessory.getId(), accessory.getName(), accessory.getDescription(), accessory.getPrice(), accessory.getType().name()))
                            .toList();
                }
                vehicleD.add(new VehicleDto(v.getId(), v.getBrand(), v.getModel(), v.getYearOfManufacture(), v.getFuelType().name(), accessoryD));
            }
        }
        return new GarageDto(garage.getId(), garage.getName(), garage.getAddress(), garage.getTelephone(), garage.getEmail(), openingT, vehicleD);
    }
}

package fr.renault.infrastructure.persistence.mapper;

import fr.renault.domain.model.*;
import fr.renault.infrastructure.persistence.jpa.entity.*;
import java.util.*;
import java.util.stream.Collectors;

public class GarageMapper {

    public static GarageEntity toEntity(Garage domain) {
        GarageEntity garageE = GarageEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .address(domain.getAddress())
                .telephone(domain.getTelephone())
                .email(domain.getEmail())
                .build();
        if (domain.getOpeningHours()!=null) {
            List<OpeningTimeEmbeddable> ohList = new ArrayList<>();
            domain.getOpeningHours().forEach((day, list) -> {
                list.stream()
                        .map(ot -> OpeningTimeEmbeddable.builder()
                                .dayOfWeek(day)
                                .startTime(ot.getStartTime())
                                .endTime(ot.getEndTime())
                                .build())
                        .forEach(ohList::add);
            });
            garageE.setOpeningHours(ohList);
        }
        if (domain.getVehicles()!=null) {
            List<VehicleEntity> ves = new ArrayList<>();
            for (Vehicle v : domain.getVehicles()) {
                VehicleEntity ve = VehicleEntity.builder()
                        .id(v.getId())
                        .brand(v.getBrand())
                        .model(v.getModel())
                        .yearOfManufacture(v.getYearOfManufacture())
                        .fuelType(v.getFuelType())
                        .garage(garageE)
                        .build();
                if (v.getAccessories()!=null) {
                    List<AccessoryEntity> aes = new ArrayList<>();
                    for (Accessory a : v.getAccessories()) {
                        aes.add(AccessoryEntity.builder()
                                .id(a.getId())
                                .name(a.getName())
                                .description(a.getDescription())
                                .price(a.getPrice())
                                .type(a.getType())
                                .vehicle(ve)
                                .build());
                    }
                    ve.setAccessories(aes);
                }
                ves.add(ve);
            }
            garageE.setVehicles(ves);
        }
        return garageE;
    }

    public static Garage toDomain(GarageEntity garageE) {
        Garage garage = Garage.builder()
                .id(garageE.getId())
                .name(garageE.getName())
                .address(garageE.getAddress())
                .telephone(garageE.getTelephone())
                .email(garageE.getEmail())
                .build();
        if (garageE.getOpeningHours()!=null) {
            Map<java.time.DayOfWeek, List<OpeningTime>> map = garageE.getOpeningHours().stream()
                    .collect(Collectors.groupingBy(OpeningTimeEmbeddable::getDayOfWeek,
                            Collectors.mapping(ot -> OpeningTime.builder().startTime(ot.getStartTime()).endTime(ot.getEndTime()).build(), Collectors.toList())));
            garage.setOpeningHours(map);
        }
        if (garageE.getVehicles()!=null) {
            List<Vehicle> vs = new ArrayList<>();
            for (VehicleEntity ve : garageE.getVehicles()) {
                Vehicle v = Vehicle.builder()
                        .id(ve.getId())
                        .brand(ve.getBrand())
                        .model(ve.getModel())
                        .yearOfManufacture(ve.getYearOfManufacture())
                        .fuelType(ve.getFuelType())
                        .build();
                if (ve.getAccessories()!=null) {
                    List<Accessory> as = new ArrayList<>();
                    for (AccessoryEntity ae : ve.getAccessories()) {
                        as.add(Accessory.builder()
                                .id(ae.getId())
                                .name(ae.getName())
                                .description(ae.getDescription())
                                .price(ae.getPrice())
                                .type(ae.getType())
                                .build());
                    }
                    v.setAccessories(as);
                }
                vs.add(v);
            }
            garage.setVehicles(vs);
        }
        return garage;
    }
}

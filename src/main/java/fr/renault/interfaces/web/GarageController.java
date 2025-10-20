package fr.renault.interfaces.web;

import fr.renault.application.GarageService;
import fr.renault.domain.model.FuelType;
import fr.renault.domain.model.Vehicle;
import fr.renault.interfaces.web.dto.GarageDto;
import fr.renault.interfaces.web.dto.VehicleDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fr.renault.interfaces.web.mapper.*;

import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "Garages")
@RestController
@RequestMapping("/api/garages")
@RequiredArgsConstructor
public class GarageController {

    private final GarageService service;

    @GetMapping
    public ResponseEntity<?> list(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "name") String sortBy) {
        List<GarageDto> all = service.listAll().stream()
                .sorted(Comparator.comparing(g -> g.getName()!=null? g.getName() : ""))
                .map(GarageDtoMapper::toDto).toList();
        int from = Math.min(page*size, all.size());
        int to = Math.min(from+size, all.size());
        return ResponseEntity.ok(new PageImpl<>(all.subList(from, to), PageRequest.of(page, size, Sort.by(sortBy)), all.size()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GarageDto> get(@PathVariable UUID id) {
        return service.get(id).map(GarageDtoMapper::toDto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GarageDto> create(@Valid @RequestBody GarageDto dto) {
        var created = service.create(GarageDtoMapper.toDomain(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(GarageDtoMapper.toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GarageDto> update(@PathVariable UUID id, @Valid @RequestBody GarageDto dto) {
        var updated = service.update(id, GarageDtoMapper.toDomain(dto));
        return ResponseEntity.ok(GarageDtoMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/vehicles")
    public ResponseEntity<VehicleDto> addVehicle(@PathVariable("id") UUID id, @Valid @RequestBody VehicleDto dto) {
        Vehicle v = Vehicle.builder()
                .id(dto.id())
                .brand(dto.brand())
                .model(dto.model())
                .yearOfManufacture(dto.yearOfManufacture())
                .fuelType(FuelType.valueOf(dto.fuelType().toUpperCase()))
                .build();
        Vehicle created = service.addVehicle(id, v);
        return ResponseEntity.status(HttpStatus.CREATED).body(new VehicleDto(created.getId(), created.getBrand(), created.getModel(), created.getYearOfManufacture(), created.getFuelType().name(), List.of()));
    }

    @GetMapping("/{id}/vehicles")
    public List<VehicleDto> listVehicles(@PathVariable UUID id) {
        return service.listVehicles(id).stream()
                .map(v -> new VehicleDto(v.getId(), v.getBrand(), v.getModel(), v.getYearOfManufacture(), v.getFuelType().name(), null))
                .collect(Collectors.toList());
    }

    @GetMapping("/vehicles/by-model/{model}")
    public List<VehicleDto> listByModel(@PathVariable String model) {
        return service.listVehiclesByModel(model).stream()
                .map(v -> new VehicleDto(v.getId(), v.getBrand(), v.getModel(), v.getYearOfManufacture(), v.getFuelType().name(), null))
                .collect(Collectors.toList());
    }

    @GetMapping("/search/by-fuel-type/{fuelType}")
    public List<GarageDto> searchByFuelType(@PathVariable String fuelType) {
        return service.searchByFuelType(FuelType.valueOf(fuelType.toUpperCase())).stream().map(GarageDtoMapper::toDto).toList();
    }

    @GetMapping("/search/by-accessory")
    public List<GarageDto> searchByAccessory(@RequestParam String name) {
        return service.searchByAccessoryName(name).stream().map(GarageDtoMapper::toDto).toList();
    }
}

package fr.renault.application;

import fr.renault.domain.model.*;
import fr.renault.domain.ports.GarageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GarageServiceTest {

    @Mock
    private GarageRepository repo;

    @Mock
    private VehiclePublisher publisher;

    @InjectMocks
    private GarageService service;

    private UUID garageId;
    private Garage garage;

    @BeforeEach
    void setup() {
        garageId = UUID.randomUUID();
        garage = Garage.builder().id(garageId).name("Test Garage").build();
        when(repo.findById(garageId)).thenReturn(Optional.of(garage));
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void createGarage() {
        Garage newGarage = Garage.builder().name("Nouveau Garage").address("123 Rue Test").build();
        when(repo.save(any())).thenReturn(newGarage);

        Garage garage = service.create(newGarage);

        assertNotNull(garage);
        verify(repo).save(newGarage);
    }

    @Test
    void listAllGarages() {
        List<Garage> garages = List.of(garage, Garage.builder().id(UUID.randomUUID()).name("Garage 2").build());
        when(repo.findAll()).thenReturn(garages);

        List<Garage> result = service.listAll();

        assertEquals(2, result.size());
        verify(repo).findAll();
    }

    @Test
    void getGarageById() {
        Optional<Garage> result = service.get(garageId);

        assertTrue(result.isPresent());
        assertEquals("Test Garage", result.get().getName());
        verify(repo).findById(garageId);
    }


    @Test
    void updateGarage() {
        Garage updateData = Garage.builder().name("Nom Modifié").address("Nouvelle Adresse").build();

        Garage result = service.update(garageId, updateData);

        assertEquals("Nom Modifié", result.getName());
        assertEquals("Nouvelle Adresse", result.getAddress());
        verify(repo).save(garage);
    }

    @Test
    void deleteGarage() {
        service.delete(garageId);

        verify(repo).deleteById(garageId);
    }

    @Test
    void addVehicle() {
        Vehicle vehicle = Vehicle.builder()
            .brand("Renault")
            .model("Clio")
            .yearOfManufacture(2023)
            .fuelType(FuelType.ESSENCE)
            .build();

        Vehicle result = service.addVehicle(garageId, vehicle);

        assertNotNull(result.getId());
        assertEquals("Renault", result.getBrand());
        assertTrue(garage.getVehicles().contains(result));
        verify(repo).save(garage);
        verify(publisher).publishVehicleCreated(garageId, result);
    }


    @Test
    void respectMaxVehicleLimit() {

        for (int i = 0; i < 50; i++) {
            garage.getVehicles().add(Vehicle.builder()
                .id(UUID.randomUUID())
                .brand("Renault")
                .model("Dacia")
                .yearOfManufacture(2020)
                .fuelType(FuelType.ESSENCE)
                .build());
        }

        Vehicle newVehicle = Vehicle.builder()
            .brand("MEGANE")
            .model("D")
            .yearOfManufacture(2024)
            .fuelType(FuelType.DIESEL)
            .build();

        IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> service.addVehicle(garageId, newVehicle));

        assertTrue(exception.getMessage().contains("Quota"));
        assertEquals(50, garage.getVehicles().size());
    }

    @Test
    void publishEventAfterAddingVehicle() {
        Vehicle vehicle = Vehicle.builder()
            .brand("HAVAL")
            .model("Model Y")
            .yearOfManufacture(2024)
            .fuelType(FuelType.DIESEL)
            .build();

        service.addVehicle(garageId, vehicle);

        ArgumentCaptor<Vehicle> captor = ArgumentCaptor.forClass(Vehicle.class);
        verify(publisher).publishVehicleCreated(eq(garageId), captor.capture());
        assertEquals("Model Y", captor.getValue().getModel());
    }

    @Test
    void removeVehicle() {
        UUID vehicleId = UUID.randomUUID();
        Vehicle vehicle = Vehicle.builder().id(vehicleId).brand("megane").model("b").build();
        garage.getVehicles().add(vehicle);

        service.removeVehicle(garageId, vehicleId);

        assertFalse(garage.getVehicles().contains(vehicle));
        verify(repo).save(garage);
    }

    @Test
    void listVehicles() {
        Vehicle v1 = Vehicle.builder().id(UUID.randomUUID()).brand("dacia").model("duster").build();
        Vehicle v2 = Vehicle.builder().id(UUID.randomUUID()).brand("megane").model("d").build();
        garage.getVehicles().addAll(List.of(v1, v2));

        List<Vehicle> result = service.listVehicles(garageId);

        assertEquals(2, result.size());
        assertTrue(result.contains(v1));
        assertTrue(result.contains(v2));
    }

    @Test
    void listVehiclesByModel() {
        Garage garage1 = Garage.builder().id(UUID.randomUUID()).name("Garage 1").build();
        Garage garage2 = Garage.builder().id(UUID.randomUUID()).name("Garage 2").build();

        Vehicle clio1 = Vehicle.builder().id(UUID.randomUUID()).brand("Renault").model("Clio").build();
        Vehicle clio2 = Vehicle.builder().id(UUID.randomUUID()).brand("Dacia").model("Clio").build();
        Vehicle megane = Vehicle.builder().id(UUID.randomUUID()).brand("Renault").model("Megane").build();

        garage1.getVehicles().addAll(List.of(clio1, megane));
        garage2.getVehicles().add(clio2);

        when(repo.findAll()).thenReturn(List.of(garage1, garage2));

        List<Vehicle> result = service.listVehiclesByModel("Clio");

        assertEquals(2, result.size());
        assertTrue(result.contains(clio1));
        assertTrue(result.contains(clio2));
        assertFalse(result.contains(megane));
    }

    @Test
    void searchByFuelType() {
        List<Garage> expectedGarages = List.of(garage);
        when(repo.searchByFuelType(FuelType.DIESEL)).thenReturn(expectedGarages);

        List<Garage> result = service.searchByFuelType(FuelType.DIESEL);

        assertEquals(1, result.size());
        assertEquals(garage, result.get(0));
        verify(repo).searchByFuelType(FuelType.DIESEL);
    }

    @Test
    void searchByAccessoryName() {
        List<Garage> expectedGarages = List.of(garage);
        when(repo.searchByAccessoryName("GPS")).thenReturn(expectedGarages);

        List<Garage> result = service.searchByAccessoryName("GPS");

        assertEquals(1, result.size());
        assertEquals(garage, result.get(0));
        verify(repo).searchByAccessoryName("GPS");
    }
}
package com.jetwise_airline.flight_service.service;

import com.jetwise_airline.flight_service.dto.FlightRequestDTO;
import com.jetwise_airline.flight_service.dto.FlightResponseDTO;
import com.jetwise_airline.flight_service.entity.FlightEntity;
import com.jetwise_airline.flight_service.exceptions.FlightAlreadyExists;
import com.jetwise_airline.flight_service.exceptions.FlightNotFoundException;
import com.jetwise_airline.flight_service.repository.FlightRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceImplTest {

    @Mock
    private FlightRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private FlightServiceImpl flightService;

    private static FlightRequestDTO flightRequestDTO;
    private static FlightEntity flightEntity;
    private static FlightResponseDTO flightResponseDTO;

    @BeforeEach
    void setup() {
        flightRequestDTO = new FlightRequestDTO();
        flightRequestDTO.setFlightNumber("A1345");
        flightRequestDTO.setSource("Delhi");
        flightRequestDTO.setDestination("Mumbai");
        flightRequestDTO.setDepartureTime(LocalDateTime.of(2025, 10, 27, 10, 30));
        flightRequestDTO.setArrivalTime(LocalDateTime.of(2025, 10, 27, 12, 45));
        flightRequestDTO.setCapacity(50);
        flightRequestDTO.setPrice(4999.99);

        flightEntity = new FlightEntity();
        flightEntity.setId(1L);
        flightEntity.setFlightNumber("A1345");
        flightEntity.setSource("Delhi");
        flightEntity.setDestination("Mumbai");
        flightEntity.setDepartureTime(LocalDateTime.of(2025, 10, 27, 10, 30));
        flightEntity.setArrivalTime(LocalDateTime.of(2025, 10, 27, 12, 45));
        flightEntity.setCapacity(50);
        flightEntity.setPrice(4999.99);

        flightResponseDTO = new FlightResponseDTO();
        flightResponseDTO.setFlightNumber("A1345");
        flightResponseDTO.setSource("Delhi");
        flightResponseDTO.setDestination("Mumbai");
        flightResponseDTO.setDepartureTime(LocalDateTime.of(2025, 10, 27, 10, 30));
        flightResponseDTO.setArrivalTime(LocalDateTime.of(2025, 10, 27, 12, 45));
        flightResponseDTO.setCapacity(50);
        flightResponseDTO.setPrice(4999.99);
    }

    @Test
    void addFlightTest() {
        Mockito.when(repository.findByFlightNumber("A1345")).thenReturn(Optional.empty());
        Mockito.when(modelMapper.map(flightRequestDTO, FlightEntity.class)).thenReturn(flightEntity);
        Mockito.when(repository.save(Mockito.any(FlightEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        flightService.addFlight(flightRequestDTO);

        Mockito.verify(repository, Mockito.times(1)).findByFlightNumber("A1345");
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(FlightEntity.class));
    }

    @Test
    void addFlightThrowsFlightAlreadyExistsTest() {
        Mockito.when(repository.findByFlightNumber("A1345")).thenReturn(Optional.of(flightEntity));

        FlightAlreadyExists exception = assertThrows(FlightAlreadyExists.class,
                () -> flightService.addFlight(flightRequestDTO));

        Assertions.assertEquals("FLIGHT.ALREADY.EXISTS", exception.getMessage());
        Mockito.verify(repository, Mockito.never()).save(Mockito.any());
    }
    @Test
    void updateFlightTest() {
        FlightEntity existing = new FlightEntity();
        existing.setFlightNumber("A1345");
        existing.setSource("Mumbai");
        existing.setDestination("Delhi");
        existing.setDepartureTime(LocalDateTime.of(2025, 10, 28, 9, 30));
        existing.setArrivalTime(LocalDateTime.of(2025, 10, 28, 12, 45));
        existing.setCapacity(45);
        existing.setPrice(5999.99);

        Mockito.when(repository.findByFlightNumber("A1345")).thenReturn(Optional.of(existing));
        Mockito.when(repository.save(Mockito.any(FlightEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(modelMapper.map(Mockito.any(FlightEntity.class), Mockito.eq(FlightResponseDTO.class)))
                .thenReturn(flightResponseDTO);

        FlightResponseDTO result = flightService.updateFlight(flightRequestDTO);

        Assertions.assertEquals(flightRequestDTO.getDepartureTime(), result.getDepartureTime());
        Assertions.assertEquals(flightRequestDTO.getArrivalTime(), result.getArrivalTime());
        Assertions.assertEquals(flightRequestDTO.getCapacity(), result.getCapacity());
    }
    @Test
    void updateFlightThrowsFlightNotFoundExceptionTest() {
        Mockito.when(repository.findByFlightNumber("A1345")).thenReturn(Optional.empty());

        FlightNotFoundException exception = assertThrows(FlightNotFoundException.class,
                () -> flightService.updateFlight(flightRequestDTO));

        Assertions.assertEquals("FLIGHT.NOT.FOUND", exception.getMessage());
        Mockito.verify(repository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deleteFlightTest() {
        Mockito.when(repository.findByFlightNumber("A1345")).thenReturn(Optional.of(flightEntity));
        flightService.deleteFlight("A1345");
        Mockito.verify(repository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void deleteFlightThrowsFlightNotFoundExceptionTest() {
        Mockito.when(repository.findByFlightNumber("A1345")).thenReturn(Optional.empty());
        FlightNotFoundException exception = assertThrows(FlightNotFoundException.class,
                () -> flightService.deleteFlight("A1345"));
        assertEquals("FLIGHT.NOT.FOUND", exception.getMessage());
        Mockito.verify(repository, Mockito.never()).deleteById(Mockito.any());
    }

    @Test
    void searchFlightsTest() {
        Mockito.when(repository.findBySourceAndDestination("Delhi", "Mumbai"))
                .thenReturn(Optional.of(List.of(flightEntity)));
        Mockito.when(modelMapper.map(Mockito.any(FlightEntity.class), Mockito.eq(FlightResponseDTO.class)))
                .thenReturn(flightResponseDTO);

        List<FlightResponseDTO> result = flightService.searchFlights("Delhi", "Mumbai");

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("A1345", result.get(0).getFlightNumber());
        Mockito.verify(repository, Mockito.times(1)).findBySourceAndDestination("Delhi", "Mumbai");
    }

    @Test
    void searchFlightsThrowsFlightNotFoundExceptionTest() {
        Mockito.when(repository.findBySourceAndDestination("Delhi", "Mumbai"))
                .thenReturn(Optional.empty());
        FlightNotFoundException exception = assertThrows(FlightNotFoundException.class,
                () -> flightService.searchFlights("Delhi", "Mumbai"));
        assertEquals("FLIGHT.NOT.FOUND", exception.getMessage());
    }

    @Test
    void getFlightByIdTest() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(flightEntity));
        Mockito.when(modelMapper.map(flightEntity, FlightResponseDTO.class)).thenReturn(flightResponseDTO);

        FlightResponseDTO result = flightService.getFlightById(1L);

        Assertions.assertEquals("A1345", result.getFlightNumber());
        Assertions.assertEquals("Delhi", result.getSource());
        Mockito.verify(repository, Mockito.times(1)).findById(1L);
    }

    @Test
    void getFlightByIdThrowsFlightNotFoundExceptionTest() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());
        FlightNotFoundException exception = assertThrows(FlightNotFoundException.class,
                () -> flightService.getFlightById(1L));
        assertEquals("FLIGHT.NOT.FOUND", exception.getMessage());
    }
    @Test
    void updateFlightThrowsCapacityReductionExceptionTest() {
        FlightEntity existing = new FlightEntity();
        existing.setFlightNumber("A1345");
        existing.setSource("Delhi");
        existing.setDestination("Mumbai");
        existing.setCapacity(80); // existing has more seats
        existing.setDepartureTime(LocalDateTime.of(2025, 10, 27, 10, 30));
        existing.setArrivalTime(LocalDateTime.of(2025, 10, 27, 12, 45));

        flightRequestDTO.setCapacity(50);
        flightRequestDTO.setArrivalTime(LocalDateTime.of(2025, 10, 27, 12, 45));

        Mockito.when(repository.findByFlightNumber("A1345")).thenReturn(Optional.of(existing));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> flightService.updateFlight(flightRequestDTO));

        assertEquals("Cannot reduce seat capacity after publishing flight.", exception.getMessage());
        Mockito.verify(repository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void updateFlightThrowsArrivalBeforeDepartureException_FirstCase() {
        FlightEntity existing = new FlightEntity();
        existing.setFlightNumber("A1345");
        existing.setSource("Delhi");
        existing.setDestination("Mumbai");
        existing.setCapacity(50);
        existing.setDepartureTime(LocalDateTime.of(2025, 10, 27, 10, 30));
        existing.setArrivalTime(LocalDateTime.of(2025, 10, 27, 12, 45));

        // Invalid request - arrival before departure
        flightRequestDTO.setDepartureTime(LocalDateTime.of(2025, 10, 27, 12, 45));
        flightRequestDTO.setArrivalTime(LocalDateTime.of(2025, 10, 27, 10, 30));

        Mockito.when(repository.findByFlightNumber("A1345")).thenReturn(Optional.of(existing));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> flightService.updateFlight(flightRequestDTO));

        assertEquals("Arrival Time cannot be before departure time", exception.getMessage());
        Mockito.verify(repository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void updateFlightThrowsArrivalBeforeDepartureException_SecondCase() {
        FlightEntity existing = new FlightEntity();
        existing.setFlightNumber("A1345");
        existing.setSource("Delhi");
        existing.setDestination("Mumbai");
        existing.setCapacity(50);
        existing.setDepartureTime(LocalDateTime.of(2025, 10, 27, 10, 30));
        existing.setArrivalTime(LocalDateTime.of(2025, 10, 27, 12, 45));

        // invalid request again
        flightRequestDTO.setDepartureTime(LocalDateTime.of(2025, 10, 27, 11, 30));
        flightRequestDTO.setArrivalTime(LocalDateTime.of(2025, 10, 27, 9, 30));

        Mockito.when(repository.findByFlightNumber("A1345")).thenReturn(Optional.of(existing));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> flightService.updateFlight(flightRequestDTO));

        assertEquals("Arrival Time cannot be before departure time", exception.getMessage());
        Mockito.verify(repository, Mockito.never()).save(Mockito.any());
    }
}

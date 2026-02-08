package com.jetwise_airline.flight_service.service;

import com.jetwise_airline.flight_service.dto.FlightBookingResponse;
import com.jetwise_airline.flight_service.dto.FlightRequestDTO;
import com.jetwise_airline.flight_service.dto.FlightResponseDTO;
import com.jetwise_airline.flight_service.entity.FlightEntity;
import com.jetwise_airline.flight_service.exceptions.FlightAlreadyExists;
import com.jetwise_airline.flight_service.exceptions.FlightNotFoundException;
import com.jetwise_airline.flight_service.repository.FlightRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightServiceImpl implements FlightService {
    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public void addFlight(FlightRequestDTO flightRequest) throws FlightAlreadyExists {
      if(flightRepository.findByFlightNumber(flightRequest.getFlightNumber()).isPresent()){
          throw new FlightAlreadyExists("FLIGHT.ALREADY.EXISTS");
      }else{
         flightRepository.save(modelMapper.map(flightRequest,FlightEntity.class));
      }
    }

    @Override
    public FlightResponseDTO updateFlight(FlightRequestDTO flightRequest) throws FlightNotFoundException {

        Optional<FlightEntity> existingFlight = flightRepository.findByFlightNumber(flightRequest.getFlightNumber());
        if(existingFlight.isEmpty()){
            throw new FlightNotFoundException("FLIGHT.NOT.FOUND");
        }


        //Update route only if not null and Changed
        if(flightRequest.getSource()!= null && !flightRequest.getSource().equals(existingFlight.get().getSource())){
            existingFlight.get().setSource(flightRequest.getSource());
        }
        if(flightRequest.getDestination()!= null && !flightRequest.getDestination().equals(existingFlight.get().getDestination())){
            existingFlight.get().setDestination(flightRequest.getDestination());
        }

        //Update timings only if not null and Changed
        if(flightRequest.getDepartureTime()!= null && !flightRequest.getDepartureTime().equals(existingFlight.get().getDepartureTime())){
            if(flightRequest.getArrivalTime().isBefore(flightRequest.getDepartureTime())){
                throw new RuntimeException("Arrival Time cannot be before departure time");      }

            existingFlight.get().setDepartureTime(flightRequest.getDepartureTime());
        }
        if(flightRequest.getArrivalTime()!= null && !flightRequest.getArrivalTime().equals(existingFlight.get().getArrivalTime())){
            if(flightRequest.getArrivalTime().isBefore(flightRequest.getDepartureTime())){
                throw new RuntimeException("Arrival Time cannot be before departure time");      }
            existingFlight.get().setArrivalTime(flightRequest.getArrivalTime());
        }

        //Seats handling
        if(flightRequest.getCapacity()<existingFlight.get().getCapacity()) {
            throw new RuntimeException("Cannot reduce seat capacity after publishing flight.");
        }else{
            existingFlight.get().setCapacity(flightRequest.getCapacity());
        }
       flightRepository.save(existingFlight.get());

        return modelMapper.map(existingFlight.get(),FlightResponseDTO.class);

    }

    @Override
    public void deleteFlight(String flightNumber) throws FlightNotFoundException {
        FlightEntity flightEntity = flightRepository.findByFlightNumber(flightNumber).
                orElseThrow(() -> new FlightNotFoundException("FLIGHT.NOT.FOUND"));
        if(Optional.of(flightEntity).isPresent()){
            flightRepository.deleteById( flightEntity.getId());
        }
    }

    @Override
    public List<FlightResponseDTO> searchFlights(String source, String destination) throws FlightNotFoundException {
        List<FlightEntity> availableFlights = flightRepository.findBySourceAndDestination(source, destination)
                .orElseThrow(() -> new FlightNotFoundException("FLIGHT.NOT.FOUND"));

        List<FlightResponseDTO> flightResponseDTOList = availableFlights.stream()
                .map(flight->modelMapper.map(flight,FlightResponseDTO.class))
                .toList();
        return flightResponseDTOList;
    }

    @Override
    public FlightResponseDTO getFlightById(Long flightId) throws FlightNotFoundException {
        FlightEntity flightEntity = flightRepository.findById(flightId)
                .orElseThrow(() -> new FlightNotFoundException("FLIGHT.NOT.FOUND"));
        return modelMapper.map(flightEntity, FlightResponseDTO.class);

    }

}

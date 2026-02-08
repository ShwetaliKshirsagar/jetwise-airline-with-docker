package com.jetwise_airline.booking_service.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jetwise_airline.booking_service.client.FlightClient;
import com.jetwise_airline.booking_service.dto.BookingPaymentResponse;
import com.jetwise_airline.booking_service.dto.BookingRequest;
import com.jetwise_airline.booking_service.dto.FlightDTO;
import com.jetwise_airline.booking_service.entity.Booking;
import com.jetwise_airline.booking_service.exception.BookingUnavailbleException;
import com.jetwise_airline.booking_service.exception.SeatsUnvailableException;
import com.jetwise_airline.booking_service.repository.BookingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FlightClient flightClient;

    public BookingServiceImpl() {
    }

    @Override
    public void createBooking(BookingRequest bookingRequest) {
        FlightDTO flight = restTemplate.getForObject("http://localhost:8082/flights/getFlight/" + bookingRequest.getFlightId(), FlightDTO.class);
        if(flight.getCapacity()<bookingRequest.getSelectedSeats()){
            throw new SeatsUnvailableException("NO.SEATS.AVAILABLE");
        }
        else {
            Booking booking = modelMapper.map(bookingRequest, Booking.class);
            booking.setBookingStatus("PENDING");
            booking.setBookingId(null);
            booking.setEmailId("dummy@gmail.com");// this need to extract from token
            bookingRepository.save(booking);

        }
    }
    @Override
    public BookingPaymentResponse getBookingDetails(String bookingId){
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("BOOKING.NOT.FOUND"));
        return modelMapper.map(booking, BookingPaymentResponse.class);
    }
    @Override
    public void updateBookingStatus(String bookingId, String status){
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("BOOKING.NOT.FOUND"));
        booking.setBookingStatus("CONFIRMED");
        bookingRepository.save(booking);
    }

    @Override
    public void generateTicket(String bookingId) throws BookingUnavailbleException, FileNotFoundException, DocumentException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("BOOKING.NOT.FOUND"));
        FlightDTO flightDetails = flightClient.getFlightById(booking.getFlightId());
        String filePath="C:/Users/hp/"+ bookingId + "_new.pdf";
        Document document = new Document(PageSize.A5);
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Title section
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.ORANGE);
        Paragraph title = new Paragraph("JetWise Airline Ticket", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Ticket details
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        table.setWidths(new int[]{1, 2});

        Font labelFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font valueFont = new Font(Font.FontFamily.HELVETICA, 12);

        addRow(table, "Ticket ID:", booking.getBookingId(), labelFont, valueFont);
        addRow(table, "Passenger Email:", booking.getEmailId(), labelFont, valueFont);
        addRow(table, "Flight Number:", String.valueOf(flightDetails.getFlightNumber()), labelFont, valueFont);
        addRow(table, "Source:", String.valueOf(flightDetails.getSource()), labelFont, valueFont);
        addRow(table, "Destination:", String.valueOf(flightDetails.getDestination()), labelFont, valueFont);
        addRow(table, "Flight Departure ime:", String.valueOf(flightDetails.getDepartureTime()), labelFont, valueFont);
        addRow(table, "Flight Arrival Time:", String.valueOf(flightDetails.getArrivalTime()), labelFont, valueFont);
        addRow(table, "Selected Seats:", String.valueOf(booking.getSelectedSeats()), labelFont, valueFont);
        addRow(table, "Status:", booking.getBookingStatus(), labelFont, valueFont);
        addRow(table, "Generated On:", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")), labelFont, valueFont);

        document.add(table);

        // Footer
        Paragraph footer = new Paragraph("Have a safe journey!\nJetWise Airlines", new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY));
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(30);
        document.add(footer);
        document.close();
    }


    private void addRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell cell1 = new PdfPCell(new Phrase(label, labelFont));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setPadding(5f);

        PdfPCell cell2 = new PdfPCell(new Phrase(value, valueFont));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setPadding(5f);

        table.addCell(cell1);
        table.addCell(cell2);
    }

    @Override
    public void updateBookingStatusCancel(List<String> bookings) {
        List<Booking> availableBookings = bookingRepository.findAllById(bookings);

        if(bookings.size()!=availableBookings.size()){

            Set<String> foundIds = availableBookings.stream()
                    .map(Booking::getBookingId)
                    .collect(Collectors.toSet());

            Set<String> missingIds = bookings.stream()
                    .filter(id-> !foundIds.contains(id))
                    .collect(Collectors.toSet());

            throw new BookingUnavailbleException(missingIds);
        }



        bookingRepository.updateAllStatusCancel(bookings);
    }


}

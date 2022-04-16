package uz.pdp.cinema_room_rest_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.cinema_room_rest_service.enums.TicketStatus;
import uz.pdp.cinema_room_rest_service.model.*;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.repository.*;

import javax.transaction.Transactional;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Transactional
@Service
public class SeatService {

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    HallRepository hallRepository;

    @Autowired
    RowRepository rowRepository;

    @Autowired
    PriceCategoryRepository priceCategoryRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    DashboardService dashboardService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MovieSessionRepository sessionRepository;

    @Autowired
    PurchaseWaitingTimeRepository waitingTimeRepository;

    public static Map<UUID, Timer> threadTicket = new HashMap<>();

    public List<Seat> saveSeat(UUID priceCategoryId, UUID rowId, int begin, int end) {
        Optional<PriceCategory> priceCategory = priceCategoryRepository.findById(priceCategoryId);
        Optional<Row> row = rowRepository.findById(rowId);
        if (row.isPresent() && priceCategory.isPresent()) {
            List<Seat> seats = new ArrayList<>();
            for (int i = begin; i <= end; i++) {
                Seat seat = new Seat();
                seat.setNumber(i);
                seat.setRow(row.get());
                seat.setPriceCategory(priceCategory.get());
                seat = seatRepository.save(seat);
                seats.add(seat);
            }
            return seats;
        }
        return null;
    }

    public HttpEntity<ApiResponse> getAvailableSeats(UUID sessionId) {
        ApiResponse response = new ApiResponse();
        try {
            List<Map<String, Object>> availableSeats = seatRepository.getAvailableSeats(sessionId);
            List<Map<String, Object>> rows = dashboardService.stringToJsonArrayList(availableSeats, "rows");
            response.setData(rows);
            response.setSuccess(true);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    public HttpEntity<ApiResponse> addToCard(UUID userId, UUID seatId, UUID sessionId) {
        ApiResponse response = new ApiResponse();

        try {
            if (!seatRepository.existsSeatBySessionIdAndSeatId(sessionId, seatId)) {
                throw new ResourceNotFoundException("bu joy zalda mavjud emas");
            }
            if (ticketRepository.existsBySeatIdAndMovieSessionId(seatId, sessionId)) {
                response.setSuccess(false);
                response.setMessage("the seat is already occupied");
                return ResponseEntity.ok(response);
            }
            Cart cart = cartRepository.findByUserId(userId);
            if (cart == null) {
                cart = new Cart();
                Optional<User> user = userRepository.findById(userId);
                user.ifPresent(cart::setUser);
                cartRepository.save(cart);
            }
            Ticket ticket = new Ticket();
            ticket.setCart(cart);
            Optional<Seat> seat = seatRepository.findById(seatId);
            seat.ifPresent(ticket::setSeat);
            Double price = ticketRepository.getPriceFromSeatIdAndSessionId(seatId, sessionId);
            ticket.setPrice(price);
            Optional<MovieSession> session = sessionRepository.findById(sessionId);
            session.ifPresent(ticket::setMovieSession);
            ticket.setStatus(TicketStatus.NEW);
            ticket.setCreatedAt(LocalDateTime.now());
            ticket.setQrCode("none");
            ticketRepository.save(ticket);
            ticketWaitingForPurchase(ticket.getId());
            response.setSuccess(true);
            response.setMessage("success");
            response.setData(ticket.getId());
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setSuccess(false);
        }
        return ResponseEntity.ok(response);
    }

    private void ticketWaitingForPurchase(UUID ticketId) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis());
                Optional<Ticket> optional = ticketRepository.findById(ticketId);
                if (optional.isPresent()) {
                    Ticket ticket1 = optional.get();
                    if (ticket1.getStatus() == TicketStatus.NEW) {
                        ticketRepository.deleteById(ticketId);
                    }
                }
            }

        };
        Optional<PurchaseWaitingTime> first = waitingTimeRepository.findAll().stream().findFirst();
        System.out.println(first.isPresent());
        PurchaseWaitingTime waitingTime;
        waitingTime = first.orElseGet(() -> new PurchaseWaitingTime(null, Time.valueOf(LocalTime.of(0, 30, 0))));
        Timer timer = new Timer();
        timer.schedule(task, waitingTime.getTime().toLocalTime().toSecondOfDay() * 1000L);
        SeatService.threadTicket.put(ticketId, timer);

    }
}

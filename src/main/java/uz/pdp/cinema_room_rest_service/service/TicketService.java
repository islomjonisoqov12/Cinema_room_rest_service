package uz.pdp.cinema_room_rest_service.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import sun.misc.BASE64Encoder;
import uz.pdp.cinema_room_rest_service.dto.StripeResponseDto;
import uz.pdp.cinema_room_rest_service.enums.TicketStatus;
import uz.pdp.cinema_room_rest_service.model.CashBox;
import uz.pdp.cinema_room_rest_service.model.PayType;
import uz.pdp.cinema_room_rest_service.model.PurchaseHistory;
import uz.pdp.cinema_room_rest_service.model.Ticket;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.projection.CartProjection;
import uz.pdp.cinema_room_rest_service.projection.TicketProjection;
import uz.pdp.cinema_room_rest_service.repository.*;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class TicketService {

    @Value("${stripe_security_key}")
    String stripeApiKey;

    @Value("${base_url}")
    String baseUrl;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    CartRepository cartRepository;

    private Logger logger = LoggerFactory.getLogger(TicketService.class);

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PurchaseHistoryRepository historyRepository;

    @Autowired
    SeatService seatService;

    @Autowired
    PayTypeRepository payTypeRepository;

    @Autowired
    CashBoxRepository cashBoxRepository;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    MovieSessionRepository movieSessionRepository;

    private final String path = "./src/main/resources/qrCodePhotos/";
    private final static String pfdPath = "./src/main/resources/ticket pdf/";

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");


    public ApiResponse purchaseTicket(Session session) {
        ApiResponse response = new ApiResponse();
        UUID userId = UUID.fromString(session.getClientReferenceId());
        CashBox cashBox = cashBoxRepository.findAll().stream().findFirst().get();
        try {
            List<Ticket> tickets = cartRepository.findCartByUserId(userId).getTickets();
            double totalAmount = tickets.stream().mapToDouble(Ticket::getPrice).sum();
            cashBox.setBalance(totalAmount);
            cashBoxRepository.save(cashBox);
            if (tickets.size() > 0) {
                for (Ticket ticket : tickets) {
                    ticket.setQrCode(System.currentTimeMillis() + "" + ticket.getId().toString());
                    BASE64Encoder encoder = new BASE64Encoder();
                    String encode = encoder.encode(ticket.getQrCode().getBytes(StandardCharsets.UTF_8));
                    ticket.setStatus(TicketStatus.PURCHASED);
                    generateTicketSendMessageToEmail(ticket.getId(), encode,userId);
                    ticket.setCart(null);
                    response.setMessage("success");
                    response.setSuccess(true);
                    response.setData(userId);
                }
                Ticket ticket = tickets.get(0);
                emailSenderService.sendMailTimer(
                        ticket.getMovieSession().getMovieAnnouncement().getMovie().getTitle(),
                        userRepository.findById(userId).get().getEmail(),
                        LocalDateTime.of(ticket.getMovieSession().getDate().getDate(), ticket.getMovieSession().getStartTime().getStartTime().toLocalTime()),
                        movieSessionRepository.getAttachmentByTicketId(ticket.getId())
                );
                List<Ticket> savedTickets = ticketRepository.saveAll(tickets);
                PurchaseHistory history = new PurchaseHistory();
                history.setUser(userRepository.findById(userId).get());
                history.setTotalAmount(totalAmount);
                Optional<PayType> payType = payTypeRepository.findByName(session.getPaymentMethodTypes().get(0));
                history.setTickets(new HashSet<>(savedTickets));
                payType.ifPresent(history::setPayType);
                history.setIntentId(session.getPaymentIntent());
                historyRepository.save(history);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setSuccess(false);
        }
        return response;
    }

    private void generateTicketSendMessageToEmail(UUID ticketId, String encode,UUID userId) {
        new Thread(() -> {
            Map<String, Object> ticketInfo = getTicketInfo(ticketId, "http://localhost:8080/api/ticket/check/" + encode);
            generatePdfFile("Ticket1", ticketInfo, "Ticket1.pdf");
            try {
                Timer timer = SeatService.threadTicket.get(ticketId);
                timer.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            emailSenderService.sendToEmailAttachment("Sinema ticket ",userRepository.getEmailByTicketId(ticket.getId()), pfdPath+"Ticket1.pdf");
//            emailSenderService.sendMailWithAttachment(ticket.getCart().getUser().getEmail(),"Ticket Cinema","download this pdf ticket","Ticket1.pdf" );
//            emailSenderService.sendToEmailText("this is subject","hello Islom how are you? ","axranaeva@gmail.com");
            try {
                emailSenderService.sendEmailAttachment("This is subject",
                        "this subject this body",userRepository.findById(userId).get().getEmail() , false,pfdPath + "Ticket1.pdf");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private Map<String, Object> getTicketInfo(UUID id, String qrCodeText) {
        TicketProjection ticket = ticketRepository.getTicketByTicketId(id);
        Map<String, Object> date = new HashMap<>();
        date.put("movieTitle", ticket.getMovieTitle());
        date.put("time", ticket.getStartDate().toLocalTime());
        date.put("date", ticket.getStartDate().toLocalDate());
        date.put("price", ticket.getPrice());
        date.put("rowNumber", ticket.getRowNumber());
        date.put("seatNumber", ticket.getSeatNumber());
        String s = generateImgQRCode(qrCodeText, 50, 50, path + ticket.getId());
        date.put("imgUrl", s);
        return date;
    }

    public static byte[] generateByteQRCode(String text, int with, int height) {
        ByteArrayOutputStream outputStream = null;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, with, height);
            outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "JPG", outputStream, new MatrixToImageConfig(0xFF000000, 0xFFFFFFFF));
        } catch (WriterException | IOException e) {
            System.out.println(e.getMessage());
        }
        return outputStream.toByteArray();
    }

    public static String generateImgQRCode(String text, int with, int height, String path) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, with, height);
            MatrixToImageWriter.writeToPath(bitMatrix, "jpg", FileSystems.getDefault().getPath(path));
        } catch (WriterException | IOException e) {
            System.out.println(e.getMessage());
        }
        return path;
    }

    public HttpEntity<ApiResponse> getUserTickets(UUID userId) {
        ApiResponse response = new ApiResponse();
        try {
            CartProjection userCartById = cartRepository.getUserCartById(userId);
            response.setMessage("success");
            response.setSuccess(true);
            response.setData(userCartById);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    public String generatePdfFile(String templateName, Map<String, Object> data, String pdfFileName) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pfdPath + "Ticket1.pdf"));
            document.open();
            Image img = Image.getInstance(data.get("imgUrl").toString());
            document.add(new Paragraph("Movie Title: " + data.get("movieTitle")));
            document.add(new Paragraph("Date: " + data.get("date")));
            document.add(new Paragraph("Time: " + data.get("time")));
            document.add(new Paragraph("Price: " + data.get("price")));
            document.add(new Paragraph("Row: " + data.get("rowNumber")));
            document.add(new Paragraph("Seat: " + data.get("seatNumber")));
            document.add(img);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pfdPath + pdfFileName;
    }

    public HttpEntity<?> createStripeSession(UUID userId) {
        // SUCCESS and FAILURE URLS
        String successURL = baseUrl + "payment/success";
        String failureURL = baseUrl + "payment/failed";
        Stripe.apiKey = stripeApiKey;
        List<TicketProjection> tickets = cartRepository.getUserCartTickets(userId);
        List<SessionCreateParams.LineItem> sessionItemList = new ArrayList<>();

        for (TicketProjection ticketDto : tickets) {
            sessionItemList.add(createSessionLineItem(ticketDto, tickets.size()));

        }
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl(failureURL)
                .setSuccessUrl(successURL)
                .addAllLineItem(sessionItemList)
                .setClientReferenceId(userId.toString())
                .build();

        Session session = null;
        try {

            session = Session.create(params);

        } catch (StripeException e) {
            e.printStackTrace();
        }
        String url = session.getUrl();

        StripeResponseDto stripeResponse = new StripeResponseDto(session.getId());
        return ResponseEntity.ok(url);
    }

    private SessionCreateParams.LineItem createSessionLineItem(TicketProjection ticketDto, int size) {
        String description = "Ticket price: "+decimalFormat.format(ticketDto.getPrice());
        String description1 = "Stripe commission fee: "+decimalFormat.format(ticketDto.getPrice() * 0.029 + 0.3) ;
        return SessionCreateParams.LineItem.builder()
                .setPriceData(createPriceData(ticketDto, size))
                .setQuantity(1L)
                .setDescription(description+" | "+description1)
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(TicketProjection ticketDto, int size) {
        System.out.println("net : " + ticketDto.getPrice());
        double amount = (ticketDto.getPrice() + (0.3/size)) / (1 - 2.9 / 100);
        System.out.println("amount: " + amount);
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmount((long) (amount) * 100)
                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(ticketDto.getMovieTitle())
                        .build())
                .build();
    }

    public HttpEntity<?> refundTicket(UUID userId, UUID ticketId) {
        ApiResponse response = new ApiResponse();
        Stripe.apiKey = stripeApiKey;
        try {
            if (!ticketRepository.existsById(ticketId)) throw new ResourceNotFoundException("Chipta Mavjud Emas");
            if (ticketRepository.isExpired(ticketId))
                throw new Exception("Muddati O'tgan yoki bu chipta hali sotib olinganlar ro'yxatida mavjud emas");
            Ticket ticket = ticketRepository.findById(ticketId).get();
            long refundAmount = (long) ((100.0 - ticketRepository.ticketRefundFeeInPer(ticketId)) * ticket.getPrice());
            String status = "";
            if (refundAmount > 0) {

                RefundCreateParams params =
                        RefundCreateParams
                                .builder()
                                .setPaymentIntent(ticketRepository.getPaymentIntentByTicketId(ticketId))
                                .setAmount(refundAmount)
                                .build();
                Refund refund = Refund.create(params);
                status = refund.getStatus();
            }
            if (status.equals("succeeded") || refundAmount == 0) {
                double refundAmountWithStripeFee = (refundAmount * 1.0029 + 30) / 100;
                if (refundAmount == 0) refundAmountWithStripeFee = 0;
                ticket.setStatus(TicketStatus.REFUNDED);
                PurchaseHistory history = new PurchaseHistory();
                history.setTickets(new HashSet<>(Collections.singletonList(ticket)));
                history.setTotalAmount(refundAmountWithStripeFee);
                history.setRefund(true);
                history.setUser(userRepository.findById(userId).get());
                CashBox cashBox = cashBoxRepository.findAll().stream().findFirst().get();
                cashBox.setBalance(cashBox.getBalance() - refundAmountWithStripeFee);
                ticketRepository.save(ticket);
                response.setSuccess(true);
                historyRepository.save(history);
                response.setMessage("successfully refunded ");
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setSuccess(false);
        }
        return ResponseEntity.ok(response);
    }

    public HttpEntity<ApiResponse> checkTicket(String qrCode, UUID sessionId) {

        ApiResponse response = new ApiResponse();
        try {
            if (ticketRepository.checkTicket(qrCode, sessionId)) {
                Ticket ticket = ticketRepository.findByQrCodeAndMovieSessionId(qrCode, sessionId).get();
                ticket.setChecked(true);
                ticketRepository.save(ticket);
                response.setMessage("succeeded");
                response.setSuccess(true);
            } else {
                response.setSuccess(false);
                response.setMessage("Check Failed");
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setSuccess(false);
        }
        return ResponseEntity.ok(response);
    }

    public HttpEntity<ApiResponse> deleteTicketById(UUID id) {
        ApiResponse response = new ApiResponse();
        try {
            ticketRepository.deleteById(id);
            response.setMessage("successfully deleted");
            response.setSuccess(true);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    public HttpEntity<ApiResponse> addToCard(UUID userId, UUID seatId, UUID sessionId) {
        return seatService.addToCard(userId, seatId, sessionId);
    }
}

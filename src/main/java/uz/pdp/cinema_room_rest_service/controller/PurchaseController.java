package uz.pdp.cinema_room_rest_service.controller;

import com.stripe.Stripe;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.service.TicketService;

@RequiredArgsConstructor

@RestController
public class PurchaseController {

    @Autowired
    TicketService ticketService;

    @Value("${stripe_security_key}")
    String secret_key;

    String endpointSecret = "whsec_b3427ef0e4da1d78161058009177da8f6e6e1234a4dd956a55cbbc6e165f4c1e";

    @PostMapping("/stripe")
    public String stripeRes(@RequestBody String  payload, @RequestHeader(name = "Stripe-Signature") String signature){
        Stripe.apiKey = secret_key;
        Event event = null;
        try {
            event = Webhook.constructEvent(payload, signature, endpointSecret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().get();
            ApiResponse response = ticketService.purchaseTicket(session);
        }
        return "";
    }




}

package uz.pdp.cinema_room_rest_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.cinema_room_rest_service.model.CashBox;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.repository.CashBoxRepository;
import uz.pdp.cinema_room_rest_service.repository.MovieRepository;
import uz.pdp.cinema_room_rest_service.repository.MovieSessionRepository;
import uz.pdp.cinema_room_rest_service.repository.TicketRepository;
import java.sql.Date;
import javax.transaction.Transactional;
import javax.xml.transform.TransformerException;
import java.time.Instant;
import java.util.*;


@Service
@Transactional
public class DashboardService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    MovieSessionRepository sessionRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    CashBoxRepository cashBoxRepository;

    public HttpEntity<?> getCosts(Date startDate, Date endDate) {
        ApiResponse response = new ApiResponse();
        try {
            boolean checkDate = startDate != null && endDate != null;
            startDate = new Date(System.currentTimeMillis());
            endDate = startDate;
            Map<String, Object> totalIncome = ticketRepository.getTotalIncomeOutcomeNet(startDate, endDate, checkDate);
            Map<String, Object> map = stringToJsonArray(totalIncome, "eachION");
            response.setSuccess(true);
            response.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setSuccess(false);
        }
        return ResponseEntity.ok(response);
    }
//    public ResponseEntity<Map<?, ?>> makeResponseFromData(Object... objects) {
//        Map<Object, Object> data = new HashMap<>();
//        for (int i = 1; i < objects.length; i++) {
//            data.put(objects[i++], objects[i - 1]);
//        }
//        return ResponseEntity.ok(data);
//    }
//
//    public List<Map<?, ?>> makeListFromData(List<Object[]> list) {
//        List<Map<?, ?>> data = new ArrayList<>();
//        for (Object[] objects : list) {
//            data.add(makeResponseFromData(objects).getBody());
//        }
//        return data;
//    }

    public HttpEntity<?> getAmountTicketSoldPerDay(int page, int size,Map<String,Object> map) {
        Object endDate = null;
        Object startDate = null;
        if (map != null) {
            startDate = map.get("startDate");
            endDate = map.get("endDate");
        }
        ApiResponse response = new ApiResponse();
        try {
            if (map == null) {
                response.setData(stringToJsonArray(ticketRepository.getTicketSoldAmountPerDay(page,size),"tickets"));
            } else if (endDate == null)
                response.setData(stringToJsonArray(ticketRepository.getTicketSoldAmountPerDay(Date.valueOf(startDate.toString()) , Date.valueOf(startDate.toString()),page,size),"tickets"));
            else if (startDate == null)
                response.setData(stringToJsonArray(ticketRepository.getTicketSoldAmountPerDay(Date.valueOf(endDate.toString()) , Date.valueOf(endDate.toString()),page,size),"tickets"));
            else
                response.setData(stringToJsonArray(ticketRepository.getTicketSoldAmountPerDay(Date.valueOf(startDate.toString()) , Date.valueOf(endDate.toString()),page,size),"tickets"));
            response.setMessage("success");
            response.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    public HttpEntity<?> getTopSession(int size, boolean expired) {
        ApiResponse response = new ApiResponse();
        try {
            List<Map<String, Object>> topSession = sessionRepository.getTopSession(size,expired);
            response.setData(topSession);
            response.setSuccess(true);
            response.setMessage("success");
            System.out.println(Instant.now());
        }catch (Exception e){
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setSuccess(false);
            return ResponseEntity.internalServerError().body(response);
        }
        return ResponseEntity.ok(response);
    }

    public List<Map<String, Object>> stringToJsonArrayList(List<Map<String, Object>> list, String jsonColumnName){
//        return list.stream().map(stringMap -> stringMap.put(jsonColumnName, new JSONObject("{ \"" + jsonColumnName + "\" : " + stringMap.get(jsonColumnName)).getJSONArray(jsonColumnName))).collect(Collectors.toList());
        List<Map<String, Object>> data = new ArrayList<>();
        for (Map<String, Object> stringObjectMap : list) {
            Map<String, Object> map = stringToJsonArray(stringObjectMap, jsonColumnName);
            if (map.size()>0) data.add(map);
        }
        return data;
//        JSONArray rows = new JSONObject("{ \"rows\" :"+stringObjectMap.get("rows").toString()+"}").getJSONArray("rows");
    }

    public Map<String, Object> stringToJsonArray(Map<String, Object> stringObjectMap, String jsonColumnName){
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        try {
            if (stringObjectMap.get(jsonColumnName) != null) {
                map.put(jsonColumnName,mapper.readValue(stringObjectMap.get(jsonColumnName).toString(), ArrayList.class));
            }
            List<String> collect = new ArrayList<>(stringObjectMap.keySet());
            collect.remove(jsonColumnName);
            for (String s : collect) map.put(s, stringObjectMap.get(s));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return map;
    }

    public HttpEntity<?> getTopMovies(int size) {
        ApiResponse response = new ApiResponse();
        try {
            List<Map<String, Object>> topMovies = movieRepository.getTopMovies(size);
            response.setData(topMovies);
            response.setSuccess(true);
            response.setMessage("success");
        }catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setSuccess(false);
            return ResponseEntity.internalServerError().body(response);
        }
        return ResponseEntity.ok(response);
    }

    public HttpEntity<?> transaction(Map<String, Object> cashBox) {
        ApiResponse response = new ApiResponse();
        try {
            Optional<CashBox> fromOptional = cashBoxRepository.findById(UUID.fromString(cashBox.get("fromId").toString()));
            Optional<CashBox> toOptional = cashBoxRepository.findById(UUID.fromString(cashBox.get("toId").toString()));
            Double amount = Double.valueOf(cashBox.get("amount").toString());
            if (fromOptional.isPresent() && toOptional.isPresent()) {
                CashBox cashBox1 = fromOptional.get();
                CashBox cashBox2 = toOptional.get();
                if (cashBox1.getBalance()>=amount) {
                    if (cashBox2.addBalance(amount)) {
                        String s = cashBox1.minusBalance(amount);
                        response.setSuccess(true);
                        response.setMessage(s);
                    }else {
                        throw new TransformerException("transaction minimum amount was greater then 0");
                    }
                }else {
                    throw  new TransformerException("amount was greater then cash box balance");
                }
            }else {
                throw new ResourceNotFoundException("cash box not found");
            }
        }catch (Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return ResponseEntity.ok(response);

    }
}

package uz.pdp.cinema_room_rest_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Time;
import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Data
@Entity(name = "session_times")
public class SessionTimes {
    @Id
    @GeneratedValue
    UUID id;

    @Column(columnDefinition = "time")
    Time start_time;

    @Column(name = "session_additional_fee_in_percentage")
    double sessionAdditionalFeeInPercentage;
}

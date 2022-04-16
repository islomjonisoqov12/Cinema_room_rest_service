package uz.pdp.cinema_room_rest_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "cash_box")
public class CashBox extends AbsEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    Double balance = 0.0;

    public boolean addBalance(Double amount) {
        if (amount>0) {
            balance +=amount;
            return true;
        }
        return false;
    }

    public String  minusBalance(Double amount) {
        if(amount<balance && amount > 0){
            balance-=amount;
            return "success";
        }else if(amount<=0) return "the amount must be greater then 0";
        else return "cash box balance must be greater then the amount";
    }
}

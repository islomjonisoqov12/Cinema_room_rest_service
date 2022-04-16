package uz.pdp.cinema_room_rest_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "roles")
public class Role extends AbsEntity{

    @Id
    @GeneratedValue
    UUID id;

    @Enumerated(value = EnumType.STRING)
    RoleEnum roleEnum;

    String name;


}

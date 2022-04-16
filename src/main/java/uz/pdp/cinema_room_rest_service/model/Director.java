package uz.pdp.cinema_room_rest_service.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "directors")
public class  Director extends AbsEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @Column(name = "full_name", nullable = false)
    String fullName;


    @Column(columnDefinition = "text")
    String bio;

    @Column(columnDefinition = "date")
    LocalDate dateOfBirth;

    @ManyToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "movies_directors", joinColumns = @JoinColumn(name = "director_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    List<Movie> movies = new ArrayList<>();


    public Director(UUID id, String fullName, String bio) {
        this.id = id;
        this.fullName = fullName;
        this.bio = bio;
    }
}

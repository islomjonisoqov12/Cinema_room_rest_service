package uz.pdp.cinema_room_rest_service.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "directors")
public class Director {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @Column(name = "full_name", nullable = false)
    String fullName;


    @Column(columnDefinition = "text")
    String bio;


    @ManyToMany
    @JoinTable(name = "movies_directors", joinColumns = @JoinColumn(name = "director_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    List<Movie> movies;

    

}

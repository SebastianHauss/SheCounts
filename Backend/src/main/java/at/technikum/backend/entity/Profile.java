package at.technikum.backend.entity;

import at.technikum.backend.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    @NotBlank
    private String country;

    @OneToOne
    @MapsId // "Nutze die ID des Users als meine ID"
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
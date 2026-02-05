package at.technikum.backend.entity;

import at.technikum.backend.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "user")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    @NotBlank
    private String country;

    @Column(name = "birthday")
    private LocalDate birthday;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    @JsonIgnore
    private User user;
}
package com.futureproof.community.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "JUNCTION")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Junction {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Nullable
    private long id;

    private long animalId;

    private long countryId;

    public Junction(long animalId, long countryId) {
        this.animalId = animalId;
        this.countryId = countryId;
    }
}

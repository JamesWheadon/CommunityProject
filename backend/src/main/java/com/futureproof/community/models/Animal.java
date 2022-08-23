package com.futureproof.community.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "ANIMALS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Animal {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Nullable
    private long id;

    private String name;

    @Lob
    @Column(name="IMAGE", length=512)
    private String image;

    private double latitude;

    private double longitude;

    public Animal(String name, String image, double latitude, double longitude) {
        this.name = name;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

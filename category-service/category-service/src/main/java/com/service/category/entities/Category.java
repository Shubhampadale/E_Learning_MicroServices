package com.service.category.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(name = "description")
    private String desc;

    private Date addedDate;

    @Column(name="banner_url")
    private String bannerUrl;
}

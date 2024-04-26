package com.deepakTraders.generalstore.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private int level;

    @ManyToOne
    @JoinColumn(name = "parentCategoryId")
    private Category parentCategory;
}

package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "func")
public class Function {

    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "russian_text")
    private String russianText;
    @Column(name = "english_text")
    private String englishText;

}

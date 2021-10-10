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
@Table(name = "pos")
public class Position {

    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "function_id")
    private Integer functionId;
    @Column(name = "russian_word_position")
    private String russianWordPosition;
    @Column(name = "english_word_position")
    private String englishWordPosition;

}

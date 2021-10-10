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
@Table(name = "dict")
public class Dictionary {

    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "russian_word")
    private String russianWord;
    @Column(name = "english_word")
    private String englishWord;

}

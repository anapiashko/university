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
@Table(name = "func_Wrd")
public class FunctionWord {

    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "word_id")
    private Integer wordId;
    @Column(name = "func_id")
    private Integer funcId;

}

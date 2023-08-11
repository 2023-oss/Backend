package com.project.easysign.domain;

import com.project.easysign.dto.BlockDto;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;
    @Column
    private String category;
    private String question;
    private String answerType;


    public BlockDto toDto() {
        return BlockDto.builder()
                .question(question)
                .answerType(answerType).build();
    }
}

package com.project.easysign.domain.answer;

import com.project.easysign.domain.Block;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "blk_id")
    private Block block;

    private boolean bool_ans;
    private String picture;
    private String content;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BoolResponse {
        private boolean bool_ans;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PicResponse {
        private String picture;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ContentResponse {
        private String content;
    }
}

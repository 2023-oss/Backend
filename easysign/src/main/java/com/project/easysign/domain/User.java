package com.project.easysign.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String pw;
    private String userName;
    private String company;
    private String companyType;
    private String email;
    private String picture;
    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable = false)
    private Role role;

    @JsonIgnore // 양방향 매핑 시 JsonIgnore를 추가해 무한루프 해결
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Template> templates;

    public String getRoleKey(){
        return this.role.getKey();
    }

}

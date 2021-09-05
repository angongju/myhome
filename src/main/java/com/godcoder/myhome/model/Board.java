package com.godcoder.myhome.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Data
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min=2, max=30, message = "제목은 2자 이상 20자 이하이어야 합니다.")
    private String title;

    @Size(min=2, max=100, message = "내용은 2자 이상 100자 이하이어야 합니다. ")
    private String content;

    @ManyToOne  //게시글 입장에서  OneToMany:사용자 입장
    @JoinColumn(name = "user_id")
    //@ManyToOne Many쪽 작성하는 쪽에서 소유하는 정보를 적어준다.
    //@JoinColumn(name = "user_id", referencedColumnName = "id") 로 쓰지만
    // user테이블의 id가 기본키로 되어 있어(17줄에 명시되어 있음) 생략 가능.
    @JsonIgnore
    private User user;
}

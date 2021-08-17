package com.godcoder.myhome.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  String username;
    private String password;
    private Boolean enabled;

    @ManyToMany
    @JoinTable(
            name = "user_role",     //조인할 테이블이름(여기서는 user테이블과 role테이블을 연결하는 user_role 테이블
            joinColumns = @JoinColumn(name = "user_id"),    //조인할 테이블 user테이블의 컬럼
            inverseJoinColumns = @JoinColumn(name = "role_id"))     //조인될 상대 테이블 role 테이블의 컬럼

    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Board> boards = new ArrayList<>();


}

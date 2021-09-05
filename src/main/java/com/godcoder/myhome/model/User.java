package com.godcoder.myhome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore //권한정보 쓰지 않음.
    @ManyToMany //유저와 권한 관계.   연결고리인 유저-권한 테이블까지 조인.
    @JoinTable(
            name = "user_role",     //조인할 테이블이름(여기서는 user테이블과 role테이블을 연결하는 user_role 테이블
            joinColumns = @JoinColumn(name = "user_id"),    //조인할 테이블 user테이블의 컬럼
            inverseJoinColumns = @JoinColumn(name = "role_id"))     //조인될 상대 테이블 role 테이블의 컬럼
    private List<Role> roles = new ArrayList<>();
    //리스트를 이용해 권한과 매핑
    //유저레파지토리를 이용해 조회를 하게되면 유저에 해당하는 권한이 알아서 조회되서 롤스에 안기게 됨.

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) //10장

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) //11장 JPA로 조회방법(FetchType) 성능 관련 전략
    @JsonIgnore
    // board 정보 쓰지 않음.  LAZY일 때만 가능. EAGER일 때는 어노테이션이 적용 안됨.
    private List<Board> boards = new ArrayList<>();

    //joincolumns를 사용하지 않고 mappedBy를 이용 board클래스의 변수명을 씀.
    // 양방향매핑처리 Many쪽에서는 joincolumns를 쓰고 one쪽에서는 mappedBy를 써서 많이 사용..
    //cascade 를 설정해서 user테이블이 persist나 merge 될 때 board테이블을 같이 수행한다.
    //orphanRemoval = true 부모가 없는 데이터는 다 지운다.
}

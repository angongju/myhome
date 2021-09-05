package com.godcoder.myhome.repository;

import com.godcoder.myhome.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = { "boards" }) //11장 attributePaths 를 user의  boards 변수명을 복사해서 넣음. 따로 sql 실행하던 것을 join을 통해 1번만 실행.
    List<User> findAll(); //11장 //새롭게 메소드 명을 만들어도 됨.

    User findByUsername(String username);

    @Query("select u from User u where u.username like %?1%") //sql과 다름
    List<User> findByUsernameQuery(String username);
}

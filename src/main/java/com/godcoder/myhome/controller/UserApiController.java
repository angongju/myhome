package com.godcoder.myhome.controller;

import com.godcoder.myhome.model.Board;
import com.godcoder.myhome.model.User;
import com.godcoder.myhome.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j //롬복을 쓰기 때문에 @Slf4j어노테이션을 사용해서 로그라는 변수를 써서 로그 사용가능.디버그..
class UserApiController {

    @Autowired
    private  UserRepository repository;
/*
    @GetMapping("/users")
    List<User> all() { return repository.findAll(); }
*/

    //게시글 검색
    @GetMapping("/users")
    List<User> all() {
        //return repository.findAll();   //11장에서 repository를 변수로 받고 user에서 LAZY로 바꿔서 board가 필요하지 않기 때문에 조회하지 않고 동일한 결과에 sql 1번 실행 확인.
        List<User> users =  repository.findAll();
        //@Slf4j 어노테이션을 사용해서 로그 확인하는 디버그 가능.
        log.debug("getBoards().size() 호출전");
        log.debug("getBoards().size() : {}", users.get(0).getBoards().size());
        log.debug("getBoards().size() 호출후");
        return users;
    }

    @PostMapping("/users")
    User newUser(@RequestBody User newUser) {
        return repository.save(newUser);
    }

    // Single item

    @GetMapping("/users/{id}")
    User one(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping("/users/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable Long id) {
        return repository.findById(id)
                .map(user -> {
                    //새로운 값 세팅은 주석 처리
                    //user.setTitle(newUser.getTitle());
                    //user.setContent(newUser.getContent());
                    //user.setBoards(newUser.getBoards()); //기존에 있던 보드를 새로은 보드로 바꾸고
                    //return repository.save(user); //유저 레포지토리에만 저장됨.. 보드에도 저장하도록 세팅 필요
                    user.getBoards().clear();  //유저클래스에서 orphanremoval=true 일때 기존 데이터 모두 삭제 후 저장
                    user.getBoards().addAll(newUser.getBoards());
                    for(Board board : user.getBoards()){
                        board.setUser(user);   //보드에 조회한 사용자 정보 넣고
                    }
                    return repository.save(user); //보드에 셋팅
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return repository.save(newUser);
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
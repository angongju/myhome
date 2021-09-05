package com.godcoder.myhome.service;

import com.godcoder.myhome.model.Board;
import com.godcoder.myhome.model.User;
import com.godcoder.myhome.repository.BoardRepository;
import com.godcoder.myhome.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    public Board save(String username, Board board){  //username을 바탕으로 UserRepository에서 검색
        User user = userRepository.findByUsername(username); //1개 혹은 널값 반환. 디비설계에서 유니크 제약조건
        board.setUser(user);
        return boardRepository.save(board); //저장된 객체가 호출한 곳으로 다시 리턴 -> BoardController
    }
}

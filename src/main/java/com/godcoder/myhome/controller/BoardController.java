package com.godcoder.myhome.controller;

import com.godcoder.myhome.model.Board;
import com.godcoder.myhome.repository.BoardRepository;
import com.godcoder.myhome.service.BoardService;
import com.godcoder.myhome.Validator.BoardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService; //4번 BoardService 연결

    @Autowired
    private BoardValidator boardValidator;

    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 2) Pageable pageable,
                       @RequestParam(required = false, defaultValue = "") String searchText) {
        //Page<Board> boards = boardRepository.findAll(pageable);
        Page<Board> boards = boardRepository.findByTitleContainingOrContentContaining(searchText, searchText, pageable);
        int startPage = Math.max(0, boards.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boards", boards);
        return "board/list";
    }

    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false) Long id) {
        if(id == null) {
            model.addAttribute("board", new Board());
        }else {
            Board board = boardRepository.findById(id).orElse(null);
            model.addAttribute("board", board);
        }
        return "board/form";
    }

    @PostMapping("/form") //글작성부분
    public String postForm(@Valid Board board, BindingResult bindingResult, Authentication authentication) {
        boardValidator.validate(board, bindingResult);
        if(bindingResult.hasErrors()) {
            return "board/form";
        }

        //Authentication a = SecurityContextHolder.getContext().getAuthentication();  //구글 검색하고 다른 방법 사용 3번
        // controller에서 가져오지 않고 서비스클래스나 스프링에서 관리하는 클래스에서 인증정보 가져올 때는 전역변수를 써서 위의 문구처럼 사용. 2번

        String username = authentication.getName(); //서버에서 인증정보 받아옴.
        //board.setUser(user); //는 보안 문제 발생.. 따라서 서버의 인증방법을 이용. 1번

        //boardRepository.save(board);
        //service 패키지를 만들어서 저장 4번  ->BoardService 로 이동후 처리
        //10장 코딩의신 8:40~ 참고
        //spring boot get login user 구글검색 2번

        boardService.save(username, board); //5번  ==> 사용자 이름과 게시글 함께 저장하게 함.
        //boardRepository.save(username, board);

        return "redirect:/board/list";
    }
}

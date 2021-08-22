package com.godcoder.myhome.controller;

import com.godcoder.myhome.Validator.BoardValidator;
import com.godcoder.myhome.model.Board;
import com.godcoder.myhome.repository.BoardRepository;
import com.godcoder.myhome.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardValidator boardValidator;

    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 2) Pageable pageable,
                       @RequestParam(required = false, defaultValue = "") String searchText) {
        //Page<Board> boards = boardRepository.findAll(pageable);
        Page<Board> boards = boardRepository.findByTitleContainingOrContentContaining(searchText, searchText, pageable);
        int startPage = Math.max(0, boards.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boards.getTotalPages()-1, boards.getPageable().getPageNumber() + 4);

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

    @PostMapping("/form")
    public String postForm(@Validated Board board, BindingResult bindingResult, Authentication authentication) {
        boardValidator.validate(board, bindingResult);
        if(bindingResult.hasErrors()){
            return "board/form";
        }
        //board.setUser(usr) 는 보안 문제 발생.. 따라서 서버의 인증방법을 이용.
        //Authentication a = SecurityContextHolder.getContext().getAuthentication();
        // controller에서 가져오지 않고 서비스클래스나 스프링에서 관리하는 클래스에서 인증정보 가져올 때는 전역변수를 써서 위의 문구처럼 사용.
        //10장 코딩의신 8:40~ 참고
        //spring boot get login user 구글검색

        String username = authentication.getName();
        boardService.save(username, board);
        //boardRepository.save(username, board);
        return "redirect:/board/list";
    }
}

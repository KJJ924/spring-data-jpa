package study.jpa.jpa.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.jpa.jpa.board.Board;
import study.jpa.jpa.board.BoardRepository;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/19
 */


@RestController
@RequiredArgsConstructor
public class ReplicationTestController {

    private final BoardRepository boardRepository;

    //slave database 에 query 가 발생하여야함
    @GetMapping("/replication/readonly")
    @Transactional(readOnly = true)
    public List<Board> readonlyTransaction(){
        return boardRepository.findAll();
    }

    //master database 에 query 가 발생하여야함
    @GetMapping("/replication/nreadonly")
    @Transactional
    public List<Board> nreadonlyTransaction(){
        return boardRepository.findAll();
    }
}

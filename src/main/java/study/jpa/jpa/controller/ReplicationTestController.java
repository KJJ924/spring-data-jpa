package study.jpa.jpa.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.jpa.jpa.board.Board;
import study.jpa.jpa.service.BoardService;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/19
 */


@RestController
@RequiredArgsConstructor
public class ReplicationTestController {

    private final BoardService boardService;

    @GetMapping("/replication/readonly")
    public List<Board> readonlyTransaction() {
        return boardService.updateTitle();
    }

    @GetMapping("/replication/nreadonly")
    @Transactional
    public List<Board> nreadonlyTransaction() {
        return boardService.getBoardList();
    }
}

package study.jpa.jpa.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.jpa.jpa.board.Board;
import study.jpa.jpa.board.BoardRepository;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/19
 */

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public List<Board> getBoardList(){
        return boardRepository.findAll();
    }

    public List<Board> updateTitle() {
        List<Board> boards = boardRepository.findAll();
        for (Board board : boards) {
            board.setTitle("newTitle(readonly)");
        }
        return getBoardList();
    }
}

package study.jpa.jpa.service;

import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
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
    private final DataSource dataSource;

    @Transactional(readOnly = true)
    public List<Board> getBoardList(){
        try {
            System.out.println(dataSource.getConnection().getMetaData().getURL());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return boardRepository.findAll();
    }

    public List<Board> updateTitle() {
        try {
            System.out.println(dataSource.getConnection().getMetaData().getURL());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        List<Board> boards = boardRepository.findAll();
        for (Board board : boards) {
            board.setTitle("newTitle");
        }
        return getBoardList();
    }
}

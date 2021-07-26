package study.jpa.jpa.board.reply;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.jpa.jpa.board.Board;
import study.jpa.jpa.board.BoardRepository;
import study.jpa.jpa.board.reply.dto.RequestReply;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/26
 */


@Service
@RequiredArgsConstructor
@Transactional
public class ReplyService {

    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;


    // getById() , findByID() Example
    public void save(RequestReply requestReply) {
        try {
            Board board = boardRepository.getById(requestReply.getBoardId());

            Reply reply = Reply.builder()
                .content(requestReply.getContent())
                .board(board)
                .build();
            replyRepository.save(reply);
        } catch (DataAccessException e) {
            throw new RuntimeException("없는 Board");
        }
    }
}

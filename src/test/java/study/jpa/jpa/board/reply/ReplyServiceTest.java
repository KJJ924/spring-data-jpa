package study.jpa.jpa.board.reply;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.jpa.jpa.board.BoardRepository;
import study.jpa.jpa.board.StudyBoard;
import study.jpa.jpa.board.reply.dto.RequestReply;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/26
 */

@SpringBootTest
class ReplyServiceTest {

    @Autowired
    ReplyService replyService;

    @Autowired
    BoardRepository boardRepository;


    @Test
    @DisplayName("댓글등록")
    void replySave() {
        //given
        StudyBoard studyBoard = StudyBoard.builder()
            .title("토비의 스프링 스터디원 구해요")
            .studyName("초보를 위한 Spring Study")
            .description("이러한 이유로 모집합니다.")
            .place("서울")
            .recruitmentDeadline(LocalDateTime.now().plusDays(7))
            .recruiter(4)
            .createBy("KJJ")
            .build();
        StudyBoard study = boardRepository.save(studyBoard);
        RequestReply requestReply = new RequestReply(study.getId(), "저요저요!");

        //when  save 호출시 쿼리 발생 갯수 확인
        System.out.println("==================================================================");
        replyService.save(requestReply);

    }
}
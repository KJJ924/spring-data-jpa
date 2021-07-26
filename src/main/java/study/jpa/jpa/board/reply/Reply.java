package study.jpa.jpa.board.reply;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import study.jpa.jpa.board.Board;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/26
 */

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @CreationTimestamp
    private LocalDateTime createAt;

    @Builder
    private Reply(String content, Board board) {
        this.content = content;
        this.board = board;
    }

    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    private Board board;
}

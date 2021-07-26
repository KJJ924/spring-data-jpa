package study.jpa.jpa.board.reply.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/26
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestReply {

    private Long boardId;
    private String content;
}

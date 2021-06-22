package study.jpa.jpa.board;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/22
 */
@Entity
@Setter @Getter
public class StudyBoard extends Board{

    @Column(nullable = false)
    private String studyName;

    private String place;

    private int recruiter;

    private LocalDateTime recruitmentDeadline;
}

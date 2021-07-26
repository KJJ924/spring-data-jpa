package study.jpa.jpa.board;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/22
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
public class StudyBoard extends Board {

    @Column(nullable = false)
    private String studyName;

    private String place;

    private int recruiter;

    private LocalDateTime recruitmentDeadline;

    @Builder
    private StudyBoard(String studyName, String place, int recruiter,
        LocalDateTime recruitmentDeadline, String title, String description, String createBy) {
        super(title, description, createBy);
        this.studyName = studyName;
        this.place = place;
        this.recruiter = recruiter;
        this.recruitmentDeadline = recruitmentDeadline;
    }

}

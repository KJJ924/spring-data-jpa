package study.jpa.jpa.board;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/22
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Getter
@Setter
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;

    private String title;

    @Lob
    private String description;

    private String createBy;

    public Board(String title, String description, String createBy) {
        this.title = title;
        this.description = description;
        this.createBy = createBy;
    }

    @CreationTimestamp
    private LocalDate createAt;

}

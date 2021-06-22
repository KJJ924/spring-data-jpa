package study.jpa.jpa.board;

import javax.persistence.Entity;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/22
 */

@Entity
public class BookReviewBoard extends Board {

    private String bookName;
    private String author;
    private int price;
}

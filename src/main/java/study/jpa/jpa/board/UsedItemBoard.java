package study.jpa.jpa.board;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/22
 */
@Entity
public class UsedItemBoard extends Board{

    @Column(nullable = false)
    private String item;

    @Column(nullable = false)
    private int itemPrice;

    @Column(nullable = false)
    private String place;
}

package study.jpa.jpa.orderitem;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import study.jpa.jpa.item.Item;
import study.jpa.jpa.order.Order;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/17
 */

@Entity
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToMany
    private List<Item> items = new ArrayList<>();

    private int oderPrice;

    private int count;
}

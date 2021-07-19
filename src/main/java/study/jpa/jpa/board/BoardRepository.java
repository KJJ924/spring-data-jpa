package study.jpa.jpa.board;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/19
 */
public interface BoardRepository extends JpaRepository<Board,Long> {

}

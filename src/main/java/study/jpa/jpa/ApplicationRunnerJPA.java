package study.jpa.jpa;

import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import study.jpa.jpa.board.StudyBoard;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/14
 */

@Component
@RequiredArgsConstructor
public class ApplicationRunnerJPA implements ApplicationRunner {

    private final EntityManagerFactory entityManagerFactory;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // EntityManger 사용하고 반드시 회수 해야한다.
        // 궁금점 (AutoCloseable 을 왜 지원하지 않을까 ?)
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

            // JPA 의 모든 데이터 변경은 트랜잭션 안에서 실행된다
            transaction.begin();
        try {

            StudyBoard studyBoard = new StudyBoard();
            studyBoard.setStudyName("스프링 기초");
            studyBoard.setPlace("서울");
            studyBoard.setRecruitmentDeadline(LocalDateTime.now().plusDays(2));
            studyBoard.setTitle("스프링 스터디 모집합니다");
            studyBoard.setCreateBy("KJJ");
            studyBoard.setDescription("이러이러한 목적으로 모집하고자합니다.");
            studyBoard.setRecruiter(7);
            em.persist(studyBoard);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            em.close();
        }
    }
}

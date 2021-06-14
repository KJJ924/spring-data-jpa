package study.jpa.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import study.jpa.jpa.member.entity.Member;

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
            Member member = new Member();
            member.setName("kjj");

            em.persist(member);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            em.close();
        }
    }
}

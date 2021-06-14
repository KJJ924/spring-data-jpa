# JPA TIL



- EntityManagerFactory 는 Apllication 에서 하나만 생성하여 사용해야한다.
- EntityManager 는 쓰레드간에 공유를 하지 않고 사용 후 바로 정리(`.close()` ) 를 해야한다.
- JPA 의 모든 데이터 변경은 트랜잭션 안에서 실행된다

## 궁금점

EntityManager 는 쓰레드간에 공유를 하지 않고 사용 후 바로 정리해야 한다고 한다.

```java
public void run(ApplicationArguments args) throws Exception {
    EntityManager em = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = em.getTransaction();
  
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
```

따라서 위와 같은 코드 try - catch - finally 문이 어쩔수 없이 강제되는데

try-with-resource 문을 사용한다면 더 깔끔하지 않을까? 라는 의문이 들었지만

현재 EntityManager 인터페이스는 AutoCloseable 인터페이스를 상속하고 있지 않고있다.

자바 7 이 2011 년도에 나왔는데 아직까지 추가되지 않았다.

혹시 뭔가 다른 이유가 있어서 추가하고 있지 않는걸까?


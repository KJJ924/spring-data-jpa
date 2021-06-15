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

이펙티브 자바의 아이템중

[try-finally 보다는 try-with-resources를 사용하라](https://github.com/KJJ924/effective-java/blob/master/src/main/java/com/jaejoon/demo/item9/item9.md)



이라는 해당 내용이 존재한다.





따라서 try-with-resource 문을 사용한다면 더 깔끔하지 않을까?라는 의문이 들어서





![img](https://blog.kakaocdn.net/dn/mlUX1/btq7iRJMHJv/gNGudu7MvMeodPdbJteh01/img.png)



사용해보려고 했지만? 현재 EntityManager는 AutoCloseable 구현하고 있지 않다.



자바 7 이 2011 년도에 나왔는데 아직까지 추가되지 않았다.



혹시 뭔가 다른 이유가 있어서 추가하고 있지 않는 걸까?





++ 추가



https://github.com/eclipse-ee4j/jpa-api/issues/77

해당 이슈는 2014년에 이미 등록된 이슈이다.





https://github.com/eclipse-ee4j/jpa-api/pull/312

또한 이번 2021년 4월 20일에 추가되어 merge 된 것으로 확인된다.





![img](https://blog.kakaocdn.net/dn/dl7l9B/btq7jx5mOvP/ELWta5lKdxYCf0wg4g2xZ1/img.png)


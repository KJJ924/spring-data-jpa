# [Spring-boot] 제목은 나주에~!.



## 서론

데이터베이스를 이용한다면 대부분 **쓰기** 보다 **읽기** 의 행위가 더 많습니다.

![image-20210720142902033](https://tva1.sinaimg.cn/large/008i3skNgy1gsncq2vjw2j30l7088aas.jpg)

DB의 부하를 줄이기 위해 다음과 같이 Master - Slave  구조를 많이 사용하는데요.

그럼 이러한 구조를 가지고있을때 애플리케이션 단에서는 Transection 의 속성이 `readOnly = true` 인 경우
<u>Slave</u> 데이터베이스에 Select query 가 발생하게 해야합니다.

따라서 이번 본문에서는 

1. `@Transactional(readOnly = true)` 인 경우는 Slave DB 접근
2. `@Transactional(readOnly = false)` 인 경우에는 Master DB 접근

위의 조건을 만족하기위한 방법을 Spring-boot 기준으로 소개하고자 합니다.



## 사전 환경설정

실습 환경은 다른것은 필요없고 저는 MySQL 데이터베이스를 이용하였고

Master - Slave 구조가 준비되었다고 가정합니다.

![image-20210720144835998](https://tva1.sinaimg.cn/large/008i3skNgy1gsnd9d2eekj30c405o0ss.jpg)

저는 Docker 를 이용하였습니다.

-  Master DB : 3306 PORT 
-  Slave  DB - 3307 PORT 

 저와 같은 환경을 설정하고싶으신분 [Github](https://github.com/KJJ924/spring-data-jpa/tree/master/docker) 를 참고해주세요.



## 본문

### application.yml 설정

먼저 DB 에 접근하기위해 `application.yml` 에 접근정보를  정의해보겠습니다.

```yaml
spring:
  datasource:
    master:
      hikari: 
        username: root
        password: password
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://localhost:3306/examreplication
    slave:
      hikari:
        username: root
        password: password
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://localhost:3307/examreplication
```

현재 Master, Slave DB 를  2 개를 사용해야하니 Datasource 를 직접 생성해야합니다.

따라서 해당 접근정보들을 이용하여 `DataSource`을 만들어야 하니 정확히 입력해주세요 !



### DataSource Bean 등록하기

일단 `readOnly`속성 별로 분기를 하기전에 앞서 설정한 정보로 DataSource 를 Bean 으로 등록하는 과정입니다.

```java
@Configuration
public class DataSourceConfiguration {

    public static final String MASTER_DATASOURCE = "masterDataSource";
    public static final String SLAVE_DATASOURCE = "slaveDataSource";

    @Bean(MASTER_DATASOURCE) 
    @ConfigurationProperties(prefix = "spring.datasource.master.hikari") // (1)
    public DataSource masterDataSource() {
        return DataSourceBuilder.create() 
            .type(HikariDataSource.class) 
            .build();
    }

    @Bean(SLAVE_DATASOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.slave.hikari")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create()
            .type(HikariDataSource.class)
            .build();
    }
}
```

1. `spring.datasource.master.hikari` 에 해당하는 property 를  DataSource 를 생성하는데 이용합니다.

여기서 1번 과정이 잘 이해가 안되신다면  [해당 자료](https://www.baeldung.com/configuration-properties-in-spring-boot#bean)를 참고해주세요.



### AbstractRoutingDataSource 구현

`AbstractRoutingDataSource.class` 는 조회 key 기반으로 등록된 Datasource 중 하나를 호출을 하게해줍니다.

말이 거창하긴한데 소스코드를 보면 매우간단합니다.

```java
public class RoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() { // (1)
        return (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) ? "slave" : "master"; //(2)
    }
}
```

1. `determineCurrentLookupKey()` 메서드는 현재 조회 키를 반환받기위해 구현해야하는 추상 메서드입니다.
2.  따라서 저희는 readOnly 속성을 구별하여 key를 반환하게 합니다.



### AbstractRoutingDataSource Bean 등록하기

```java
@Bean
@Primary
@DependsOn({MASTER_DATASOURCE, SLAVE_DATASOURCE})
public DataSource routingDataSource(
    @Qualifier(MASTER_DATASOURCE) DataSource masterDataSource,
    @Qualifier(SLAVE_DATASOURCE) DataSource slaveDataSource) {
    
    RoutingDataSource routingDataSource = new RoutingDataSource();
    
    Map<Object, Object> datasourceMap = new HashMap<>() {
        {
            put("master", masterDataSource);
            put("slave", slaveDataSource);
        }
    };

    routingDataSource.setTargetDataSources(datasourceMap);
    routingDataSource.setDefaultTargetDataSource(masterDataSource);
    
    return routingDataSource;
}
```

routingDataSource  Bean 으로 생성하여 @Primary  를 선언하여 DataSource 타입에  바인딩되는 객체로 정의합니다.



## 결과확인

```java
@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final DataSource dataSource;

    @Transactional(readOnly = true)
    public List<Board> getBoardList(){
        try {
            System.out.println(dataSource.getConnection().getMetaData().getURL());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return boardRepository.findAll();
    }

    public List<Board> updateTitle() {
        try {
            System.out.println(dataSource.getConnection().getMetaData().getURL());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        List<Board> boards = boardRepository.findAll();
        for (Board board : boards) {
            board.setTitle("newTitle");
        }
        return getBoardList();
    }
}
```



<img src="https://tva1.sinaimg.cn/large/008i3skNgy1gsntlmstetj30cx01i3yf.jpg" alt="image-20210721001403969" style="zoom:150%;" />

`getBoardList()` 는 `readOnly = ture ` 임으로 ->. jdbc:mysql://localhost:3307 으로 출력되어야합니다.





![image-20210721001609645](https://tva1.sinaimg.cn/large/008i3skNgy1gsntnt0an9j30ko07habh.jpg)

`updateTitle()` 는 `readOnly =false` 임으로 - >. jdbc:mysql://localhost:3306 으로 출력되어야합니다.

-> 여기서 중요한 점은 method 의 시작 트랜잭션이 읽기전용이 아님으로 내부에서 `getBoardList()` 을  호출하였을때

Slave DB 에 쿼리가 발생하는것이 아닌 Master DB 로 query 가 발생합니다.



## 궁금한점🤔??!!

해당 주제를 공부하면서 많은 예제와 블로그를 찾아본 결과

```java
@Bean
@DependsOn("routingDataSource")
public LazyConnectionDataSourceProxy dataSource(DataSource routingDataSource){
    return new LazyConnectionDataSourceProxy(routingDataSource);
}
```

해당 예제처럼 AbstractRoutingDataSource 를  LazyConnectionDataSourceProxy 로 한번 감싸는 예제들이 많았다.



이렇게 감싸는 이유로는 다음과 같이 설명하고있다.([link](http://kwon37xi.egloos.com/m/5364167))

> **TransactionManager 선별 -> DataSource에서 Connection 획득 -> Transaction 동기화(Synchronization)**
>
> 여기서 보면 트랜잭션 동기화를 마친 뒤에 [ReplicationRoutingDataSource.java](https://github.com/kwon37xi/replication-datasource/blob/master/src/test/java/kr/pe/kwonnam/replicationdatasource/routingdatasource/ReplicationRoutingDataSource.java)에서 커넥션을 획득해야만 이게 올바로 동작하는데 그 순서가 뒤바뀌어 있기 때문이다.
>
> 
>
> 따라서  LazyConnectionDataSourceProxy 한번 감싸서 다음과 같이 작동하게한다.
>
> **TransactionManager 선별 -> LazyConnectionDataSourceProxy에서 Connection Proxy 객체 획득 -> Transaction 동기화(Synchronization)** 
>
> **-> 실제 쿼리 호출시에 `ReplicationRoutingDataSource.getConnection()/determineCurrentLookupKey()` 호출**



그래서 정말 그런가 싶어서 LazyConnectionDataSourceProxy 감싸지않고 그냥 AbstractRoutingDataSource 을 빈으로 등록사용 했는데

`readOnly = ture/false ` 속성에 따라 Master / Slave DB 에 원하는대로  잘 쿼리가 분배가 된다.



따라서 제가 이해하지 못하여 이렇게나마 소개하고자합니다.

추 후  LazyConnectionDataSourceProxy 에 대해 파악하여 본문에 추가하도록 하겠습니다.





긴글 읽어주셔서 감사합니다.

부족한 내용이나 틀린내용이 있다면 댓글로 남겨주세요! 🙏



## 참고자료

https://www.oracle.com/technetwork/community/developer-day/mysql-replication-scalability-403030.pdf

https://cheese10yun.github.io/spring-transaction/

http://kwon37xi.egloos.com/m/5364167


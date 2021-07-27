# [JPA] findById() , getById() 에 대한 생각



## 서론

이번 글 에서는 `findById()`, `getById()`  메서드의 차이점과 제가 생각하는 느낀점에 대해 말해보고자 합니다.

따라서 개인적인 생각이 어느정도 담긴 글이니 어느정도 비판적인 시각으로 봐주시면 감사하겠습니다.



## 사전 설명

![image-20210727152331749](https://tva1.sinaimg.cn/large/008i3skNgy1gsvhltlcocj30oa0avq3m.jpg)

다음과 같은 Entity 연관관계를 가지고 있을때 저희는 일반적으로 게시판에 해당하는 댓글을 추가하기 위해서 
클라이언트 요청으로부터 `게시판의 Id, 댓글내용, 작성자` 등 필요한 정보를 받게됩니다.  

그러면 저희는 Serivce Layer 에서 해당하는 게시판이 있는지 확인 후 Reply 객체를 생성해서 연관관계를 맺은 후 저장하게됩니다.



간단히 이러한 로직이 만들어지게 됩니다.

```java
public void save(RequestReply requestReply) {
   
    // 해당하는 게시판이 존재하는가 ?
    Board board = boardRepository.findById(requestReply.getBoardId())
        .orElseThrow(EntityNotFoundException::new);

    // 댓글 생성
    Reply reply = Reply.builder()
        .content(requestReply.getContent())
        .board(board)
        .build();

    replyRepository.save(reply);
}
```



해당 로직 테스트코드는 다음과 같습니다.

```java
@Test
@DisplayName("댓글등록")
void replySave() {
    //given
    StudyBoard studyBoard = StudyBoard.builder()
        .title("토비의 스프링 스터디원 구해요")
        .studyName("초보를 위한 Spring Study")
        .description("이러한 이유로 모집합니다.")
        .place("서울")
        .recruitmentDeadline(LocalDateTime.now().plusDays(7))
        .recruiter(4)
        .createBy("KJJ")
        .build();
    StudyBoard study = boardRepository.save(studyBoard);


    RequestReply requestReply = new RequestReply(study.getId(), "저요저요!");
    System.out.println("==================================================================");
    //when  save 호출시 쿼리 발생 갯수 확인
    replyService.save(requestReply);
}
```



## findById()

앞서 살펴본 예제는 findById() 를 통해  DB에 쿼리가 발생하여 객체를 찾게 됩니다. 

![image-20210727154303468](https://tva1.sinaimg.cn/large/008i3skNgy1gsvi6w7thqj30ko016aa1.jpg)

따라 다음과 같이 총 2번의 쿼리가 발생하게 됩니다.

여기서 드는 생각은 왜? 

- Board 객체를 찾아야하지? 
- Id 값을 알고있으니 Insert Query 하나만 발생해도 충분한거아닌가?

이러한 생각이 들 수 있습니다.



하지만 ORM 입장에서는 해당 ID 값 만으로는 어떤 타입의 객체인지 알 수 없습니다.

따라서 앞서 살펴본것처럼 한번 찾고 Reply 객체를 생성하여 Board 를 설정하는 과정이 발생하게됩니다.



이처럼 2번의 Query 가 발생하는것이 무조건 나쁜 과정일까요?

해당 질문은 아래에서 좀 더 다루겠습니다.



## getById()

`getById()` 메소드가 생소하신분들도 있을것 같은데요  Spring Data JPA 2.5.3 이후 부터 getOne() 메서드가 Deprecated 되고

추가된 메서드입니다 기존 getOne() 메서드와 하는 일은 동일합니다.



그렇다면 앞선 예제에서 `findById()` 로 Board 를 찾는 대신 `getById()` 를 통해 Board 를 찾는다면 어떻게될까요?

![image-20210727160242428](https://tva1.sinaimg.cn/large/008i3skNgy1gsviqiq3prj30j701c3yd.jpg)

다음과 같이 하나의 Query 만 발생하게 됩니다. 

따라서 앞서 findById() 메서드를 사용했을때 가진 생각을 해결할 수 있는데요!



우선  `getById()` 가  어떻게 작동하는지부터 알아보죠!

![image-20210727161126928](https://tva1.sinaimg.cn/large/008i3skNgy1gsvizmhllej309t00pweb.jpg)

`getById()` 는 지연로딩으로 작동하게 되는데요!  ID 값을 제외한 나머지 필드에 접근 했을때 Query가 발생하게 됩니다.

```java
public void save(RequestReply requestReply) {
    Board board = boardRepository.getById(requestReply.getBoardId());

    System.out.println("ID 값 :"+ board.getId()); // 이때는 Query 가 발생하지않음
    System.out.println("=============================================");
    System.out.println("Title 값: "+ board.getTitle()); //이때는 정보가 필요하기때문에 Query 가 발생함

    // 댓글 생성
    Reply reply = Reply.builder()
        .content(requestReply.getContent())
        .board(board)
        .build();

    replyRepository.save(reply);
}
```

예제를 본다면 더욱 이해하기 쉬울것 같습니다

![image-20210727161721678](https://tva1.sinaimg.cn/large/008i3skNgy1gsvj5sgjqij30jm036jrk.jpg)

이처럼  **어떠한 객체의 ID 값이 DB에 반드시 존재하고 ID를 제외한  다른 필드에 접근하지 않을때 사용하게되면 좋을 것 같습니다.**





## 나의 생각💡

`findById()`, `getById()` 의 차이점은 Entity 를 즉시로딩 , 지연로딩 가져오는가? 의 차이점이 있습니다.

여기서 또 메서드 이름으로 유추 해볼수 있는 주제가 있는데요.

왜 getOne() 메서드가 Deprecated 되고 getById() 로 이름만 변경되었을까요 ?



여기서 알 수 있는것은 메서드가 조금더 명확하게 변경되었다는 것입니다.

`get` 은 무엇을 받다, 얻다 라는 의미가있습니다. <u>즉 어떠한 객체를 무조건 받는다는 가정이 있습니다.</u>  (null 을 return 하면 안된다.)

`find` 은  찾다 라는 의미를 가지고있습니다. <u>즉 객체를 찾을수 있고 없을 수 도 있다는 가정이 있습니다.</u> (Optional 로 return 하여 사용자에게 선택권을 주자)

이렇게 명확한 구분은 저희는 코딩을할때 메서드 이용할때 기준을 가지고 사용할 수 있게됩니다.



따라서 `이처럼 2번의 Query 가 발생하는것이 무조건 나쁜 과정일까요?` 에 대한 질문은 메서드의 의미를 생각해보면 답이 나올것 같습니다.

비지니스상 클라이언트로부터 들어온 ID 값이(게시글이) 어떠한 경우에도 항상 존재하는가 ->  YSE or NO   

Yes 인 경우에는 getById() 로 메서드로 사용해도 문제가 발생하지 않습니다. (만약 없으면 에러가 발생합니다.)

NO 인 경우에는 findById() 를 사용해서 존재 여부를 확인해야 하겠죠?



## 결론

프레임워크가 제공하는 메서드명을 읽다보면 의도를 어느정도 파악할 수 있습니다.

이러한 확실한 메서드명은 개발자로 하여금 어떤 문제를 발생할 수 있는지 내부코드를 보지않아도 어느정도 유추할수 있는데요

이러한코드가 정말로 좋은코드이지 않을까요 ?












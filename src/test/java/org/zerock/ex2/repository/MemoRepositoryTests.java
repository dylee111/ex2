package org.zerock.ex2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.zerock.ex2.entity.Memo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {

  @Autowired
  MemoRepository memoRepository;

  // MemoRepository 인터페이스가 실제 객체가 어떤 것인지 확인.
  @Test
  public void testClass() {
    System.out.println("testClass::"+memoRepository.getClass().getName());
  }

  /*@Test
  public void testInsert(){
      IntStream.rangeClosed(1,100).forEach(i -> {
        Memo memo = Memo.builder().memoText("Sample..."+i).build();
        memoRepository.save(memo);
      // save() : 해당 객체가 존재하면 UPDATE, 없다면 INSERT 실행.
    });

    }*/ // testInsert()
    @Test
    public void testSelect() {
       // 데이터 베이스의 column 이름
       Long mno = 100L;
       // Optional<> :
       Optional<Memo> result = memoRepository.findById(mno);
       System.out.println("============================");

        if(result.isPresent()) {
            Memo memo = result.get();
            System.out.println(memo);
        }
    } // testSelect()

    @Transactional
    @Test
    public void testSelect2() {
        // 데이터 베이스의 column 이름
        Long mno = 100L;

        Memo memo = memoRepository.getOne(mno);

        System.out.println("============================");
        System.out.println(memo);
    } // testSelect2()

    @Test
    public void testUpdate() {
        Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();
        System.out.println(memoRepository.save(memo));
    } // testUpdate()

    @Test
    public void testDelete() {
        Long mno = 102L;
        memoRepository.deleteById(mno);
        testSelect2();
    } // testDelete()

    @Test
    public void testInsert() {
        Long mno = 100L;

        Memo memo = Memo.builder().mno(100L).memoText("Sample..." + mno).build();
        memoRepository.save(memo);
    }

    @Test
    public void testPageDefault() {
        // Pageable은 인터페이스, PageRequest.of()를 통해 객체를 생성
        // of(페이지, 페이지 크기) : 원하는 페이지의 데이터를 보기 위해서 원하는 페이지 수를 페이지 매개변수에 입력하면 된다.
        Pageable pageable = PageRequest.of(0, 10);

        // findAll()에서 페이징에 관련된 쿼리를 실행, 결과를 이용하여 Page<엔티티 타입> 객체로 저장.
        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println("result >> " + result);

        System.out.println("--------------------------------");

        // 전체 페이지 수
        System.out.println("Total Page >> " + result.getTotalPages());
        // 전체 개수
        System.out.println("Total Count >> " + result.getTotalElements());
        // 현재 페이지 번호 (0부터 시작)
        System.out.println("Page Number >> " + result.getNumber());
        // 페이지당 데이터 개수
        System.out.println("Page Size >> " + result.getSize());
        // 다음 페이지 존재 여부
        System.out.println("has next page? >> " + result.hasNext());
        // 시작 페이지(0) 여부
        System.out.println("First page? >> " + result.isFirst());

        System.out.println("--------------------------------");

        // getContent() : 실제 페이지의 데이터를 처리, List<엔티티 타입>으로 처리
        for (Memo memo : result.getContent()) {
            System.out.println(memo);
        }
    } // testPageDefault()

    @Test
    public void testSort() {

        Sort sort1 = Sort.by("mno").descending();

        Pageable pageable = PageRequest.of(0, 10, sort1);
        Page<Memo> result = memoRepository.findAll(pageable);

        // getContent()와 비교해보기.
        result.get().forEach(memo -> {
            System.out.println(memo);
        });

//        result.get().forEach(new Consumer<Memo>() {
//            @Override
//            public void accept(Memo memo) {
//                System.out.println(memo);
//            }
//        });
    } // testSort()

    @Test
    public void testQueryMethods() {
        // mno 값이 70~80 사이인 행을 출력하고 내림차순으로 정렬
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L, 80L);

        for (Memo memo : list) {
            System.out.println(memo);
        }
    } // testQueryMethods()

    @Test
    public void testQueryMethodWithPageable() {
        Pageable pageable = PageRequest.of(0,10, Sort.by("Mno").descending());

        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);

        result.get().forEach(memo -> System.out.println(memo));
    } // testQueryMethodWithPageable()

    @Commit
    @Transactional
    @Test
    void testDeleteQueryMethods() {
        memoRepository.deleteMemoByMnoLessThan(10L);
    } // testDeleteQueryMethods(

    @Test
    void testGetListDesc() {
        List<Memo> list = memoRepository.getListDesc();

        for(Memo memo : list) {
            System.out.println(memo);
        }
    } // testGetListDesc()

    @Test
    void testUpdateMemoText() {
        memoRepository.updateMemoText(50L,"Changed...");
    } // testUpdateMemoText()

    @Test
    void testUpdateMemoTextObject() {
        Memo memo = Memo.builder().mno(30L).memoText("Change2").build();
        memoRepository.updateMemoTextObject(memo);
    } // testUpdateMemoTextObject()

    @Test
    void testGetListWithQuery() {
        Pageable pageable = PageRequest.of(0,10,Sort.by("mno").descending());
        Page<Memo> result = memoRepository.getListWithQuery(20L, pageable);
        result.get().forEach(memo -> System.out.println(memo));
    }

    @Test
    void testGetListWithQueryObject() {
        Pageable pageable = PageRequest.of(0,10,Sort.by("mno").descending());
        Page<Object[]> result = memoRepository.getListWithQueryObject(20L, pageable);
        result.get().forEach(objects -> System.out.println(objects[0]+"/"+objects[1]+"/"+objects[2]));
    }

    @Test
    void testGetNativeResult() {
//        List<Object[]> result = memoRepository.getNativeResult();
//
//        for(Object[] objects : result) {
//            System.out.println(objects[0]+"/"+objects[1]);
//        }

        List<Memo> result = memoRepository.getNativeResult();

        for(Memo memo : result) {
            System.out.println(memo);
        }
    }
}

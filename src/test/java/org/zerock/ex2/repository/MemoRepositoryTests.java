package org.zerock.ex2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.ex2.entity.Memo;

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

  @Test
  public void testInsert(){
    IntStream.rangeClosed(1,100).forEach(i -> {
      Memo memo = Memo.builder().memoText("Sample..."+i).build();
      memoRepository.save(memo);
      // 해당 객체가 존재하면 UPDATE, 없다면 INSERT 실행.
    });
  }
}

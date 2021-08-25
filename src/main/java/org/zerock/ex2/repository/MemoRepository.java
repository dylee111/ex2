package org.zerock.ex2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.ex2.entity.Memo;

import javax.transaction.Transactional;
import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    // Query Methods : 메서드 이름 자체가 query문이 됨.
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

    void deleteMemoByMnoLessThan(Long num);
//    void deleteByMnoLessThan(Long num); // 위 메서드와 동일한 결과.

    // query는 소문자로 작성.
    // Memo라는 이름의 Table이 없지만 List의 제네릭 타입을 Memo class로 정의했기 때문에 query문에서 Memo를 참조 -> 객체 지향 쿼리
    // 테이블 대신 엔티티 객체를 이용 / 칼럼 대신 클래스 멤버변수 이용.
    @Query("select m from Memo m where m.mno > 30 and m.mno < 40 order by m.mno desc ")
    List<Memo> getListDesc();

    @Transactional
    @Modifying
    // =: 대입 연산자
    @Query("update Memo m set m.memoText = :memoText where m.mno = :mno ")
    int updateMemoText(@Param("mno") Long mno, @Param("memoText") String memoText);

    @Transactional
    @Modifying
    @Query("update Memo m set m.memoText = :#{#param.memoText} " +
            "where m.mno = :#{#param.mno} ")
    int updateMemoTextObject(@Param("param") Memo memo);

    @Query(value = "select m from Memo m where m.mno > :mno ",
            countQuery = "select count(m) from Memo m where m.mno > :mno ")
    Page<Memo> getListWithQuery(Long mno, Pageable pageable);

    @Query(value = "select m.mno, m.memoText, CURRENT_DATE from Memo m " +
            "where m.mno > :mno ",
            countQuery = "select count(m) from Memo m where m.mno > :mno ")
    Page<Object[]> getListWithQueryObject(Long mno, Pageable pageable);

    @Query(value = "select * from tbl_memo where mno > 70 ", nativeQuery = true)
//    List<Object[]> getNativeResult();
    List<Memo> getNativeResult();
}

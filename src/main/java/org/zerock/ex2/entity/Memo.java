package org.zerock.ex2.entity;

import lombok.*;

import javax.persistence.*;

@Entity // 해당 클래스가 entity class임을 표시, 한 개 이상의 테이블 생성
@Table(name = "tbl_memo") // 관계형 데이터 베이스에 생성되는 테이블과 관련 설정
@ToString
@Getter
@Builder             // 생성자 생성
@AllArgsConstructor  // 모든 멤버변수에 대한 생성자 (@Builder와 함께 사용)
@NoArgsConstructor   // 기본 생성자 (@Builder와 함께 사용)
public class Memo {

  @Id // PK(Primary Key) 관련 설정
  @GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스
  private Long mno;

  //추가적인 field가 필요한 경우
  // (length = 200) == varchar2(200)
  @Column(length = 200, nullable = false)
  private String memoText;

}

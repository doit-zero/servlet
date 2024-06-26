package hello.servlet.domain.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryTest {
    MemberRepository memberRepository = MemberRepository.getInstance();


    // 테스트 끌날때마다 하는 행동을 정의
    @AfterEach
    void afterEach(){
        memberRepository.clearStore();
    }

    @Test
    void save() throws Exception{
    //given
        Member member = new Member("hello",20);
    //when
        Member savedMember = memberRepository.save(member);

    //then
        Member findMember = memberRepository.findById(savedMember.getId());
        assertThat(findMember).isSameAs(savedMember);
    }

    @Test
    void findAll() throws Exception{
    //given
        Member member1 = new Member("hello1",20);
        Member member2 = new Member("hello2",20);

        memberRepository.save(member1);
        memberRepository.save(member2);
    //when
        List<Member> memberList = memberRepository.findAll();
    //then
        assertThat(memberList.size()).isEqualTo(2);
        assertThat(memberList).contains(member1,member2);
    }
}

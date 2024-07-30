package adhd.diary.member.service;

import adhd.diary.member.domain.Member;
import adhd.diary.member.domain.MemberRepository;
import adhd.diary.member.dto.request.CompleteRegistrationRequest;
import adhd.diary.member.dto.response.MemberResponse;
import adhd.diary.member.dto.response.MemberSignUpResponse;
import adhd.diary.member.exception.MemberNotFoundException;
import adhd.diary.response.ResponseCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse updateNickname(String email, String nickname) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(ResponseCode.MEMBER_NOT_FOUND));
        member.updateNickname(nickname);

        return new MemberResponse(member.getEmail(), member.getRole(), member.getSocialProvider(), member.getSocialId(), member.getNickname(), member.getBirthDay());
    }

    @Transactional
    public void updateBirthDay(String email, LocalDate birthDay) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(ResponseCode.MEMBER_NOT_FOUND));

        member.updateBirthDay(birthDay);
    }

    @Transactional
    public MemberSignUpResponse completeRegistration(CompleteRegistrationRequest registrationRequest, String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(ResponseCode.MEMBER_NOT_FOUND));

        updateNickname(member.getEmail(), registrationRequest.getNickname());
        updateBirthDay(member.getEmail(), registrationRequest.getBirthDay());

        memberRepository.saveAndFlush(member);

        return new MemberSignUpResponse(member.getEmail(), member.getRole(), member.getSocialProvider(), member.getSocialId(), member.getNickname(), member.getBirthDay());
    }
}
package adhd.diary.auth.service;

import adhd.diary.auth.dto.SocialLoginRequest;
import adhd.diary.auth.dto.TokenResponse;
import adhd.diary.auth.jwt.JwtService;
import adhd.diary.member.domain.Member;
import adhd.diary.member.domain.MemberRepository;
import adhd.diary.member.domain.Role;
import adhd.diary.member.dto.CompleteRegistrationRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    public AuthService(MemberRepository memberRepository, JwtService jwtService) {
        this.memberRepository = memberRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public void updateNickname(Long memberId, String nickname) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        member.updateNickname(nickname);
    }

    @Transactional
    public void updateBirthDay(Long memberId, LocalDate birthDay) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        member.updateBirthDay(birthDay);
    }

    @Transactional
    public void completeRegistration(CompleteRegistrationRequest registrationRequest, String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        updateNickname(member.getId(), registrationRequest.getNickname());
        updateBirthDay(member.getId(), registrationRequest.getBirthDay());

        memberRepository.saveAndFlush(member);
    }

}

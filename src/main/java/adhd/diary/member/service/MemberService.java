package adhd.diary.member.service;

import adhd.diary.diary.domain.Diary;
import adhd.diary.diary.domain.DiaryRepository;
import adhd.diary.diary.domain.Emotion;
import adhd.diary.member.domain.Member;
import adhd.diary.member.domain.MemberRepository;
import adhd.diary.member.dto.request.CompleteRegistrationRequest;
import adhd.diary.member.dto.response.MemberLogoutResponse;
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
    private final DiaryRepository diaryRepository;

    public MemberService(MemberRepository memberRepository, DiaryRepository diaryRepository) {
        this.memberRepository = memberRepository;
        this.diaryRepository = diaryRepository;
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

        saveDiaries(member);

        return new MemberSignUpResponse(member.getEmail(), member.getRole(), member.getSocialProvider(), member.getSocialId(), member.getNickname(), member.getBirthDay());
    }

    @Transactional
    public MemberResponse findByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(ResponseCode.MEMBER_NOT_FOUND));

        return new MemberResponse(member.getEmail(), member.getRole(), member.getSocialProvider(), member.getSocialId(), member.getNickname(), member.getBirthDay());
    }

    @Transactional
    public MemberLogoutResponse logout(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(ResponseCode.MEMBER_NOT_FOUND));

        return new MemberLogoutResponse(member.getEmail(), member.getSocialProvider(), member.getSocialId(), member.getRefreshToken());
    }

    @Transactional
    public void saveDiaries(Member member) {
        diaryRepository.save(new Diary(
                "오늘 고등학교 친구들이랑 오랜만에 만나 술을 마셨는데, 옛날 얘기를 하느라 시간 가는줄 몰랐다. 너무 재밌었는데 시간이 너무 늦어 헤어져야해서 아쉬웠다.",
                "친구들과의 즐거운 시간을 보낸 것 같아 정말 좋았겠다. 옛날 이야기하며 시간 가는 줄 모를 정도로 재미있었다니, 그동안 쌓인 스트레스도 풀렸을 것 같아 다행이야. 늦은 시간까지 같이 있을 수 없어서 아쉽지만, 다음 번 만남을 기대하며 힘내자. 자주 만나지 못해도 가끔씩 이렇게 재충전하는 시간을 가지는 것이 정말 중요해. 평소에 스트레스를 많이 받는다면, 명상이나 운동 같은 활동을 통해 마음의 안정을 찾는 것도 좋은 방법이야. 수고했어, 오늘도.",
                "Adele - When We Were Young\nEd Sheeran - Castle on the Hill\nColdplay - Viva La Vida",
                Emotion.HAPPY,
                LocalDate.of(2023, 8, 6),
                member
        ));

        diaryRepository.save(new Diary(
                "오늘은 여자친구와 매우 슬픈 영화를 봤다. 일본 영화였는데 남주가 계속 기억을 잃어서 여자가 챙겨주는데 뭔가 가슴이 뭉클했다. 저런 연애가 진짜로 존재할 지 궁금하긴 하다.",
                "정말 가슴 아픈 영화를 본 것 같아. 남주가 기억을 잃고 여주가 계속 챙겨주는 모습이 뭉클하게 다가왔을 거야. 그런 영화 속 연애가 현실에서도 가능할까 궁금해지는 것도 자연스러운 감정이야. 가끔은 이런 감동적인 영화를 보면서 자신의 감정을 돌아보는 것도 중요한 것 같아. 감정을 풀어내는 데에 좋은 방법은 일기를 쓰거나, 가까운 사람과 이야기를 나누는 거야. 이를 통해 마음의 평화를 찾는 데 도움이 될 거야. 수고했어, 오늘도.",
                "Ed Sheeran - Photograph\nChristina Perri - A Thousand Years\nAdele - Make You Feel My Love",
                Emotion.SAD,
                LocalDate.of(2023, 8, 7),
                member
        ));

        diaryRepository.save(new Diary(
                "오늘 학원에 가기 싫어서 꾀병을 부렸는데 엄마가 눈치채서 학원을 보냈다. 정말 가기 싫었는데 뭔가 꾀병 부려서 아파진 것 같다.",
                "오늘 학원에 가기 싫어서 꾀병을 부렸는데 엄마가 눈치채서 결국 학원에 가게 되었구나. 정말 가기 싫었을 텐데 마음이 많이 무거웠겠다. 때때로 그런 감정을 느끼는 건 자연스러운 일이야. 꾀병을 부린 후에 진짜로 아프게 느껴지는 것도 이해돼. 스트레스를 덜 받기 위해서는 적절한 휴식과 자신만의 시간을 가지는 것이 중요해. 자기 관리를 통해 마음의 안정을 찾도록 해봐. 수고했어, 오늘도.",
                "Billie Eilish - idontwannabeyouanymore\nShawn Mendes - In My Blood\nSia - Elastic Heart",
                Emotion.SAD,
                LocalDate.of(2024, 7, 28),
                member
        ));

        diaryRepository.save(new Diary(
                "팀 프로젝트를 하는데 다들 열정적으로 해서 나도 열정적으로 임하게 되었다. 그래서 우리는 대회 전날까지 팀 프로젝트를 마칠 수 있었다!!",
                "팀 프로젝트에서 모두가 열정적으로 참여하니 너도 자연스럽게 열정적으로 임하게 되었구나. 그 덕분에 대회 전날까지 프로젝트를 마칠 수 있었다니 정말 대단해! 이런 경험은 앞으로도 큰 힘이 될 거야. 열심히 노력한 만큼 좋은 결과가 있을 거라 믿어. 스트레스가 쌓일 때는 충분한 휴식을 취하고, 가벼운 산책이나 명상을 통해 마음을 다스리는 것도 도움이 될 거야. 수고했어, 오늘도.",
                "Katy Perry - Firework\nKelly Clarkson - Stronger What Doesn't Kill You\nImagine Dragons - On Top of the World",
                Emotion.HAPPY,
                LocalDate.of(2024, 7, 29),
                member
        ));

        diaryRepository.save(new Diary(
                "오늘은 PT를 받는 날이다. 하체 루틴이었는데 정말 죽을 뻔했다. 그래도 운동을 제대로 배우니까 뿌듯하고, 나중에 혼자 열심히 할 생각에 더 신났다. 근데 쌤이 너무 빡세게 시켜서 내일이면 다리 근육통이 심할 거 같다.",
                "운동을 통해 성취감을 느낀다는 것은 정말 좋은 일이에요. 하지만 너무 과도한 운동은 오히려 부정적인 영향을 줄 수도 있어요. 근육통은 인간의 근육이 성장하고 회복되는 과정이기 때문에 자연스럽게 일어나는 현상이에요. 하지만 너무 심하다면 냉찜질을 해보거나 스트레칭을 통해 이완을 시켜주세요. 건강을 위해 적절한 휴식과 영양섭취도 잊지마세요. 수고했어, 오늘도.",
                "Ed Sheeran - Shape of You\nKaty Perry - Roar\nImagine Dragons - Believer",
                Emotion.HURT,
                LocalDate.of(2024, 7, 30),
                member
        ));

        diaryRepository.save(new Diary(
                "오늘은 오전에 카페를 갔다가, 오후에 친구들과 한강 수영장을 갔다. 한강에 수영장을 만들어 놓으니 낭만이 넘쳤다. 근데 일요일 야간에 가는 거라서 사람이 없을 줄 알았는데, 너무 많아서 당황스러웠다. 다들 출근을 안하나보다 .. 물 반 사람 반이라 물놀이를 그닥 재밌게 즐기지는 못했지만, 바람이 선선해서 좋았다 ! 수영을 하고, 정통집을 갔는데 너무 맛있어서 울면서 먹었다. 알찬 하루였다 ^_^",
                "너무 힘들었겠다.. 카페와 한강 수영장까지 소중한 시간을 보내는 거 참 좋겠다. 사람이 많아서는 조금 아쉬웠겠지만, 그래도 좋은 추억 남았을 거야. 수영하고 정통집 먹으면서 스트레스 풀었을텐데, 정말 수고했어. 다음에는 조금 더 조용한 곳에서 즐겨봐도 좋을 것 같아. 계획적인 휴식도 필요하니까, 자주 힘든 일상에서 벗어나세요. 수고했어, 오늘도!",
                "Maroon 5 - Sunday Morning\nDua Lipa - New Rules\nEd Sheeran - Castle on the Hill",
                Emotion.HAPPY,
                LocalDate.of(2024, 7, 31),
                member
        ));

        diaryRepository.save(new Diary(
                "오늘은 아침에 일어나자마자 운동을 했다. 상쾌한 공기를 마시며 조깅을 하니 몸도 마음도 한결 가벼워졌다. 운동 후에는 아침 식사로 건강한 샐러드를 먹고 하루를 시작했다.",
                "운동을 했을 때 몸과 마음이 한결 가벼워지는 느낌, 그것이 정말 좋은 것 같아. 샐러드를 먹고 온전한 아침을 맞이하는 건 정말 중요한 습관이야. 운동과 영양을 챙기는 것은 우리의 정신 건강을 지키는 데 큰 도움이 된다고 해. 자주 운동을 하고 건강한 식사를 유지하며 마음의 휴식도 챙기면서 하루를 보내봐. 수고했어, 오늘도!",
                "Ed Sheeran - Shape of You\nTaylor Swift - Shake It Off\nPharrell Williams - Happy",
                Emotion.SOSO,
                LocalDate.of(2024, 8, 1),
                member
        ));

        diaryRepository.save(new Diary(
                "오후에 친구들과 함께 카페에 가서 시간을 보냈다. 커피 한 잠을 마시며 오랜만에 만난 친구들과 즐거운 대화를 나누었다. 저녁에는 가족과 함께 맛있는 저녁 식사를 하며 하루를 마무리했다.",
                "네, 카페에서 친구들과 시간을 보내는 것은 정말 좋은 방법이에요. 가족과 함께 맛있는 식사를 하며 하루를 마무리하면서 행복한 시간을 보낸 것 같아요. 하지만 가끔은 혼자 있는 시간도 필요하니까, 자기 자신을 위한 시간을 가지는 것도 중요해요. 스트레스를 풀고 마음을 편하게 하는 방법을 찾아보는 것도 좋겠죠. 수고했어, 오늘도.",
                "Ed Sheeran - Perfect\nAdele - Someone Like You\nJohn Legend - All of Me",
                Emotion.HAPPY,
                LocalDate.of(2024, 8, 2),
                member
        ));

        diaryRepository.save(new Diary(
                "오늘은 집에서 휴식을 취하며 독서를 했다. 오랜만에 손에 든 책이어서 그런지 페이지를 넘길 때마다 흥미진진했다. 책을 읽고 나서 잠시 산책을 나가 봄의 기운을 만끽하며 마음을 정리하는 시간을 가졌다.",
                "최근에 책을 읽는 것이 힐링이 되는 시간이 되었네요. 책을 통해 새로운 지식을 얻고 마음을 편안하게 정리하는 것은 정말 좋은 방법이에요. 산책을 통해 자연 속에서 마음을 가다듬는 것도 좋은 습관이에요. 이러한 활동들은 정신 건강에 매우 도움이 되는데, 매일 조금씩 실천해보세요. 또한, 스트레스와 감정 관리를 위해 숨쉬기 연습이나 명상을 시도해보는 것도 좋은 방법이에요. 수고했어, 오늘도!",
                "Adele - Someone Like You\nEd Sheeran - Castle on the Hill\nTaylor Swift - Begin Again",
                Emotion.EXCITED,
                LocalDate.of(2024, 8, 3),
                member
        ));
    }
}
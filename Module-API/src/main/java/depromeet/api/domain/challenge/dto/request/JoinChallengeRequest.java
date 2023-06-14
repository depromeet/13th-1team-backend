package depromeet.api.domain.challenge.dto.request;


import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinChallengeRequest {

    @NotNull(message = "챌린지 동안 사용할 닉네임 입력하세요.")
    private String nickname;

    @NotNull(message = "참여하는 챌린지의 목표 금액을 입력하세요.")
    private Integer currentCharge;
}
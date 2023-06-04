package depromeet.api.domain.record.validator;


import depromeet.domain.record.domain.RecordEvaluation;
import depromeet.domain.record.exception.RecordEvaluationNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RecordValidator {
    public void validateEvaluationType(int evaluation) {
        try {
            RecordEvaluation.getEnumTypeByValue(evaluation);
        } catch (RecordEvaluationNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    // 현재 챌린지에 유저가 가입했는지 확인하는 메서드
    // 지금 챌린지와 유저가 ManyToOne으로 설정되어 있어, ManyToMany로 수정된 뒤 코드 작성
    public void validateUnparticipatedChallenge(String socialId, Long challengeId) {}
}

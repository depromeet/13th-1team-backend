package depromeet.api.domain.record.dto.response;


import depromeet.domain.record.domain.Record;
import depromeet.domain.record.domain.RecordEvaluation;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateRecordResponse {
    private final Long id;
    private String name;
    private final String content;
    private final String imgUrl;
    private final RecordEvaluation evaluation;

    public static CreateRecordResponse of(Record record) {
        return CreateRecordResponse.builder()
                .id(record.getId())
                .name(record.getName())
                .content(record.getContent())
                .imgUrl(record.getImgUrl())
                .evaluation(record.getEvaluation())
                .build();
    }
}

package depromeet.api.domain.challenge.dto.request;


import depromeet.domain.challenge.domain.Category;
import depromeet.domain.rule.domain.Rule;
import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateChallengeRequest {

    private Category category;

    @NotBlank(message = "title can not be blank")
    private String title;

    @Min(10000)
    @Max(999999)
    private int price;

    private String imageUrl;

    @NotBlank(message = "hashtag can not be blank")
    private String hashtag;

    private int availableCount;

    private List<Rule> challengeRule;

    private int period;

    private LocalDate startAt;

    private LocalDate endAt;
}
package depromeet.api.domain.emoji.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import depromeet.api.config.security.filter.JwtRequestFilter;
import depromeet.api.domain.emoji.dto.request.CreateEmojiRequest;
import depromeet.api.domain.emoji.dto.response.CreateEmojiResponse;
import depromeet.api.domain.emoji.usecase.CreateEmojiUseCase;
import depromeet.api.util.AuthenticationUtil;
import depromeet.domain.record.domain.EmojiType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(
        controllers = EmojiController.class,
        excludeFilters = {
            @ComponentScan.Filter(
                    type = FilterType.ASSIGNABLE_TYPE,
                    classes = {JwtRequestFilter.class})
        })
@AutoConfigureDataJpa
@AutoConfigureMockMvc(addFilters = false)
class EmojiControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockBean private CreateEmojiUseCase createEmojiUseCase;

    private static MockedStatic<AuthenticationUtil> authenticationUtil;

    @BeforeAll
    public static void beforeAll() {
        authenticationUtil = mockStatic(AuthenticationUtil.class);
    }

    @AfterAll
    public static void afterAll() {
        authenticationUtil.close();
    }

    @Test
    @DisplayName("[Put] 이모지 등록")
    public void CreateRecordEmojiTest() throws Exception {
        // given
        CreateEmojiRequest createEmojiRequest =
                CreateEmojiRequest.builder().type(EmojiType.CRAZY_BEGGAR.getValue()).build();

        CreateEmojiResponse createEmojiResponse =
                CreateEmojiResponse.builder()
                        .emojisCount(1)
                        .type(createEmojiRequest.getType())
                        .selected(true)
                        .build();

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.put("/challenge/{recordId}/emojis", 1)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createEmojiRequest))
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);

        String socialId = "socialId";

        when(AuthenticationUtil.getCurrentUserSocialId()).thenReturn(socialId);
        when(createEmojiUseCase.execute(anyString(), anyLong(), any()))
                .thenReturn(createEmojiResponse);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.result.emojisCount")
                                .value(createEmojiResponse.getEmojisCount()),
                        jsonPath("$.result.type").value(createEmojiResponse.getType()),
                        jsonPath("$.result.selected").value(createEmojiResponse.getSelected()));
    }
}

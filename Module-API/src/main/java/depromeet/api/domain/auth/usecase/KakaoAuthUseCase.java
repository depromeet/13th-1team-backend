package depromeet.api.domain.auth.usecase;


import depromeet.api.domain.auth.dto.TokenInfo;
import depromeet.api.domain.auth.dto.request.KakaoAuthRequest;
import depromeet.api.domain.auth.dto.response.KakaoAuthResponse;
import depromeet.api.domain.auth.feign.UserInfo;
import depromeet.api.domain.auth.mapper.AuthMapper;
import depromeet.api.domain.auth.validator.OAuthValidator;
import depromeet.api.util.JwtUtil;
import depromeet.common.annotation.UseCase;
import depromeet.domain.user.adaptor.UserAdaptor;
import depromeet.domain.user.domain.Platform;
import depromeet.domain.user.domain.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@UseCase
public class KakaoAuthUseCase {

    private final UserAdaptor userAdaptor;
    private final OAuthValidator oAuthValidator;
    private final JwtUtil jwtUtil;
    private final AuthMapper authMapper;

    public KakaoAuthResponse execute(KakaoAuthRequest reqAuth) {

        oAuthValidator.sigVerification(reqAuth.getIdToken());

        UserInfo userInfo = oAuthValidator.reqUserInfo(reqAuth.getAccessToken());

        // 회원가입(가입 정보 없는 유저일 때만) 및 로그인
        User user =
                userAdaptor.authUser(
                        userInfo.getNickname(),
                        userInfo.getEmail(),
                        userInfo.getSub(),
                        Platform.KAKAO);

        // 토큰 생성
        TokenInfo tokenInfo =
                jwtUtil.generateTokenInfo(
                        user.getSocial().getId(), user.getSocial().getPlatform(), user.getRole());

        // refresh token 저장
        jwtUtil.storeRefreshToken(user.getSocial().getId(), tokenInfo.getRefreshToken());

        return authMapper.toKakaoAuthResponse(tokenInfo.getAccessToken());
    }
}

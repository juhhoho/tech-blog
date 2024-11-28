package com.blog.auth.service.user;

import com.blog.auth.dto.SocialUserDetails;
import com.blog.auth.dto.UserDTO;
import com.blog.auth.dto.response.NaverResponse;
import com.blog.auth.dto.response.OAuth2Response;
import com.blog.auth.entity.SocialUser;
import com.blog.auth.repository.SocialUserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SocialUserService extends DefaultOAuth2UserService {

    private final SocialUserRepository socialUserRepository;

    public SocialUserService(SocialUserRepository socialUserRepository) {
        this.socialUserRepository = socialUserRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {
//            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {
            return null;
        }

        /*
        특정 조건에 따라 사용자를 ROLE_ADMIN 설정
        ex. 특정 이메일 등
        * */

        String identifier = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        Optional<SocialUser> existUser = socialUserRepository.findSocialUserByIdentifier(identifier);

        // 새로운 사용자
        if (existUser.isEmpty()) {

            SocialUser userEntity = SocialUser.builder()
                    .identifier(identifier)
                    .email(oAuth2Response.getEmail())
                    .name(oAuth2Response.getName())
                    .role("ROLE_USER")
                    .provider(oAuth2Response.getProvider())
                    .build();

            socialUserRepository.save(userEntity);

            UserDTO userDTO = UserDTO.builder()
                    .username(identifier)
                    .name(oAuth2Response.getName())
                    .role("ROLE_USER")
                    .build();

            return new SocialUserDetails(userDTO);
        }
        else {
            SocialUser cuser = existUser.get();
            cuser.changeEmail(oAuth2Response.getEmail());
            cuser.changeName(oAuth2Response.getName());

            socialUserRepository.save(cuser);

            UserDTO userDTO = UserDTO.builder()
                    .username(cuser.getIdentifier())
                    .name(oAuth2Response.getName())
                    .role(cuser.getRole())
                    .build();

            return new SocialUserDetails(userDTO);
        }
    }
}

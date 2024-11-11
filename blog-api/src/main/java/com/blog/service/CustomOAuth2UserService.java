package com.blog.service;

import com.blog.dto.CustomOAuth2User;
import com.blog.dto.UserDTO;
import com.blog.dto.response.NaverResponse;
import com.blog.dto.response.OAuth2Response;
import com.blog.entity.UserEntity;
import com.blog.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User);

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

        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        UserEntity existUser = userRepository.findByUserName(username);

        if (existUser == null) {

            UserEntity userEntity = UserEntity.builder()
                    .userName(username)
                    .email(oAuth2Response.getEmail())
                    .name(oAuth2Response.getName())
                    .role("ROLE_USER")
                    .build();

            userRepository.save(userEntity);

            UserDTO userDTO = UserDTO.builder()
                    .username(username)
                    .name(oAuth2Response.getName())
                    .role("ROLE_USER")
                    .build();

            return new CustomOAuth2User(userDTO);
        }
        else {
            existUser.changeEmail(oAuth2Response.getEmail());
            existUser.changeName(oAuth2Response.getName());

            userRepository.save(existUser);

            UserDTO userDTO = UserDTO.builder()
                    .username(existUser.getUserName())
                    .name(oAuth2Response.getName())
                    .role(existUser.getRole())
                    .build();

            return new CustomOAuth2User(userDTO);
        }
    }
}

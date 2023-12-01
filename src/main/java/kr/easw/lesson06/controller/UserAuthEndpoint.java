package kr.easw.lesson06.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.easw.lesson06.model.dto.ExceptionalResultDto;
import kr.easw.lesson06.model.dto.UserAuthenticationDto;
import kr.easw.lesson06.model.dto.UserDataEntity;
import kr.easw.lesson06.service.UserDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.http.HttpResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserAuthEndpoint {
    private final UserDataService userDataService;


    // JWT 인증을 위해 사용되는 엔드포인트입니다.
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserDataEntity entity) {
        try {
            // 로그인을 시도합니다.
            return ResponseEntity.ok(userDataService.createTokenWith(entity));
        } catch (Exception ex) {
            // 만약 로그인에 실패했다면, 400 Bad Request를 반환합니다.
            return ResponseEntity.badRequest().body(new ExceptionalResultDto(ex.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(HttpServletRequest httpServletRequest) {
        try {
            String userId = httpServletRequest.getParameter("username");
            String password = httpServletRequest.getParameter("password");

            if (userDataService.isUserExists(userId)) {
                return ResponseEntity.badRequest().body("User of this ID already exists");
            }

            UserDataEntity user = new UserDataEntity(0l, userId, password, false);
            userDataService.createUser(user);

            return ResponseEntity.ok("Registration success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration fail: " + e.getMessage());
        }
    }
}


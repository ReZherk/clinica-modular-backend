package ReZherk.clinica.sistema.modules.auth.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ReZherk.clinica.sistema.modules.auth.application.dto.request.LoginRequestDto;
import ReZherk.clinica.sistema.modules.auth.application.dto.response.LoginResponseDto;
import ReZherk.clinica.sistema.modules.auth.application.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

 private final AuthService authService;

 @PostMapping("/login")
 public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginDto) {
  LoginResponseDto response = authService.login(loginDto);
  return ResponseEntity.ok(response);
 }
}

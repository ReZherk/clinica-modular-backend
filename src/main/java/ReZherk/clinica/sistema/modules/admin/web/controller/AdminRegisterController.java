package ReZherk.clinica.sistema.modules.admin.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ReZherk.clinica.sistema.modules.admin.application.dto.request.RegisterMedicoDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.RegisterResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminRegisterController {
 private final AdminService adminService;

 @PostMapping("/medicos/register")
 public ResponseEntity<RegisterResponseDto> registrarMedico(@Valid @RequestBody RegisterMedicoDto registerDto) {

  RegisterResponseDto response = adminService.registrarMedico(registerDto);

  return new ResponseEntity<>(response, HttpStatus.CREATED);
 }
}

package ReZherk.clinica.sistema.modules.patient.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ReZherk.clinica.sistema.modules.patient.application.dto.request.RegisterPacienteDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.RegisterResponseDto;
import ReZherk.clinica.sistema.modules.patient.application.service.UsuarioService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PatientController {

  private final UsuarioService usuarioService;

  @PostMapping("/register")
  public ResponseEntity<RegisterResponseDto> registerPaciente(
      @Valid @RequestBody RegisterPacienteDto registerDto) {

    RegisterResponseDto response = usuarioService.registerPaciente(registerDto);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }
}

package ReZherk.clinica.sistema.modules.patient.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ReZherk.clinica.sistema.modules.patient.application.dto.request.RegisterPacienteDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.RegisterResponseDto;
import ReZherk.clinica.sistema.modules.patient.application.service.PacienteService;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PatientController {

  private final PacienteService usuarioService;

  @PostMapping("/register")
  public ResponseEntity<RegisterResponseDto> registerPaciente(
      @Valid @RequestBody RegisterPacienteDto registerDto) {
    try {
      RegisterResponseDto response = usuarioService.registerPaciente(registerDto);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(new RegisterResponseDto(false, "Error en registro: " + e.getMessage()));
    }
  }

}

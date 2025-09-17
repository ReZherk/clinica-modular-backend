package ReZherk.clinica.sistema.modules.future_modules.controller;

import ReZherk.clinica.sistema.modules.future_modules.dto.RegisterMedicoDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.RegisterResponseDto;
import ReZherk.clinica.sistema.modules.patient.application.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/future_modules")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN_MEDICOS') or hasRole('SUPERADMIN')")
public class AdminMedicosController {

  private final UsuarioService usuarioService;

  // @PostMapping("/register")
  public ResponseEntity<RegisterResponseDto> registerMedico(
      @Valid @RequestBody RegisterMedicoDto registerDto) {

    RegisterResponseDto response = usuarioService.registerMedicoByAdmin(registerDto);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }
}

package ReZherk.clinica.sistema.controller;

import ReZherk.clinica.sistema.dto.CitaRequestDTO;
import ReZherk.clinica.sistema.dto.CitaResponseDTO;
import ReZherk.clinica.sistema.service.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

 private final CitaService citaService;

 @PostMapping
 public ResponseEntity<CitaResponseDTO> registrarCita(
   @Valid @RequestBody CitaRequestDTO dto) {
  return ResponseEntity.ok(citaService.registrarCita(dto));
 }

 @GetMapping("/paciente/{idPaciente}")
 public ResponseEntity<List<CitaResponseDTO>> listarPorPaciente(@PathVariable Integer idPaciente) {
  return ResponseEntity.ok(citaService.listarCitasPorPaciente(idPaciente));
 }

 @PutMapping("/{idCita}/cancelar")
 public ResponseEntity<Void> cancelarCita(@PathVariable Integer idCita) {
  citaService.cancelarCita(idCita);
  return ResponseEntity.noContent().build();
 }
}

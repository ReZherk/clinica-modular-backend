package ReZherk.clinica.sistema.modules.payment.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ReZherk.clinica.sistema.core.application.dto.ApiResponse;
import ReZherk.clinica.sistema.modules.payment.application.service.SeguroService;

@RestController
@RequestMapping("/api/admin/seguros")
@RequiredArgsConstructor
@Tag(name = "Seguros", description = "Endpoints para gestion de seguros medicos")
public class SeguroController {

 private final SeguroService seguroService;

 @PutMapping("/{idPacienteSeguro}/desactivar")
 @Operation(summary = "Desactivar seguro", description = "Desactiva el seguro vinculado al paciente")
 public ResponseEntity<ApiResponse<Void>> desactivarSeguro(
   @PathVariable Integer idPacienteSeguro) {

  seguroService.desactivarSeguro(idPacienteSeguro);

  return ResponseEntity.ok(new ApiResponse<>(true, "Seguro desactivado exitosamente", null));
 }
}

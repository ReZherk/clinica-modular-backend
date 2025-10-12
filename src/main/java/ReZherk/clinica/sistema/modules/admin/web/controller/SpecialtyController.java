package ReZherk.clinica.sistema.modules.admin.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ReZherk.clinica.sistema.core.application.dto.ApiResponse;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.EspecialidadRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.SpecialtyUpdateDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.SpecialtiesDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.SpecialtyResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.SpecialtyUpdateResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.SpecialtyWithDoctorsResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.service.EspecialidadService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/especialidad")
@RequiredArgsConstructor
public class SpecialtyController {

  private final EspecialidadService especialidadService;

  @PostMapping("/create")
  public ResponseEntity<SpecialtyResponseDto> crearEspecialidad(@RequestBody EspecialidadRequestDto dto) {
    SpecialtyResponseDto creada = especialidadService.crearEspecialidad(dto);
    return new ResponseEntity<>(creada, HttpStatus.CREATED);
  }

  @GetMapping("/active")
  public ResponseEntity<ApiResponse<List<SpecialtiesDto>>> listActiveSpecialties() {

    List<SpecialtiesDto> result = especialidadService.listarEspecialidades(true);
    return ResponseEntity.ok(new ApiResponse<>(true, "Se obtuvo exitosamente las especialidades activas", result));
  }

  @GetMapping("/inactive")
  public ResponseEntity<ApiResponse<List<SpecialtiesDto>>> listInactiveSpecialties() {

    List<SpecialtiesDto> result = especialidadService.listarEspecialidades(false);
    return ResponseEntity.ok(new ApiResponse<>(true, "Se obtuvo exitosamente las especialidades Inactivas", result));
  }

  @GetMapping("/all/with-medicos")
  public ResponseEntity<List<SpecialtyWithDoctorsResponseDto>> listarEspecialidadesConMedicos() {
    return ResponseEntity.ok(especialidadService.listarEspecialidadesConMedicos());
  }

  @GetMapping("/{id}/medicos")
  public ResponseEntity<SpecialtyWithDoctorsResponseDto> buscarMedicosPorEspecialidad(@PathVariable Integer id) {
    return ResponseEntity.ok(especialidadService.buscarMedicosPorEspecialidad(id));
  }

  @PutMapping("/{id}/activar")
  public ResponseEntity<SpecialtyUpdateResponseDto> activar(@PathVariable Integer id) {
    return ResponseEntity.ok(especialidadService.activar(id));
  }

  @PutMapping("/{id}/desactivar")
  public ResponseEntity<Void> desactivarEspecialidad(@PathVariable Integer id) {
    especialidadService.desactivarEspecialidad(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<SpecialtyUpdateResponseDto> actualizarEspecialidad(
      @PathVariable Integer id,
      @RequestBody SpecialtyUpdateDto dto) {
    return ResponseEntity.ok(especialidadService.actualizar(id, dto));
  }

}

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
  public ResponseEntity<ApiResponse<Void>> crearEspecialidad(@RequestBody EspecialidadRequestDto dto) {
    especialidadService.crearEspecialidad(dto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "Se creo la especialidad exitosamente.", null));
  }

  @GetMapping("/active")
  public ResponseEntity<ApiResponse<List<SpecialtyResponseDto>>> listActiveSpecialties() {

    List<SpecialtyResponseDto> result = especialidadService.listarEspecialidades(true);
    return ResponseEntity.ok(new ApiResponse<>(true, "Se obtuvo exitosamente las especialidades activas", result));
  }

  @GetMapping("/inactive")
  public ResponseEntity<ApiResponse<List<SpecialtyResponseDto>>> listInactiveSpecialties() {

    List<SpecialtyResponseDto> result = especialidadService.listarEspecialidades(false);
    return ResponseEntity.ok(new ApiResponse<>(true, "Se obtuvo exitosamente las especialidades Inactivas", result));
  }

  @PutMapping("/{id}/activar")
  public ResponseEntity<ApiResponse<Void>> activarEspecialidad(@PathVariable Integer id) {
    especialidadService.activar(id);
    return ResponseEntity.ok(new ApiResponse<>(true, "Se activo la especialidad", null));
  }

  @PutMapping("/{id}/desactivar")
  public ResponseEntity<ApiResponse<Void>> desactivarEspecialidad(@PathVariable Integer id) {
    especialidadService.desactivarEspecialidad(id);
    return ResponseEntity.ok(new ApiResponse<>(true, "Se desactivo la especialidad", null));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<SpecialtyResponseDto>> getSpecialityById(@PathVariable Integer id) {
    SpecialtyResponseDto result = especialidadService.obtenerEspecialidadPorId(id);

    return ResponseEntity.ok(new ApiResponse<>(true, "Especialidad encontrada", result));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<SpecialtyUpdateResponseDto>> actualizarEspecialidad(
      @PathVariable Integer id,
      @RequestBody SpecialtyUpdateDto dto) {

    SpecialtyUpdateResponseDto result = especialidadService.actualizar(id, dto);
    return ResponseEntity.ok(new ApiResponse<>(true, "Se actualizo correctamente", result));
  }

  // Revisar lo de abahjo .

  @GetMapping("/all/with-medicos")
  public ResponseEntity<List<SpecialtyWithDoctorsResponseDto>> listarEspecialidadesConMedicos() {
    return ResponseEntity.ok(especialidadService.listarEspecialidadesConMedicos());
  }

  @GetMapping("/{id}/medicos")
  public ResponseEntity<SpecialtyWithDoctorsResponseDto> buscarMedicosPorEspecialidad(@PathVariable Integer id) {
    return ResponseEntity.ok(especialidadService.buscarMedicosPorEspecialidad(id));
  }

}

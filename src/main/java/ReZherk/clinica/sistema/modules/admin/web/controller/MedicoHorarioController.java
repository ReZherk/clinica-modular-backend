package ReZherk.clinica.sistema.modules.admin.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ReZherk.clinica.sistema.core.application.dto.ApiResponse;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.AsignarHorariosRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.MedicoHorarioSearchRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.HorarioResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.MedicoHorarioAsignacionResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.MedicoHorarioPaginatedResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.service.MedicoHorarioService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/medico-horarios")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Gestión de Horarios Médicos", description = "Endpoints para administrar horarios de médicos")
public class MedicoHorarioController {

  private final MedicoHorarioService medicoHorarioService;

  @GetMapping("/{horas}")
  public ResponseEntity<ApiResponse<List<HorarioResponseDto>>> getHorariosPorTipo(@PathVariable int horas) {

    List<HorarioResponseDto> response = medicoHorarioService.obtenerHorariosPorTipo(horas);
    return ResponseEntity
        .ok(new ApiResponse<>(true, "Se encontraron horarios que coincidan para: " + horas + " horas", response));
  }

  @PostMapping("/asignar")
  @Operation(summary = "Asignar horarios a un médico", description = "Asigna bloques horarios a un médico específico. Los horarios anteriores se desactivan.")
  public ResponseEntity<ApiResponse<MedicoHorarioAsignacionResponseDto>> asignarHorarios(
      @Valid @RequestBody AsignarHorariosRequestDto request) {

    log.info("POST /api/admin/medico-horarios/asignar - Asignando horarios al médico ID: {}",
        request.getIdMedico());

    MedicoHorarioAsignacionResponseDto response = medicoHorarioService.asignarHorarios(request);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "Se asigno exitosamente los horarios.", response));
  }

  @GetMapping("/con-horarios")
  public ResponseEntity<ApiResponse<MedicoHorarioPaginatedResponseDto>> buscarMedicosConHorarios(
      @Parameter(description = "Nombre o apellido del médico") @RequestParam(required = false) String nombre,

      @Parameter(description = "Número de documento (DNI)") @RequestParam(required = false) String dni,

      @Parameter(description = "Código de Colegio Médico del Perú") @RequestParam(required = false) String cmp,

      @Parameter(description = "Nombre de la especialidad") @RequestParam(required = false) String especialidad,

      @Parameter(description = "Número de página (inicia en 0)") @RequestParam(defaultValue = "0") Integer page,

      @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") Integer size) {

    log.info(
        "GET /api/admin/medico-horarios/con-horarios - Filtros: nombre={}, dni={}, cmp={}, especialidad={}, page={}, size={}",
        nombre, dni, cmp, especialidad, page, size);

    MedicoHorarioSearchRequestDto searchRequest = MedicoHorarioSearchRequestDto.builder()
        .nombre(nombre)
        .dni(dni)
        .cmp(cmp)
        .especialidad(especialidad)
        .page(page)
        .size(size)
        .build();

    MedicoHorarioPaginatedResponseDto response = medicoHorarioService.buscarMedicosConHorarios(searchRequest);

    return ResponseEntity.ok(new ApiResponse<>(true, "Se encontro exitosamente los medicos y sus horarios.", response));
  }

  @GetMapping("/sin-horarios")
  public ResponseEntity<ApiResponse<MedicoHorarioPaginatedResponseDto>> buscarMedicosSinHorarios(
      @Parameter(description = "Nombre o apellido del médico") @RequestParam(required = false) String nombre,

      @Parameter(description = "Número de documento (DNI)") @RequestParam(required = false) String dni,

      @Parameter(description = "Código de Colegio Médico del Perú") @RequestParam(required = false) String cmp,

      @Parameter(description = "Nombre de la especialidad") @RequestParam(required = false) String especialidad,

      @Parameter(description = "Número de página (inicia en 0)") @RequestParam(defaultValue = "0") Integer page,

      @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") Integer size) {

    log.info(
        "GET /api/admin/medico-horarios/sin-horarios - Filtros: nombre={}, dni={}, cmp={}, especialidad={}, page={}, size={}",
        nombre, dni, cmp, especialidad, page, size);

    MedicoHorarioSearchRequestDto searchRequest = MedicoHorarioSearchRequestDto.builder()
        .nombre(nombre)
        .dni(dni)
        .cmp(cmp)
        .especialidad(especialidad)
        .page(page)
        .size(size)
        .build();

    MedicoHorarioPaginatedResponseDto response = medicoHorarioService.buscarMedicosSinHorarios(searchRequest);

    return ResponseEntity.ok(new ApiResponse<>(true, "Se encontro exitosamente los medicos sin horarios.", response));
  }

  @GetMapping("/medico/{idMedico}")
  public ResponseEntity<ApiResponse<List<HorarioResponseDto>>> obtenerHorariosPorMedico(
      @Parameter(description = "ID del médico") @PathVariable Integer idMedico) {

    log.info("GET /api/admin/medico-horarios/medico/{} - Obteniendo horarios", idMedico);

    List<HorarioResponseDto> horarios = medicoHorarioService.obtenerHorariosPorMedico(idMedico);

    return ResponseEntity.ok(new ApiResponse<>(true, "Se obtuvo horarios por medico", horarios));
  }

  @DeleteMapping("/medico/{idMedico}/horario/{idHorario}")
  public ResponseEntity<ApiResponse<Void>> eliminarHorarioMedico(
      @Parameter(description = "ID del médico") @PathVariable Integer idMedico,

      @Parameter(description = "ID del horario") @PathVariable Integer idHorario) {

    log.info("DELETE /api/admin/medico-horarios/medico/{}/horario/{} - Eliminando horario",
        idMedico, idHorario);

    medicoHorarioService.eliminarHorarioMedico(idMedico, idHorario);

    return ResponseEntity.ok(new ApiResponse<>(true, "Se elimino los horarios del medico", null));
  }
}
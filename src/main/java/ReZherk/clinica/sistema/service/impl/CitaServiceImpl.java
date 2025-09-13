package ReZherk.clinica.sistema.service.impl;

import ReZherk.clinica.sistema.dto.CitaRequestDTO;
import ReZherk.clinica.sistema.dto.CitaResponseDTO;
import ReZherk.clinica.sistema.entity.*;
import ReZherk.clinica.sistema.mapper.CitaMapper;
import ReZherk.clinica.sistema.repository.*;
import ReZherk.clinica.sistema.service.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaServiceImpl implements CitaService {

 private final CitaRepository citaRepository;
 private final PacienteDetalleRepository pacienteRepository;
 private final MedicoDetalleRepository medicoRepository;
 private final EspecialidadRepository especialidadRepository;
 private final SedeRepository sedeRepository;
 private final TarifarioRepository tarifarioRepository;
 private final CitaMapper citaMapper;

 @Override
 public CitaResponseDTO registrarCita(CitaRequestDTO dto) {
  // Validaciones básicas
  PacienteDetalle paciente = pacienteRepository.findById(dto.getIdPaciente())
    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

  MedicoDetalle medico = medicoRepository.findById(dto.getIdMedico())
    .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

  Especialidad especialidad = especialidadRepository.findById(dto.getIdEspecialidad())
    .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

  Sede sede = sedeRepository.findById(dto.getIdSede())
    .orElseThrow(() -> new RuntimeException("Sede no encontrada"));

  Tarifario tarifa = tarifarioRepository.findById(dto.getIdTarifa())
    .orElseThrow(() -> new RuntimeException("Tarifa no encontrada"));

  // Crear entidad
  Cita cita = Cita.builder()
    .fechaHora(dto.getFechaHora())
    .paciente(paciente)
    .medico(medico)
    .especialidad(especialidad)
    .sede(sede)
    .tarifa(tarifa)
    .estado(true)
    .build();

  // Guardar en BD
  Cita saved = citaRepository.save(cita);

  return citaMapper.toResponseDto(saved);
 }

 @Override
 public List<CitaResponseDTO> listarCitasPorPaciente(Integer idPaciente) {
  return citaRepository.findAll()
    .stream()
    .filter(c -> c.getPaciente().getIdUsuario().equals(idPaciente))
    .map(citaMapper::toResponseDto)
    .toList();
 }

 @Override
 public void cancelarCita(Integer idCita) {
  Cita cita = citaRepository.findById(idCita)
    .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
  cita.setEstado(false);
  citaRepository.save(cita);
 }
}

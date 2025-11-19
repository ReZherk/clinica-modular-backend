package ReZherk.clinica.sistema.modules.payment.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ReZherk.clinica.sistema.core.domain.entity.PacienteSeguro;
import ReZherk.clinica.sistema.core.domain.entity.Seguro;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.PacienteSeguroRepository;
import ReZherk.clinica.sistema.core.domain.repository.SeguroRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.modules.payment.application.dto.request.VincularSeguroRequestDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.response.PacienteSeguroResponseDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.response.SeguroResponseDto;
import ReZherk.clinica.sistema.modules.payment.application.mapper.PacienteSeguroMapper;
import ReZherk.clinica.sistema.modules.payment.application.mapper.SeguroMapper;
import ReZherk.clinica.sistema.modules.payment.application.service.SeguroService;
import ReZherk.clinica.sistema.modules.payment.application.validator.PagoValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeguroServiceImpl implements SeguroService {

 private final SeguroRepository seguroRepository;
 private final PacienteSeguroRepository pacienteSeguroRepository;
 private final UsuarioRepository usuarioRepository;
 private final SeguroMapper seguroMapper;
 private final PacienteSeguroMapper pacienteSeguroMapper;
 private final PagoValidator pagoValidator;

 @Override
 @Transactional(readOnly = true)
 public List<SeguroResponseDto> listarSegurosConConvenio() {
  log.info("Listando seguros con convenio");

  List<Seguro> seguros = seguroRepository.findSegurosConConvenio();

  return seguros.stream()
    .map(seguroMapper::toResponseDto)
    .collect(Collectors.toList());
 }

 @Override
 @Transactional(readOnly = true)
 public PacienteSeguroResponseDto obtenerSeguroPaciente(Integer idPaciente) {
  log.info("Obteniendo seguro del paciente ID: {}", idPaciente);

  PacienteSeguro pacienteSeguro = pacienteSeguroRepository
    .findByPacienteIdAndEstadoActivoTrue(idPaciente)
    .orElseThrow(() -> new ResourceNotFoundException("El paciente no tiene seguro vinculado"));

  return pacienteSeguroMapper.toResponseDto(pacienteSeguro);
 }

 @Override
 @Transactional
 public PacienteSeguroResponseDto vincularSeguro(VincularSeguroRequestDto request) {
  log.info("Vinculando seguro al paciente ID: {}", request.getIdPaciente());

  // Validar
  pagoValidator.validateVincularSeguro(
    request.getIdPaciente(),
    request.getFechaVigenciaInicio(),
    request.getFechaVigenciaFin());

  // Obtener entidades
  Usuario paciente = usuarioRepository.findById(request.getIdPaciente())
    .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));

  Seguro seguro = seguroRepository.findById(request.getIdSeguro())
    .orElseThrow(() -> new ResourceNotFoundException("Seguro no encontrado"));

  // Crear vinculo
  PacienteSeguro pacienteSeguro = PacienteSeguro.builder()
    .paciente(paciente)
    .seguro(seguro)
    .numeroPoliza(request.getNumeroPoliza())
    .fechaVigenciaInicio(request.getFechaVigenciaInicio())
    .fechaVigenciaFin(request.getFechaVigenciaFin())
    .estadoActivo(true)
    .build();

  pacienteSeguro = pacienteSeguroRepository.save(pacienteSeguro);

  log.info("Seguro vinculado exitosamente con ID: {}", pacienteSeguro.getIdPacienteSeguro());

  return pacienteSeguroMapper.toResponseDto(pacienteSeguro);
 }

 @Override
 @Transactional
 public void desactivarSeguro(Integer idPacienteSeguro) {
  log.info("Desactivando seguro ID: {}", idPacienteSeguro);

  PacienteSeguro pacienteSeguro = pacienteSeguroRepository.findById(idPacienteSeguro)
    .orElseThrow(() -> new ResourceNotFoundException("Seguro del paciente no encontrado"));

  pacienteSeguro.setEstadoActivo(false);
  pacienteSeguroRepository.save(pacienteSeguro);

  log.info("Seguro desactivado exitosamente");
 }
}

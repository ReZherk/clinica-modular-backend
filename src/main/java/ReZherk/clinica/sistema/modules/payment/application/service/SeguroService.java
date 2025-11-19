package ReZherk.clinica.sistema.modules.payment.application.service;

import java.util.List;

import ReZherk.clinica.sistema.modules.payment.application.dto.request.VincularSeguroRequestDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.response.PacienteSeguroResponseDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.response.SeguroResponseDto;

public interface SeguroService {

 List<SeguroResponseDto> listarSegurosConConvenio();

 PacienteSeguroResponseDto obtenerSeguroPaciente(Integer idPaciente);

 PacienteSeguroResponseDto vincularSeguro(VincularSeguroRequestDto request);

 void desactivarSeguro(Integer idPacienteSeguro);
}

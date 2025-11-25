package ReZherk.clinica.sistema.modules.medico.application.service;

import org.springframework.data.domain.Page;

import ReZherk.clinica.sistema.modules.appointment.application.dto.response.CitaResponseDto;
import ReZherk.clinica.sistema.modules.medico.application.dto.request.BuscarCitasFiltrosRequestDto;
import ReZherk.clinica.sistema.modules.medico.application.dto.request.DetalleCitaRequestDto;
import ReZherk.clinica.sistema.modules.medico.application.dto.request.MeetingLinkRequestDto;
import ReZherk.clinica.sistema.modules.medico.application.dto.response.CitaHistorialResponseDto;
import ReZherk.clinica.sistema.modules.medico.application.dto.response.CitaMedicoResponseDto;
import ReZherk.clinica.sistema.modules.medico.application.dto.response.DetalleCitaResponseDto;
import ReZherk.clinica.sistema.modules.medico.application.dto.response.MeetingLinkResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface MedicoCitaService {

 List<CitaMedicoResponseDto> listarCitasDelDia(Integer idMedico, LocalDate fecha);

 CitaMedicoResponseDto obtenerDetalleCita(Integer idCita);

 DetalleCitaResponseDto registrarDetalleCita(DetalleCitaRequestDto request);

 DetalleCitaResponseDto actualizarDetalleCita(Integer idDetalleCita, DetalleCitaRequestDto request);

 MeetingLinkResponseDto enviarEnlaceReunion(MeetingLinkRequestDto request);

 List<CitaHistorialResponseDto> obtenerHistorialPaciente(Integer idPaciente);

 Page<CitaMedicoResponseDto> buscarCitasConFiltros(BuscarCitasFiltrosRequestDto filtros);

 //////// Del modulo de citas
 CitaResponseDto cancelarCitaAdmin(Integer idCita);

 CitaResponseDto completarCita(Integer idCita);
}
package ReZherk.clinica.sistema.modules.appointment.application.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;

import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaCancelRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaCreateRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaFiltroRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaReprogramRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.HorariosDisponiblesRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.CitaListadoResult;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.CitaResponseDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.HorariosDisponiblesResponseDto;

public interface CitaService {

 // Paciente endpoints
 CitaResponseDto crearCitaPaciente(CitaCreateRequestDto request);

 CitaResponseDto cancelarCitaPaciente(CitaCancelRequestDto request);

 Page<CitaResponseDto> obtenerCitasPaciente(Integer idPaciente, Integer page, Integer size);

 CitaResponseDto obtenerDetalleCitaPaciente(Integer idCita, Integer idPaciente);

 HorariosDisponiblesResponseDto listarHorariosDisponibles(HorariosDisponiblesRequestDto request);

 // Admin endpoints

 CitaResponseDto marcarNoAtendida(Integer idCita);

 CitaResponseDto cancelarCitaAdmin(Integer idCita);

 CitaResponseDto completarCita(Integer idCita);

 CitaResponseDto reprogramarCita(CitaReprogramRequestDto request);

 Page<CitaListadoResult> listarCitasConFiltros(CitaFiltroRequestDto filtros);

 HorariosDisponiblesResponseDto listarHorariosMedicoCompleto(Integer idMedico, LocalDate fecha);
}

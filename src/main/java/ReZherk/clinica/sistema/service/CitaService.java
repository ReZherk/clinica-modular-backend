package ReZherk.clinica.sistema.service;

import ReZherk.clinica.sistema.dto.CitaRequestDTO;
import ReZherk.clinica.sistema.dto.CitaResponseDTO;

import java.util.List;

public interface CitaService {
 CitaResponseDTO registrarCita(CitaRequestDTO dto);

 List<CitaResponseDTO> listarCitasPorPaciente(Integer idPaciente);

 void cancelarCita(Integer idCita);
}

package ReZherk.clinica.sistema.modules.admin.application.mapper;

import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.modules.admin.application.dto.response.HorarioPorDiaDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.MedicoConHorariosResponseDto;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MedicoHorarioMapper {

 public MedicoConHorariosResponseDto mapFromStoredProcedure(Map<String, Object> resultMap) {
  if (resultMap == null || resultMap.isEmpty()) {
   return null;
  }

  // Parsear los horarios del formato concatenado
  String horariosStr = (String) resultMap.get("horarios");
  List<HorarioPorDiaDto> horariosPorDia = parseHorarios(horariosStr);

  return MedicoConHorariosResponseDto.builder()
    .idUsuario(getInteger(resultMap, "Id_Usuario"))
    .nombres((String) resultMap.get("Nombres"))
    .apellidos((String) resultMap.get("Apellidos"))
    .nombreCompleto((String) resultMap.get("nombre_completo"))
    .dni((String) resultMap.get("dni"))
    .email((String) resultMap.get("Email"))
    .telefono((String) resultMap.get("Telefono"))
    .cmp((String) resultMap.get("CMP"))
    .idEspecialidad(getInteger(resultMap, "Id_Especialidad"))
    .especialidad((String) resultMap.get("especialidad"))
    .duracionConsulta(getInteger(resultMap, "duracion_consulta"))
    .horasSemanales(getInteger(resultMap, "Horas_Semanales"))
    .horariosPorDia(horariosPorDia)
    .totalBloquesHorarios(getInteger(resultMap, "total_bloques_horarios"))
    .build();
 }

 private List<HorarioPorDiaDto> parseHorarios(String horariosStr) {
  if (horariosStr == null || horariosStr.trim().isEmpty()) {
   return Collections.emptyList();
  }

  // Formato: "Lunes:08:00-12:00|Lunes:14:00-18:00|Martes:08:00-12:00"
  Map<String, List<HorarioPorDiaDto.BloqueHorarioDto>> horariosPorDiaMap = new LinkedHashMap<>();

  String[] bloques = horariosStr.split("\\|");

  for (String bloque : bloques) {
   String[] partes = bloque.split(":");
   if (partes.length == 3) {
    String dia = partes[0];
    String[] horas = partes[2].split("-");

    if (horas.length == 2) {
     HorarioPorDiaDto.BloqueHorarioDto bloqueDto = HorarioPorDiaDto.BloqueHorarioDto.builder()
       .horaInicio(partes[1] + ":" + horas[0])
       .horaFin(horas[1])
       .build();

     horariosPorDiaMap.computeIfAbsent(dia, k -> new ArrayList<>()).add(bloqueDto);
    }
   }
  }

  return horariosPorDiaMap.entrySet().stream()
    .map(entry -> HorarioPorDiaDto.builder()
      .diaSemana(entry.getKey())
      .bloques(entry.getValue())
      .build())
    .collect(Collectors.toList());
 }

 private Integer getInteger(Map<String, Object> map, String key) {
  Object value = map.get(key);
  if (value == null) {
   return null;
  }
  if (value instanceof Integer) {
   return (Integer) value;
  }
  if (value instanceof Long) {
   return ((Long) value).intValue();
  }
  if (value instanceof Number) {
   return ((Number) value).intValue();
  }
  return null;
 }
}
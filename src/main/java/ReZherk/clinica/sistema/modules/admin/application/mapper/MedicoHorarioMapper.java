package ReZherk.clinica.sistema.modules.admin.application.mapper;

import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.modules.admin.application.dto.response.HorarioPorDiaDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.MedicoConHorariosResponseDto;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MedicoHorarioMapper {

  public MedicoConHorariosResponseDto mapFromStoredProcedure(Map<String, Object> resultMap) {
    if (resultMap == null || resultMap.isEmpty()) {
      return null;
    }

    // Parsear los horarios del formato concatenado
    String horariosStr = (String) resultMap.get("horarios");
    log.info("String de horarios recibido: [{}]", horariosStr);

    List<HorarioPorDiaDto> horariosPorDia = parseHorarios(horariosStr);

    log.info("Horarios parseados: {} bloques", (horariosPorDia != null ? horariosPorDia.size() : 0));
    if (horariosPorDia != null && !horariosPorDia.isEmpty()) {
      horariosPorDia.forEach(h -> {
        log.info("  Día: {}, Bloques: {}", h.getDiaSemana(), h.getBloques().size());
      });
    }

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
    log.info("Parseando horarios. Input: [{}]", horariosStr);

    if (horariosStr == null || horariosStr.trim().isEmpty()) {
      log.warn("String de horarios vacío o null");
      return Collections.emptyList();
    }

    // Formato: "10:Lunes:08:00-12:00|11:Martes:08:00-12:00"
    Map<String, List<HorarioPorDiaDto.BloqueHorarioDto>> horariosPorDiaMap = new LinkedHashMap<>();

    String[] bloques = horariosStr.split("\\|");
    log.info("Total de bloques separados por |: {}", bloques.length);

    for (String bloque : bloques) {
      log.info("Procesando bloque: [{}]", bloque);

      // Dividir en: id_horario, día, horario (HH:MM-HH:MM)
      String[] partes = bloque.split(":", 3);

      if (partes.length == 3) {
        Integer idHorario = null;
        try {
          idHorario = Integer.parseInt(partes[0].trim());
        } catch (NumberFormatException e) {
          log.warn("  ID de horario inválido: {}", partes[0]);
          continue;
        }

        String dia = partes[1].trim();
        String rangoHorario = partes[2]; // "08:00-12:00"

        log.info("  ID_Horario: [{}], Día: [{}], Rango: [{}]", idHorario, dia, rangoHorario);

        // Ahora dividir el rango por "-"
        String[] horas = rangoHorario.split("-");

        if (horas.length == 2) {
          String horaInicio = horas[0].trim(); // "08:00"
          String horaFin = horas[1].trim(); // "12:00"

          log.info("    Hora inicio: [{}], Hora fin: [{}]", horaInicio, horaFin);

          HorarioPorDiaDto.BloqueHorarioDto bloqueDto = HorarioPorDiaDto.BloqueHorarioDto.builder()
              .id(idHorario)
              .horaInicio(horaInicio)
              .horaFin(horaFin)
              .build();

          horariosPorDiaMap.computeIfAbsent(dia, k -> new ArrayList<>()).add(bloqueDto);
        } else {
          log.warn("  Formato de horas inválido: {}", rangoHorario);
        }
      } else {
        log.warn("  Formato de bloque inválido (esperaba 3 partes): {}", bloque);
      }
    }

    List<HorarioPorDiaDto> resultado = horariosPorDiaMap.entrySet().stream()
        .map(entry -> HorarioPorDiaDto.builder()
            .diaSemana(entry.getKey())
            .bloques(entry.getValue())
            .build())
        .collect(Collectors.toList());

    log.info("Total de días con horarios: {}", resultado.size());
    return resultado;
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
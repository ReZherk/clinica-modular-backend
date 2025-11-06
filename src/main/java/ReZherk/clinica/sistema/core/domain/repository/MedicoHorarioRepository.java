package ReZherk.clinica.sistema.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ReZherk.clinica.sistema.core.domain.entity.MedicoHorario;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface MedicoHorarioRepository extends JpaRepository<MedicoHorario, Integer> {

  @Query("SELECT mh FROM MedicoHorario mh " +
      "JOIN FETCH mh.medico m " +
      "JOIN FETCH mh.horario h " +
      "WHERE m.id = :idMedico AND mh.estado = 'ACTIVO'")
  List<MedicoHorario> findByMedicoIdAndEstadoActivo(@Param("idMedico") Integer idMedico);

  @Query("SELECT mh FROM MedicoHorario mh " +
      "WHERE mh.medico.id = :idMedico " +
      "AND mh.horario.idHorario = :idHorario " +
      "AND mh.estado = 'ACTIVO'")
  Optional<MedicoHorario> findByMedicoAndHorarioAndEstadoActivo(
      @Param("idMedico") Integer idMedico,
      @Param("idHorario") Integer idHorario);

  @Modifying
  @Query("UPDATE MedicoHorario mh SET mh.estado = 'INACTIVO' WHERE mh.medico.id = :idMedico")
  void deactivateAllByMedicoId(@Param("idMedico") Integer idMedico);

  @Modifying
  @Query("UPDATE MedicoHorario mh SET mh.estado = 'INACTIVO' WHERE mh.idMedicoHorario = :id")
  void deactivateById(@Param("id") Integer id);

  @Modifying
  @Query("DELETE FROM MedicoHorario mh WHERE mh.medico.id = :idMedico")
  void deleteAllByMedicoId(@Param("idMedico") Integer idMedico);

  @Query("SELECT COUNT(mh) > 0 FROM MedicoHorario mh " +
      "WHERE mh.medico.id = :idMedico " +
      "AND mh.horario.idHorario = :idHorario " +
      "AND mh.estado = 'ACTIVO'")
  boolean existsByMedicoAndHorarioActivo(
      @Param("idMedico") Integer idMedico,
      @Param("idHorario") Integer idHorario);

  // Llamada al procedimiento almacenado para médicos CON horarios
  @Query(value = "CALL sp_buscar_medicos_con_horarios(:nombre, :dni, :cmp, :especialidad, :page, :size)", nativeQuery = true)
  List<Map<String, Object>> buscarMedicosConHorarios(
      @Param("nombre") String nombre,
      @Param("dni") String dni,
      @Param("cmp") String cmp,
      @Param("especialidad") String especialidad,
      @Param("page") Integer page,
      @Param("size") Integer size);

  // Llamada al procedimiento almacenado para médicos SIN horarios
  @Query(value = "CALL sp_buscar_medicos_sin_horarios(:nombre, :dni, :cmp, :especialidad, :page, :size)", nativeQuery = true)
  List<Map<String, Object>> buscarMedicosSinHorarios(
      @Param("nombre") String nombre,
      @Param("dni") String dni,
      @Param("cmp") String cmp,
      @Param("especialidad") String especialidad,
      @Param("page") Integer page,
      @Param("size") Integer size);
}

package ReZherk.clinica.sistema.core.domain.repository;

import ReZherk.clinica.sistema.core.domain.entity.Usuario;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Buscar usuario por email
    Optional<Usuario> findByEmail(String email);

    // Verificar si existe email
    boolean existsByEmail(String email);

    // Buscar usuario con sus roles/perfiles
    @Query("SELECT u FROM Usuario u JOIN FETCH u.perfiles WHERE u.email = :email")
    Optional<Usuario> findByEmailWithRoles(@Param("email") String email);

    // Buscar usuario con roles + permisos
    @Query("""
                SELECT DISTINCT u
                FROM Usuario u
                LEFT JOIN FETCH u.perfiles r
                LEFT JOIN FETCH r.permisos p
                WHERE u.email = :email
            """)
    Optional<Usuario> findByEmailWithRolesAndPermissions(@Param("email") String email);

    // Listar usuarios activos
    @Query("SELECT u FROM Usuario u WHERE u.estadoRegistro = true")
    List<Usuario> findAllActive();

    // Buscar por NumeroDocumento
    Optional<Usuario> findByNumeroDocumento(String numeroDocumento);

    // Verificar si existe NumeroDocumento
    boolean existsByNumeroDocumento(String numeroDocumento);

    @Query("""
                SELECT DISTINCT u FROM Usuario u
                WHERE u.estadoRegistro = :estado
                AND (
                    :rol IS NULL OR :rol = '' OR
                    EXISTS (
                        SELECT 1 FROM RolPerfil r
                        WHERE r MEMBER OF u.perfiles
                        AND UPPER(r.nombre) = UPPER(:rol)
                    )
                )
                AND (
                    :search IS NULL OR :search = '' OR
                    (
                        CASE
                            WHEN :searchType = 'documento' THEN u.numeroDocumento LIKE CONCAT('%', :search, '%')
                            ELSE (
                                LOWER(u.nombres) LIKE LOWER(CONCAT('%', :search, '%')) OR
                                LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :search, '%'))
                            )
                        END
                    ) = true
                )
            """)
    Page<Usuario> findUserByEstadoAndSearch(
            @Param("estado") Boolean estado,
            @Param("rol") String rol,
            @Param("search") String search,
            @Param("searchType") String searchType,
            Pageable pageable);

    //////////
    @Query("""
                SELECT DISTINCT u FROM Usuario u
                LEFT JOIN FETCH u.perfiles rp
                WHERE u.estadoRegistro = :estado
                AND EXISTS (
                    SELECT 1 FROM RolPerfil r
                    WHERE r MEMBER OF u.perfiles
                    AND r.estadoRegistro = true
                )
                AND (
                    :rol IS NULL OR :rol = '' OR
                    EXISTS (
                        SELECT 1 FROM RolPerfil r
                        WHERE r MEMBER OF u.perfiles
                        AND UPPER(r.nombre) = UPPER(:rol)
                        AND r.estadoRegistro = true
                    )
                )
                AND (
                    :search IS NULL OR :search = '' OR
                    (
                        CASE
                            WHEN :searchType = 'documento' THEN u.numeroDocumento LIKE CONCAT('%', :search, '%')
                            ELSE (
                                LOWER(u.nombres) LIKE LOWER(CONCAT('%', :search, '%')) OR
                                LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :search, '%'))
                            )
                        END
                    ) = true
                )
                AND NOT EXISTS (
                    SELECT 1 FROM RolPerfil rp2
                    WHERE rp2 MEMBER OF u.perfiles
                    AND UPPER(rp2.nombre) IN ('MEDICO', 'PACIENTE', 'SUPERADMIN', 'ADMINISTRADOR')
                )
            """)
    Page<Usuario> findAdministrativeUsers(
            @Param("estado") Boolean estado,
            @Param("rol") String rol,
            @Param("search") String search,
            @Param("searchType") String searchType,
            Pageable pageable);

    /////////////
    @Query("""
            SELECT DISTINCT u FROM Usuario u
            LEFT JOIN FETCH u.perfiles p
            LEFT JOIN FETCH u.tipoDocumento
            LEFT JOIN MedicoDetalle md ON md.usuario = u
            LEFT JOIN md.especialidad e
            WHERE u.estadoRegistro = :estado
            AND EXISTS (
                SELECT 1 FROM RolPerfil r
                WHERE r MEMBER OF u.perfiles
                AND UPPER(r.nombre) = UPPER(:rol)
            )
            AND (
                :especialidad IS NULL OR :especialidad = '' OR
                UPPER(e.nombreEspecialidad) = UPPER(:especialidad)
            )
            AND (
                :search IS NULL OR :search = '' OR
                (
                    CASE
                        WHEN :searchType = 'documento' THEN u.numeroDocumento LIKE CONCAT('%', :search, '%')
                        WHEN :searchType = 'cmp' THEN md.cmp LIKE CONCAT('%', :search, '%')
                        ELSE (
                            LOWER(u.nombres) LIKE LOWER(CONCAT('%', :search, '%')) OR
                            LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :search, '%'))
                        )
                    END
                ) = true
            )
            ORDER BY u.id
            """)
    List<Usuario> findUserByEstadoAndSearchWithProfiles(
            @Param("estado") Boolean estado,
            @Param("rol") String rol,
            @Param("especialidad") String especialidad,
            @Param("search") String search,
            @Param("searchType") String searchType);

    @Query("""
            SELECT DISTINCT u
            FROM Usuario u
            LEFT JOIN FETCH u.perfiles r
            LEFT JOIN FETCH r.permisos p
            WHERE u.numeroDocumento = :numeroDocumento
            """)
    Optional<Usuario> findByNumeroDocumentoWithRolesAndPermissions(@Param("numeroDocumento") String numeroDocumento);

    //////////////
    @Query("""
                SELECT DISTINCT u FROM Usuario u
                LEFT JOIN FETCH u.perfiles r
                LEFT JOIN FETCH r.permisos
                WHERE u.id = :id
                AND EXISTS (
                    SELECT 1 FROM RolPerfil rol
                    WHERE rol MEMBER OF u.perfiles
                    AND rol.estadoRegistro = true
                )
            """)
    Optional<Usuario> findByIdWithActiveRoles(@Param("id") Integer id);
}

package ReZherk.clinica.sistema.modules.admin.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicoHorarioPaginatedResponseDto {

 private List<MedicoConHorariosResponseDto> content;
 private Integer currentPage;
 private Integer pageSize;
 private Long totalElements;
 private Integer totalPages;
 private Boolean hasNext;
 private Boolean hasPrevious;
}

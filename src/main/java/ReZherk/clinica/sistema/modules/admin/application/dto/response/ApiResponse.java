package ReZherk.clinica.sistema.modules.admin.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
 private boolean successs;
 private String message;
 private T data;
}

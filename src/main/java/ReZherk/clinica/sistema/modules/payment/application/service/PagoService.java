package ReZherk.clinica.sistema.modules.payment.application.service;

import org.springframework.data.domain.Page;

import ReZherk.clinica.sistema.modules.payment.application.dto.request.PagoSeguroRequestDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.request.PagoTarjetaRequestDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.request.PagoYapeRequestDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.response.PagoResponseDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.response.ResumenPagoDto;

public interface PagoService {

 ResumenPagoDto obtenerResumenPago(Integer idCita);

 PagoResponseDto procesarPagoConTarjeta(PagoTarjetaRequestDto request);

 PagoResponseDto procesarPagoConYape(PagoYapeRequestDto request);

 PagoResponseDto procesarPagoConSeguro(PagoSeguroRequestDto request);

 PagoResponseDto obtenerDetallePago(Integer idPago);

 PagoResponseDto obtenerPagoPorCita(Integer idCita);

 Page<PagoResponseDto> obtenerHistorialPagos(Integer idPaciente, Integer page, Integer size);
}
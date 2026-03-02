package edu.rutmiit.demo.stoyakapicontract.endpoints;

import edu.rutmiit.demo.stoyakapicontract.dto.OrderRequest;
import edu.rutmiit.demo.stoyakapicontract.dto.OrderResponse;
import edu.rutmiit.demo.stoyakapicontract.dto.StatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Orders", description = "API для управления заявками на очередь")
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))
        })
})
public interface OrderApi {

    @Operation(summary = "Создать новую заявку")
    @ApiResponse(responseCode = "201", description = "Заявка успешно создана")
    @PostMapping(value = "/api/orders", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    OrderResponse createOrder(@Valid @RequestBody OrderRequest request);

    @Operation(summary = "Получить заявку по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заявка найдена"),
            @ApiResponse(responseCode = "404", description = "Заявка не найдена", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))
            })
    })
    @GetMapping("/api/orders/{id}")
    OrderResponse getOrderById(@ PathVariable Long id);

    @Operation(summary = "Отменить заявку")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заявка отменена"),
            @ApiResponse(responseCode = "404", description = "Заявка не найдена")
    })
    @PutMapping("/api/orders/{id}/cancel")
    StatusResponse cancelOrder(@PathVariable Long id);
}
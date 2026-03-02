package edu.rutmiit.demo.stoyakapicontract.endpoints;

import edu.rutmiit.demo.stoyakapicontract.dto.QueueRequest;
import edu.rutmiit.demo.stoyakapicontract.dto.QueueResponse;
import edu.rutmiit.demo.stoyakapicontract.dto.StatusResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "Queues", description = "API для управления очередями")
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Некорректный запрос / Ошибка валидации", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))
        })
})
public interface QueueApi {

    @Operation(summary = "Создать новое место для очереди")
    @ApiResponse(responseCode = "201", description = "Место успешно создано")
    @PostMapping(value = "/api/queues", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    QueueResponse createQueue(@Valid @RequestBody QueueRequest queueRequest);

    @Operation(summary = "Получить информацию об очереди по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Очередь найдена"),
            @ApiResponse(responseCode = "404", description = "Очередь не найдена", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))
            })
    })
    @GetMapping("/api/queues/{id}")
    QueueResponse getQueueById(@PathVariable("id") Long id);

    @Operation(summary = "Получить список всех доступных очередей")
    @ApiResponse(responseCode = "200", description = "Список очередей получен")
    @GetMapping("/api/queues")
    List<QueueResponse> getAllQueues();

    @Operation(summary = "Удалить очередь")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Очередь успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Очередь не найдена")
    })
    @DeleteMapping("/api/queues/{id}")
    StatusResponse deleteQueue(@PathVariable("id") Long id);
}
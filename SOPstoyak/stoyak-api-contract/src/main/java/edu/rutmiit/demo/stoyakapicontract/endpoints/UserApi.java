package edu.rutmiit.demo.stoyakapicontract.endpoints;

import edu.rutmiit.demo.stoyakapicontract.dto.StatusResponse;
import edu.rutmiit.demo.stoyakapicontract.dto.UserRequest;
import edu.rutmiit.demo.stoyakapicontract.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;


@Tag(name = "Users", description = "API для управления пользователями")
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Некорректный запрос / Ошибка валидации", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))
        })
})
public interface UserApi {

    @Operation(summary = "Зарегистрировать нового пользователя")
    @ApiResponse(responseCode = "201", description = "Пользователь успешно создан")
    @PostMapping(value = "/api/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    UserResponse registerUser(@Valid @RequestBody UserRequest userRequest);

    @Operation(summary = "Получить информацию о пользователе по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))
            })
    })
    @GetMapping("/api/users/{id}")
    UserResponse getUserById(@PathVariable("id") Long id);

    @Operation(summary = "Обновить информацию о пользователе")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PutMapping(value = "/api/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    UserResponse updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserRequest userRequest);

    @Operation(summary = "Удалить пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь успешно удален"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/api/users/{id}")
    StatusResponse deleteUser(@PathVariable("id") Long id);
}
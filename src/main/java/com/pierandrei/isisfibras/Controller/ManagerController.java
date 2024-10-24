package com.pierandrei.isisfibras.Controller;

import com.pierandrei.isisfibras.Dto.LogistcsAndEmployeeDto.ProductCreateDto;
import com.pierandrei.isisfibras.Dto.LogistcsAndEmployeeDto.ProductCreateResponseDto;
import com.pierandrei.isisfibras.Dto.LogistcsAndEmployeeDto.ProductUpdateDto;
import com.pierandrei.isisfibras.Exception.LogistcsExceptions.ProductNotAvailableException;
import com.pierandrei.isisfibras.Exception.UserNotUnauthorizedException;
import com.pierandrei.isisfibras.Model.UserModels.UserModel;
import com.pierandrei.isisfibras.Service.EmployeeServices.LogisticsManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/gerente-logistico")
public class ManagerController {
    private final LogisticsManagerService logisticsManagerService;


    // Criação do produto
    @PostMapping("/product/create")
    public ResponseEntity<ProductCreateResponseDto> productCreateController(
            @AuthenticationPrincipal UserModel userModel,
            @RequestBody @Valid ProductCreateDto productCreateDto,
            @RequestParam String accessCode) {
        try {
            // Chama o serviço para criar o produto
            ProductCreateResponseDto response = this.logisticsManagerService.productCreate(userModel, productCreateDto, accessCode);
            // Retorna uma resposta HTTP 201 (Created) em caso de sucesso, junto com os dados do produto criado
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UserNotUnauthorizedException e) {
            // Retorna uma resposta HTTP 403 (Forbidden) se o usuário não estiver autorizado
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (Exception e) {
            // Retorna uma resposta HTTP 500 (Internal Server Error) em caso de erro inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @PutMapping("/product/update")
    public ResponseEntity<ProductCreateResponseDto> productUpdateController(
            @AuthenticationPrincipal UserModel userModel,
            @RequestBody @Valid ProductUpdateDto productUpdateDto,
            @RequestParam String accessCode) {
        try {
            ProductCreateResponseDto response = logisticsManagerService.productUpdate(userModel, productUpdateDto, accessCode);
            return ResponseEntity.ok(response);
        } catch (UserNotUnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ProductCreateResponseDto(null, LocalDateTime.now(), e.getMessage()));
        } catch (ProductNotAvailableException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ProductCreateResponseDto(null, LocalDateTime.now(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ProductCreateResponseDto(null, LocalDateTime.now(), "Erro inesperado: " + e.getMessage()));
        }
    }


    @DeleteMapping("product/delete/{sku}")
    public ResponseEntity<String> productDeleteController(
            @AuthenticationPrincipal UserModel userModel,
            @PathVariable String sku,
            @RequestParam String accessCode) {
        try {
            String message = logisticsManagerService.productDelete(userModel, sku, accessCode);
            return ResponseEntity.ok(message);
        } catch (UserNotUnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (ProductNotAvailableException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
        }
    }


}

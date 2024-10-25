package com.pierandrei.isisfibras.Controller;

import com.pierandrei.isisfibras.Dto.LogistcsAndEmployeeDto.ProductCreateDto;
import com.pierandrei.isisfibras.Dto.LogistcsAndEmployeeDto.ProductCreateResponseDto;
import com.pierandrei.isisfibras.Dto.LogistcsAndEmployeeDto.ProductDeleteResponse;
import com.pierandrei.isisfibras.Dto.LogistcsAndEmployeeDto.ProductUpdateDto;
import com.pierandrei.isisfibras.Exception.LogistcsExceptions.ProductNotAvailableException;
import com.pierandrei.isisfibras.Model.UserModels.UserModel;
import com.pierandrei.isisfibras.Service.EmployeeServices.LogisticsManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam String accessCode) throws Exception {
        // Chama o serviço para criar o produto
        ProductCreateResponseDto response = this.logisticsManagerService.productCreate(userModel, productCreateDto, accessCode);
        // Retorna uma resposta HTTP 201 (Created) em caso de sucesso, junto com os dados do produto criado
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // Atualização do produto
    @PutMapping("/product/update")
    public ResponseEntity<ProductCreateResponseDto> productUpdateController(
            @AuthenticationPrincipal UserModel userModel,
            @RequestBody @Valid ProductUpdateDto productUpdateDto,
            @RequestParam String accessCode) throws ProductNotAvailableException {
        // Chama o serviço para atualizar o produto
        ProductCreateResponseDto response = logisticsManagerService.productUpdate(userModel, productUpdateDto, accessCode);
        // Retorna uma resposta HTTP 200 (OK) com os dados do produto atualizado
        return ResponseEntity.ok(response);
    }


    // Remoção do produto
    @DeleteMapping("product/delete/{sku}")
    public ResponseEntity<ProductDeleteResponse> productDeleteController(
            @AuthenticationPrincipal UserModel userModel,
            @PathVariable String sku,
            @RequestParam String accessCode) throws ProductNotAvailableException {
        // Chama o serviço para remover o produto
        ProductDeleteResponse productDeleteResponse = logisticsManagerService.productDelete(userModel, sku, accessCode);
        // Retorna uma resposta HTTP 200 (OK) com a mensagem de sucesso
        return ResponseEntity.ok(productDeleteResponse);
    }



}
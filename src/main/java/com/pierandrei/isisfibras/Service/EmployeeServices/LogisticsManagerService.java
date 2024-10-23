package com.pierandrei.isisfibras.Service.EmployeeServices;

import com.pierandrei.isisfibras.Dto.LogistcsAndEmployeeDto.ProductCreateDto;
import com.pierandrei.isisfibras.Dto.LogistcsAndEmployeeDto.ProductCreateResponseDto;
import com.pierandrei.isisfibras.Enuns.RolesUsers;
import com.pierandrei.isisfibras.Exception.UserNotUnauthorizedException;
import com.pierandrei.isisfibras.Model.LogisticModels.ProductsModel;
import com.pierandrei.isisfibras.Model.UserModels.UserModel;
import com.pierandrei.isisfibras.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class LogisticsManagerService {
    private final ProductRepository productRepository;

    public String generateSku(){
        Random random = new Random();
        StringBuilder skuGeneration = new StringBuilder();

        do {
            skuGeneration.setLength(0);
            for (int i = 0; i < 10; i++){
                skuGeneration.append(String.valueOf(random.nextInt(10)));
            }
            skuGeneration.append("sku");
        }while(!this.productRepository.existsBySku(skuGeneration.toString()));
        return skuGeneration.toString();

    }



    public ProductCreateResponseDto productCreate(UserModel userModel, ProductCreateDto productCreateDto){
        if (userModel.getRolesUsers() !=  RolesUsers.GERENTE_LOGISTICO){
            throw new UserNotUnauthorizedException("Você não está autorizado para adicioar um produto!");
        }

        ProductsModel productsModel = new ProductsModel();
        productsModel.setSku(generateSku());
        productsModel.setName(productCreateDto.name());
        productsModel.setDescription(productCreateDto.description());
        productsModel.setImagesUrls(productCreateDto.imageUrlsProduct());
        productsModel.setImageUrlPrincipal(productCreateDto.imageUrlPrincipal());
        productsModel.setQuantity(productCreateDto.quantity());
        productsModel.setPrice(productCreateDto.price());
        productsModel.setCategoriesEnum(productCreateDto.categoriesEnum());
        productsModel.setShippingWeight(productCreateDto.weightProduct());
        productsModel.setCreatedAt(LocalDateTime.now());
        this.productRepository.save(productsModel);
        return new ProductCreateResponseDto(productsModel.getSku(),LocalDateTime.now(),"Produto Cadastrado com Sucesso!");

    }
}

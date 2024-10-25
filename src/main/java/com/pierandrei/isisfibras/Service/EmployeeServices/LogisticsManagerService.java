package com.pierandrei.isisfibras.Service.EmployeeServices;

import com.pierandrei.isisfibras.Dto.LogistcsAndEmployeeDto.ProductCreateDto;
import com.pierandrei.isisfibras.Dto.LogistcsAndEmployeeDto.ProductCreateResponseDto;
import com.pierandrei.isisfibras.Dto.LogistcsAndEmployeeDto.ProductUpdateDto;
import com.pierandrei.isisfibras.Enuns.RolesUsers;
import com.pierandrei.isisfibras.Exception.LogistcsExceptions.ProductNotAvailableException;
import com.pierandrei.isisfibras.Exception.UserNotUnauthorizedException;
import com.pierandrei.isisfibras.Model.LogisticModels.ProductsModel;
import com.pierandrei.isisfibras.Model.UserModels.UserModel;
import com.pierandrei.isisfibras.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        }while(this.productRepository.existsBySku(skuGeneration.toString()));
        return skuGeneration.toString();

    }


    // Cadastro de Produto
    public ProductCreateResponseDto productCreate(UserModel userModel, ProductCreateDto productCreateDto, String accessCode) throws Exception {
        // Verifica se o usuário tem a role adequada
        if (userModel.getRolesUsers() != RolesUsers.GERENTE_LOGISTICO || !userModel.getAccessCode().equals(accessCode)) {
            throw new UserNotUnauthorizedException("Você não está autorizado para adicionar um produto!");
        }

        try {
            // Criação e configuração do produto
            ProductsModel productsModel = new ProductsModel();
            productsModel.setSku(generateSku());
            productsModel.setName(productCreateDto.name());
            productsModel.setDescription(productCreateDto.description());

            // Formata a lista de URLs de imagens
            List<String> imageUrlFormatted = new ArrayList<>();
            for (String url : productCreateDto.imageUrlsProduct()) {
                String formattedUrl = url.replace("https://www.dropbox.com/", "https://dl.dropboxusercontent.com/");
                imageUrlFormatted.add(formattedUrl);
            }

            productsModel.setImagesUrls(imageUrlFormatted);
            productsModel.setImageUrlPrincipal(imageUrlFormatted.get(0));  // Define a primeira imagem como principal
            productsModel.setQuantity(productCreateDto.quantity());
            productsModel.setPrice(productCreateDto.price());
            productsModel.setCategoriesEnum(productCreateDto.categoriesEnum());
            productsModel.setShippingWeight(productCreateDto.weightProduct());

            // Define se o produto está em estoque
            productsModel.setInStock(productCreateDto.quantity() > 1);

            productsModel.setCreatedAt(LocalDateTime.now());

            // Salva o produto no banco de dados
            this.productRepository.save(productsModel);

            // Retorna a resposta do sucesso
            return new ProductCreateResponseDto(productsModel.getSku(), LocalDateTime.now(), "Produto Cadastrado com Sucesso!");
        } catch (Exception e) {
            // Lança uma nova exceção com uma mensagem mais clara sem expor a exceção original
            throw new Exception("Erro ao criar produto: " + e.getMessage());
        }
    }

    // Atualizar o produto
    public ProductCreateResponseDto productUpdate(UserModel userModel, ProductUpdateDto productUpdateDto, String accessCode) throws ProductNotAvailableException {
        // Verifica se o usuário tem a role adequada
        if (userModel.getRolesUsers() != RolesUsers.GERENTE_LOGISTICO && !userModel.getAccessCode().equals(accessCode)) {
            throw new UserNotUnauthorizedException("Você não está autorizado para atualizar um produto!");
        }

        Optional<ProductsModel> model = this.productRepository.findBySku(productUpdateDto.sku());
        if (model.isEmpty()) {
            throw new ProductNotAvailableException("Produto indisponível para atualização!");
        }

        // Atualiza os campos existentes se eles não estiverem vazios ou com valor zero
        if (!productUpdateDto.name().isEmpty()) model.get().setName(productUpdateDto.name());
        if (!productUpdateDto.description().isEmpty()) model.get().setDescription(productUpdateDto.description());
        if (productUpdateDto.quantity() != 0) model.get().setQuantity(productUpdateDto.quantity());
        if (productUpdateDto.price() != 0) model.get().setPrice(productUpdateDto.price());
        if (!productUpdateDto.categoriesEnum().toString().isEmpty()) model.get().setCategoriesEnum(productUpdateDto.categoriesEnum());
        if (productUpdateDto.weightProduct() != 0) model.get().setShippingWeight(productUpdateDto.weightProduct());

        // Atualiza as URLs de imagens mantendo as já existentes e removendo as que foram retiradas
        List<String> existingUrls = model.get().getImagesUrls();
        List<String> updatedUrls = productUpdateDto.imageUrlsProduct(); // URLs que o usuário deseja manter

        // Remove as URLs que não estão na lista de URLs atualizadas
        existingUrls.removeIf(url -> !updatedUrls.contains(url));

        // Atualiza a lista de URLs de imagens no produto
        model.get().setImagesUrls(existingUrls);

        // Atualiza a imagem principal, se uma nova foi fornecida
        if (!updatedUrls.isEmpty() && updatedUrls.get(0) != null) {
            model.get().setImageUrlPrincipal(updatedUrls.get(0));
        }

        // Salva as alterações no banco de dados
        this.productRepository.save(model.get());

        // Retorna a resposta de atualização do produto
        return new ProductCreateResponseDto(model.get().getSku(), LocalDateTime.now(), "Produto atualizado com sucesso!");
    }

    // Remover o produto
    public String productDelete(UserModel userModel, String sku, String accessCode) throws ProductNotAvailableException {
        // Verifica se o usuário tem a role adequada
        if (userModel.getRolesUsers() != RolesUsers.GERENTE_LOGISTICO && !userModel.getAccessCode().equals(accessCode)) {
            throw new UserNotUnauthorizedException("Você não está autorizado para remover um produto!");
        }

        Optional<ProductsModel> model = this.productRepository.findBySku(sku);
        if (model.isEmpty()) {
            throw new ProductNotAvailableException("Produto indisponível para remoção!");
        }

        this.productRepository.delete(model.get());
        return "Produto SKU: [ "+sku+" ] removido!";
    }




}

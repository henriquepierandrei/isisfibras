package com.pierandrei.isisfibras.Service.UserServices;

import com.pierandrei.isisfibras.Exception.AuthExceptions.PhoneExistsException;
import com.pierandrei.isisfibras.Exception.LogistcsExceptions.ProductNotAvailableException;
import com.pierandrei.isisfibras.Exception.UserNotUnauthorizedException;
import com.pierandrei.isisfibras.Model.LogisticModels.ProductOrder;
import com.pierandrei.isisfibras.Model.LogisticModels.ProductsModel;
import com.pierandrei.isisfibras.Model.UserModels.CartModel;
import com.pierandrei.isisfibras.Model.UserModels.UserModel;
import com.pierandrei.isisfibras.Repository.CartRepository;
import com.pierandrei.isisfibras.Repository.ProductRepository;
import com.pierandrei.isisfibras.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ProductRepository productRepository;
    private CartRepository cartRepository;
    private UserRepository userRepository;

    // Adicionar produto no carrinho de compras
    public String addProductInCart(UserModel userModel, String skuProduct, int quantity) throws ProductNotAvailableException, UserNotUnauthorizedException {
        if (userModel == null) {
            throw new UserNotUnauthorizedException("Você não possui nenhuma conta logada!");
        }

        // Verificar se o produto está disponível
        Optional<ProductsModel> productsModelOptional = this.productRepository.findBySku(skuProduct);
        if (productsModelOptional.isEmpty()) {
            throw new ProductNotAvailableException("Produto indisponível no momento!");
        }

        // Buscar o carrinho do usuário
        CartModel cartModel = this.cartRepository.findByIdUser(userModel.getId());


        // Criar um novo pedido com SKU, nome e quantidade
        ProductOrder productOrder = new ProductOrder();
        productOrder.setQuantity(quantity);
        productOrder.setName(productsModelOptional.get().getName()); // Usar o nome correto do produto
        productOrder.setPrice(productsModelOptional.get().getPrice());

        // Adicionar o pedido à lista de produtos do carrinho
        List<ProductOrder> productOrders = cartModel.getProductOrders();
        productOrders.add(productOrder);
        cartModel.setProductOrders(productOrders);

        // Atualizar o preço total do carrinho
        double totalPrice = cartModel.getTotalPrice();
        totalPrice += productsModelOptional.get().getPrice() * quantity; // Multiplicar o preço pela quantidade
        cartModel.setTotalPrice(totalPrice);

        // Salvar o carrinho atualizado no repositório
        this.cartRepository.save(cartModel);
        return "Produto adicionado ao carrinho!";
    }


//    // Adicionar telefone à conta
//    public String addPhoneForUser(String phone){
//        if (this.userRepository.existsByPhone(phone)){
//            throw new PhoneExistsException("Telefone já existente!");
//        }
//    }


}

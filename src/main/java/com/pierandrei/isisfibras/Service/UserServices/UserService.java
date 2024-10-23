package com.pierandrei.isisfibras.Service.UserServices;

import com.pierandrei.isisfibras.Exception.AuthExceptions.CodeNotExistsException;
import com.pierandrei.isisfibras.Exception.AuthExceptions.PhoneExistsException;
import com.pierandrei.isisfibras.Exception.AuthExceptions.PhoneNotFoundException;
import com.pierandrei.isisfibras.Exception.AuthExceptions.TimeLimitOfGenerationException;
import com.pierandrei.isisfibras.Exception.LogistcsExceptions.ProductNotAvailableException;
import com.pierandrei.isisfibras.Exception.UserNotUnauthorizedException;
import com.pierandrei.isisfibras.Model.LogisticModels.ProductOrder;
import com.pierandrei.isisfibras.Model.LogisticModels.ProductsModel;
import com.pierandrei.isisfibras.Model.UserModels.CartModel;
import com.pierandrei.isisfibras.Model.UserModels.UserModel;
import com.pierandrei.isisfibras.Repository.CartRepository;
import com.pierandrei.isisfibras.Repository.ProductRepository;
import com.pierandrei.isisfibras.Repository.UserRepository;
import com.pierandrei.isisfibras.Service.MessageSenderService.TwilioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final TwilioService twilioService;

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


    // Adicionar telefone à conta
    public String addPhoneForUser(String code, UUID idUser) throws UserPrincipalNotFoundException, CodeNotExistsException, PhoneExistsException {

        Optional<UserModel> userModel = this.userRepository.findById(idUser);
        if (!userModel.isPresent()) throw new UserPrincipalNotFoundException("Não existe nenhum usuário!");


        if (this.userRepository.existsByPhone(userModel.get().getPossiblePhone())){
            throw new PhoneExistsException("Telefone já cadastrado");
        }

        if (userModel.get().getPossiblePhone() == null || userModel.get().getPossiblePhone().isEmpty()) {
            throw new PhoneExistsException("Número de telefone não pode ser nulo ou vazio.");
        }


        if (code.equals(userModel.get().getCodeVerification())){
            userModel.get().setPhone(userModel.get().getPossiblePhone());
            userModel.get().setPossiblePhone(null);
            this.userRepository.save(userModel.get());
            return "Telefone Cadastrado!";
        }
        throw new CodeNotExistsException("Código incorreto!");
    }

    // Gerador do código e envio por SMS
    public String generateCode(UUID idUser, String phone) throws TimeLimitOfGenerationException, PhoneNotFoundException {
        Random random = new Random();
        Optional<UserModel> userModel = this.userRepository.findById(idUser);

        // Verifica se o usuário foi encontrado
        if (!userModel.isPresent()) {
            return null; // ou lançar uma exceção personalizada se preferir
        }

        UserModel user = userModel.get(); // Obter o modelo de usuário
        LocalDateTime now = LocalDateTime.now();

        // Se codeGeneratedAt é nulo, inicializa e salva
        if (user.getCodeGeneratedAt() == null) {
            user.setCodeGeneratedAt(now);
            this.userRepository.save(user);
        } else {
            // Calcula a duração desde o último código gerado
            Duration duration = Duration.between(user.getCodeGeneratedAt(), now);

            // Lança exceção se o tempo de geração não for atendido
            if (duration.toMinutes() < 5) {
                throw new TimeLimitOfGenerationException("Espere 5 minutos para gerar o código novamente!");
            }
        }

        // Gera o código de verificação
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            stringBuilder.append(random.nextInt(10));
        }

        // Atualiza o modelo do usuário com o novo código e a data atual
        user.setCodeGeneratedAt(now); // Atualiza a data atual
        user.setCodeVerification(stringBuilder.toString());
        user.setPossiblePhone(phone);
        this.userRepository.save(user); // Salva as mudanças no banco de dados

        // Envia o código por SMS
        this.twilioService.sendMessage(phone, "Código de Verificação: " + stringBuilder.toString());
        return "Código enviado!";
    }




}

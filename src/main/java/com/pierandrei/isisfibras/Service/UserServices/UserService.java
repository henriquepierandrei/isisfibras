package com.pierandrei.isisfibras.Service.UserServices;

import com.pierandrei.isisfibras.Dto.Admin.CouponResponse;
import com.pierandrei.isisfibras.Exception.AuthExceptions.CodeNotExistsException;
import com.pierandrei.isisfibras.Exception.AuthExceptions.PhoneExistsException;
import com.pierandrei.isisfibras.Exception.AuthExceptions.PhoneNotFoundException;
import com.pierandrei.isisfibras.Exception.AuthExceptions.TimeLimitOfGenerationException;
import com.pierandrei.isisfibras.Exception.LogistcsExceptions.CouponException;
import com.pierandrei.isisfibras.Exception.LogistcsExceptions.CouponNotExistsException;
import com.pierandrei.isisfibras.Exception.LogistcsExceptions.ProductNotAvailableException;
import com.pierandrei.isisfibras.Exception.AuthExceptions.UserUnauthorizedException;
import com.pierandrei.isisfibras.Model.LogisticModels.CouponModel;
import com.pierandrei.isisfibras.Model.LogisticModels.ProductsModel;
import com.pierandrei.isisfibras.Model.UserModels.CartItems;
import com.pierandrei.isisfibras.Model.UserModels.CartModel;
import com.pierandrei.isisfibras.Model.UserModels.UserModel;
import com.pierandrei.isisfibras.Repository.*;
import com.pierandrei.isisfibras.Service.MessageSenderService.TwilioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final CartItemsRepository cartItemsRepository;
    private final TwilioService twilioService;

    // Adicionar produto no carrinho de compras
    public String addProductInCart(UserModel userModel, String skuProduct, int quantity) throws ProductNotAvailableException, UserUnauthorizedException {
        if (userModel == null) {
            throw new UserUnauthorizedException("Você não possui nenhuma conta logada!");
        }

        // Verificar se o produto está disponível
        Optional<ProductsModel> productsModelOptional = this.productRepository.findBySku(skuProduct);
        if (productsModelOptional.isEmpty()) {
            throw new ProductNotAvailableException("Produto indisponível no momento!");
        }

        // Buscar o carrinho do usuário
        CartModel cartModel = this.cartRepository.findByIdUser(userModel.getId());

        // Caso o carrinho ainda não exista, criar um novo para o usuário
        if (cartModel == null) {
            cartModel = new CartModel();
            cartModel.setIdUser(userModel.getId());
            cartModel.setTotalPrice(0.0);
            cartModel.setTotalItems(0);
            cartModel.setTotalDiscount(0.0);
            cartModel.setSubTotal(0.0);
        }

        // Criar um novo item de carrinho com SKU e quantidade
        CartItems cartItems = new CartItems();
        cartItems.setSku(skuProduct);
        cartItems.setQuantity(quantity);
        this.cartItemsRepository.save(cartItems);

        // Adicionar o item ao carrinho
        cartModel.getCartItems().add(cartItems);

        // Atualizar o preço total do carrinho
        double itemTotalPrice = productsModelOptional.get().getPrice() * quantity;
        cartModel.setTotalPrice(cartModel.getTotalPrice() + itemTotalPrice);

        // Atualizar o número total de itens no carrinho
        cartModel.setTotalItems(cartModel.getTotalItems() + quantity);

        // Salvar o carrinho atualizado no repositório
        this.cartRepository.save(cartModel);

        return "Produto adicionado ao carrinho!";
    }



    // Adicionar telefone à conta
    public String addPhoneForUser(String code, UUID idUser) throws UserPrincipalNotFoundException, CodeNotExistsException, PhoneExistsException {
        Optional<UserModel> userModelOpt = this.userRepository.findById(idUser);

        if (!userModelOpt.isPresent()) {
            throw new UserPrincipalNotFoundException("Não existe nenhum usuário!");
        }

        UserModel userModel = userModelOpt.get(); // Obter o modelo do usuário

        // Verifica se o telefone já está cadastrado
        if (this.userRepository.existsByPhone(userModel.getPossiblePhone())) {
            throw new PhoneExistsException("Telefone já cadastrado");
        }

        // Valida se o possível telefone é nulo ou vazio
        if (userModel.getPossiblePhone() == null || userModel.getPossiblePhone().isEmpty()) {
            throw new PhoneExistsException("Número de telefone não pode ser nulo ou vazio.");
        }

        // Verifica se o código fornecido está correto
        if (code.equals(userModel.getCodeVerification())) {
            userModel.setPhone("+"+userModel.getPossiblePhone()); // Atualiza o telefone
            userModel.setPossiblePhone(null);
            userModel.setCodeVerification(null);// Limpa o possível telefone
            this.userRepository.save(userModel); // Salva as alterações
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


    // Calcula o valor final com o cupom
    public CouponResponse calculateFinalValueWithCoupon(UUID userId, String couponCode, double orderTotal) {
        // Busca o cupom pelo código fornecido
        CouponModel coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new CouponNotExistsException("Cupom indisponível!"));

        // Valida se o cupom está ativo e se já expirou ou atingiu o limite
        validateCouponAvailability(coupon, userId);

        // Verifica se o valor do pedido atinge o mínimo necessário para aplicar o cupom
        if (orderTotal < coupon.getMinimumAmount()) {
            throw new CouponException("O valor do pedido é abaixo do esperado para esse Cupom!");
        }

        // Verifica se é frete grátis
        if (coupon.isFreeShipping()){
            return new CouponResponse(null, true);
        }

        // Calcula o desconto e o valor final
        double discountAmount = orderTotal * (coupon.getValuePerCentDiscount() / 100.0);
        if (discountAmount > coupon.getMaxDiscountAmount()) {
            throw new CouponException("O valor do desconto ultrapassa o limite determinado do cupom!");
        }
        double finalValue = orderTotal - discountAmount;

        // Formata o valor final com número fixo de casas decimais
        return new CouponResponse(formatFinalValue(finalValue), false);
    }

    // Valida o cupom
    private void validateCouponAvailability(CouponModel coupon, UUID userId) {
        if (!coupon.isCouponActive()) {
            throw new CouponException("Cupom desativado.");
        }

        // Verifica se o cupom é de uso único e já foi usado pelo usuário
        if (coupon.isSingleUse() && coupon.getIdUsersUsed().contains(userId)) {
            throw new UserUnauthorizedException("Este cupom já foi usado por este usuário!");
        }

        // Verifica se o cupom atingiu o limite de uso ou está expirado
        boolean hasUsageLimitExceeded = coupon.getIdUsersUsed().size() >= coupon.getUsageLimit();
        boolean hasExpired = coupon.getExpirationDate().isBefore(LocalDate.now());
        if (hasUsageLimitExceeded || hasExpired) {
            throw new CouponException("Cupom expirado ou limite de uso atingido!");
        }
    }
    private static final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
    private String formatFinalValue(double value) {
        return decimalFormat.format(value);
    }









}

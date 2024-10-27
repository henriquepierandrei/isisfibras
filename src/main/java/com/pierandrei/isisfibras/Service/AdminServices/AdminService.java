package com.pierandrei.isisfibras.Service.AdminServices;

import com.pierandrei.isisfibras.Dto.Admin.CouponCreateDto;
import com.pierandrei.isisfibras.Dto.Admin.CouponCreateResponse;
import com.pierandrei.isisfibras.Dto.Admin.PaginatedUsersResponse;
import com.pierandrei.isisfibras.Dto.Admin.UserResponse;
import com.pierandrei.isisfibras.Enuns.RolesUsers;
import com.pierandrei.isisfibras.Exception.AuthExceptions.UserNotFoundException;
import com.pierandrei.isisfibras.Exception.LogistcsExceptions.CouponExistsException;
import com.pierandrei.isisfibras.Exception.AuthExceptions.UserUnauthorizedException;
import com.pierandrei.isisfibras.Model.Historic.RoleHistoricChange;
import com.pierandrei.isisfibras.Model.LogisticModels.CouponModel;
import com.pierandrei.isisfibras.Model.UserModels.UserModel;
import com.pierandrei.isisfibras.Repository.CouponRepository;
import com.pierandrei.isisfibras.Repository.RoleHistoricChangeRepository;
import com.pierandrei.isisfibras.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final RoleHistoricChangeRepository roleHistoricChangeRepository;
    private final CouponRepository couponRepository;


    // Gerar código de acesso
    public String generateCode(UUID idOwner) {
        Optional<UserModel> userModelOptional = this.userRepository.findById(idOwner);
        if (userModelOptional.isEmpty()) {
            throw new BadCredentialsException("Usuário inexistente!");
        }

        String accessCode;
        do {
            accessCode = generateRandomCode(6);
        } while (this.userRepository.existsByAccessCode(accessCode));

        userModelOptional.get().setAccessCode(accessCode);
        this.userRepository.save(userModelOptional.get());
        return accessCode;
    }

    // Método auxiliar para gerar código aleatório
    private String generateRandomCode(int length) {
        Random random = new Random();
        StringBuilder codeCreation = new StringBuilder();
        for (int i = 0; i < length; i++) {
            codeCreation.append(random.nextInt(10));
        }
        return codeCreation.toString();
    }


    // Definir ROLE ao usuário
    public String changeRole(UUID idAdmin, UUID idUser, RolesUsers rolesUsers) {
        Optional<UserModel> userModelOptional = this.userRepository.findById(idUser);
        if (userModelOptional.isEmpty()) {
            throw new BadCredentialsException("Usuário inexistente!");
        }

        Optional<UserModel> adminModelOptional = this.userRepository.findById(idAdmin);
        if (adminModelOptional.isEmpty()) {
            throw new BadCredentialsException("Admin inexistente!");
        }

        // Verifica se o administrador tem a permissão
        if (adminModelOptional.get().getRolesUsers() == RolesUsers.ADMIN) {
            UserModel userModel = userModelOptional.get();
            RolesUsers oldRole = userModel.getRolesUsers();

            userModel.setRolesUsers(rolesUsers);
            this.userRepository.save(userModel);

            // Cria um histórico da mudança de papel
            RoleHistoricChange roleHistoricChange = new RoleHistoricChange();
            roleHistoricChange.setChangeAt(LocalDateTime.now());
            roleHistoricChange.setOldRolesUsers(oldRole);
            roleHistoricChange.setNewRolesUsers(rolesUsers);
            roleHistoricChange.setRoleUserChanged(idUser);
            roleHistoricChange.setIdAdminOwnerChange(idAdmin);
            this.roleHistoricChangeRepository.save(roleHistoricChange);
        } else {
            throw new UserUnauthorizedException("Você não tem permissão para alteração!");
        }

        return "Posição mudada para: " + rolesUsers;
    }



    // Deletar Usuário no Banco de Dados
    public String deleteUser(UUID idAdmin, UUID idUser) throws UserNotFoundException {
        // Verifica se o usuário a ser deletado existe
        Optional<UserModel> userModelOptional = this.userRepository.findById(idUser);
        if (userModelOptional.isEmpty()) {
            throw new UserNotFoundException("Usuário inexistente!");
        }

        // Verifica se o administrador existe
        Optional<UserModel> adminModelOptional = this.userRepository.findById(idAdmin);
        if (adminModelOptional.isEmpty()) {
            throw new UserNotFoundException("Admin inexistente!");
        }

        // Verifica se o administrador tem a permissão para deletar
        if (adminModelOptional.get().getRolesUsers() != RolesUsers.ADMIN) {
            throw new UserUnauthorizedException("Você não tem permissão para deletar usuários!");
        }

        // Realiza a deleção
        this.userRepository.delete(userModelOptional.get());

        return "Usuário: ID: [" + userModelOptional.get().getId() + "] removido!";
    }


    // Obter usuário através do ID
    public Optional<UserModel> getUser(UUID id) throws UserNotFoundException {
        return this.userRepository.findById(id);

    }


    // Obtém e retorna em formato de DTO o usuário através do ID
    public UserResponse getUserWithResponse(UUID id) throws UserNotFoundException {
        Optional<UserModel> userModelOptional = this.userRepository.findById(id);
        if (userModelOptional.isEmpty()) throw new UserNotFoundException("Usuário não existe!");

        UserModel userModel = userModelOptional.get();
        return new UserResponse(userModel.getId(),userModel.getEmail(), userModel.getPhone(), userModel.getRolesUsers());


    }


    // Obter Lista de usuários de acordo com sua ROLE
    public PaginatedUsersResponse getUsersByRole(RolesUsers rolesUsers, int page, int size) {
        // Cria um objeto Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Busca a página de usuários com a ROLE especificada
        Page<UserModel> userPage = this.userRepository.findByRolesUsers(rolesUsers, pageable);

        // Mapeia os usuários encontrados para o formato de resposta desejado
        List<UserResponse> userRespons = userPage.getContent()
                .stream()
                .map(user -> new UserResponse(user.getId(), user.getEmail(), user.getPhone(),  user.getRolesUsers()))
                .collect(Collectors.toList());

        // Retorna a resposta paginada
        return new PaginatedUsersResponse(userRespons, userPage.getTotalPages(), userPage.getTotalElements());
    }


    // Criação do Cupom
    public CouponCreateResponse createCoupon(CouponCreateDto couponCreateDto) throws CouponExistsException {
        // Verifica se o cupom já existe
        if (couponRepository.existsByCode(couponCreateDto.couponCode())) {
            throw new CouponExistsException("Este cupom já existe!");
        }

        // Cria o modelo do cupom a partir dos dados do DTO
        CouponModel couponModel = CouponModel.builder()
                .createdAt(LocalDateTime.now())
                .couponActive(couponCreateDto.couponActive())
                .code(couponCreateDto.couponCode().toUpperCase())
                .description(couponCreateDto.description())
                .expirationDate(couponCreateDto.expirationDate())
                .maxDiscountAmount(couponCreateDto.maxDiscountAmount())
                .minimumAmount(couponCreateDto.minimumAmount())
                .singleUse(couponCreateDto.singleUse())
                .valuePerCentDiscount(couponCreateDto.valuePerCentDiscount())
                .usageLimit(couponCreateDto.usageLimit())
                .freeShipping(couponCreateDto.freeShipping())
                .build();

        couponRepository.save(couponModel);

        // Retorna a resposta de criação do cupom
        return new CouponCreateResponse(
                couponCreateDto.couponCode(),
                couponCreateDto.description(),
                String.format("Cupom '%s' criado com sucesso!", couponCreateDto.couponCode())
        );
    }




}

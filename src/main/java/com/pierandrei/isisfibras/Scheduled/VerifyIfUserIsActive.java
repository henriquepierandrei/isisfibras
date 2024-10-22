package com.pierandrei.isisfibras.Scheduled;

import com.pierandrei.isisfibras.Model.UserModels.UserModel;
import com.pierandrei.isisfibras.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VerifyIfUserIsActive {
    private final UserRepository userRepository;

    // Define um cron para executar o método, por exemplo, uma vez por dia
    @Scheduled(cron = "0 0 0 * * ?") // Executa à meia-noite todos os dias
    public void verifyIfActive() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Obtém todos os usuários
        List<UserModel> userModels = userRepository.findAll();

        // Filtra usuários que não estão ativos por 60 dias
        List<UserModel> usersToDeactivate = userModels.stream()
                .filter(userModel ->
                        ChronoUnit.DAYS.between(userModel.getLastLoginAt(), currentDateTime) == 60
                )
                .toList();

        // Desativa os usuários encontrados
        usersToDeactivate.forEach(userModel -> userModel.setActive(false));

        // Salva todos os usuários desativados de uma vez
        if (!usersToDeactivate.isEmpty()) {
            userRepository.saveAll(usersToDeactivate);
        }
    }








}

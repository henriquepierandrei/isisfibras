package com.pierandrei.isisfibras.Service.AdminServices;

import com.pierandrei.isisfibras.Model.UserModels.UserModel;
import com.pierandrei.isisfibras.Repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;


    // Enviar email de promoção para usuários com a permissão de envio de promoção ativo.
    public void sendEmailPromotionsToUser(String title, String content, String linkImageOffer, String principalLink) {
        List<UserModel> userModels = this.userRepository.findByReceivePromotions(true);
        for (UserModel userModel : userModels) {
            sendEmail(title, userModel.getName(), content, linkImageOffer, principalLink, userModel.getEmail());
        }
    }


    public void sendEmail(String title, String name, String content, String linkImageOffer, String principalLink, String emailTo) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(emailTo);
            helper.setSubject(title);

            String htmlContent = "<html lang='pt-BR'>"
                    + "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;width: max-content;margin: auto;'>"
                    + "    <!-- Container principal -->"
                    + "    <table width='100%' cellspacing='0' cellpadding='0' style='background-color: #f4f4f4; padding: 20px 0;'>"
                    + "        <tr>"
                    + "            <td>"
                    + "                <!-- Conteúdo do email -->"
                    + "                <table width='600' cellpadding='0' cellspacing='0' align='center' style='background-color: white; border-radius: 10px; overflow: hidden; box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);'>"
                    + "                    <!-- Cabeçalho -->"
                    + "                    <tr>"
                    + "                        <td align='center' style='background-color:rgb(82, 82, 238); padding: 20px;'>"
                    + "                            <img src='https://dl.dropboxusercontent.com/scl/fi/1zg1icar7b4iflcsqmjcm/Design_sem_nome__2_-removebg-preview.png?rlkey=q2x6jlefsr4qtn66r1oo2f6qi&st=ks1vsjh3&dl=0'"
                    + "                                alt='Logo Isis Fibras' style='display: block; width: 150px;'>"
                    + "                        </td>"
                    + "                    </tr>"
                    + "                    <!-- Corpo da mensagem -->"
                    + "                    <tr>"
                    + "                        <td style='padding: 30px 20px; text-align: left;'>"
                    + "                            <h1 style='color: #007bff; font-size: 2.1em;'>" + title + " </h1>"
                    + "                            <p style='color: #333; font-size: 1.5em;'>Prezado(a) <strong style='color:#007bff;'>" + name + "</strong>, fique por"
                    + "                                dentro das <strong style='color:#007bff;'>promoções!</strong></p>"
                    + "                            <p style='color: #333; font-size: 1em;'>" + content + "</p>"
                    + "                        </td>"
                    + "                    </tr>"
                    + "                    <!-- Rodapé -->"
                    + "                    <tr>"
                    + "                        <td style='background-color: #f8f8f8; padding: 20px; text-align: center; border-top: 2px solid #e02f44;'>"
                    + "                            <div style='width: 700px;height: 700px;background-color: rgb(228, 215, 215);'>"
                    + "                                <img src='" + linkImageOffer + "' alt='' width='700px' height='700px'>"
                    + "                            </div>"
                    + "                            <br><br>"
                    + "                            <a href='" + principalLink + "' style='background-color: rgb(82, 82, 238);text-align: center;padding: 15px 150px 15px 150px;color: white;text-decoration: none;font-weight: 900;border-radius: 15px;'>Conferir Promoções!</a>"
                    + "                            <p style='color: #888; font-size: 0.9em; margin: 10px 0;padding-top: 30px;'>Isis Fibras - Estofamento e Almofadas</p>"
                    + "                            <p style='color: #888; font-size: 0.9em;'>CNPJ xxxxxxxxxxxxxxx</p>"
                    + "                            <hr style='border: none; border-top: 1px solid #ccc; margin: 20px 0;'>"
                    + "                            <!-- Redes sociais e contato -->"
                    + "                            <p style='color: #888; font-size: 0.9em;'>Siga nas redes sociais:</p>"
                    + "                            <p>"
                    + "                                <a href='#' style='margin: 0 10px; color: #888;'><img"
                    + "                                        src='https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Instagram_icon.png/1200px-Instagram_icon.png'"
                    + "                                        alt='Instagram' style='width: 30px;'></a>"
                    + "                                <a href='#' style='margin: 0 10px; color: #888;'><img"
                    + "                                        src='https://static.vecteezy.com/system/resources/thumbnails/016/716/480/small/whatsapp-icon-free-png.png'"
                    + "                                        alt='WhatsApp' style='width: 30px;'></a>"
                    + "                            </p>"
                    + "                            <p style='color: #888; font-size: 0.9em; margin-top: 20px;'>Telefone para atendimento: (11) 3003-3994</p>"
                    + "                        </td>"
                    + "                    </tr>"
                    + "                </table>"
                    + "            </td>"
                    + "        </tr>"
                    + "    </table>"
                    + "</body>"
                    + "</html>";

            helper.setText(htmlContent, true);
            javaMailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }



}

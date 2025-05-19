package com.dolloer.colla.domain.mail.serivce;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendHtmlInvitationEmail(String toEmail, String projectName, String inviteUrl) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject("[Colla] 프로젝트 초대: " + projectName);

        String html = """
                    <div style="font-family: Arial; font-size: 14px;">
                        <p><strong>프로젝트 [%s]</strong>에 초대되었습니다.</p>
                        <p>아래 버튼을 눌러 초대를 수락하거나 거절해주세요.</p>
                        <a href="%s/accept" style="...">수락하기</a>
                        <a href="%s/reject" style="...">거절하기</a>
                        <p style="margin-top: 20px;">Colla 팀</p>
                    </div>
                """.formatted(projectName, inviteUrl, inviteUrl);

        helper.setText(html, true); // HTML 전송

        mailSender.send(message);

        log.info("{}로 메일 전송 완료", toEmail);
    }
}
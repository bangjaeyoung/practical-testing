package sample.cafekiosk.spring.api.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MailService {

    private final MailSendClient mailSendClient;
    private final MailSendHistoryRepository mailSendHistoryRepository;

    @Transactional
    public boolean sendMail(String fromEmail, String toEmail, String subject, String content) {
        boolean result = mailSendClient.sendEmail(fromEmail, toEmail, subject, content);
        if (result) {
            mailSendHistoryRepository.save(MailSendHistory.builder()
                .fromEmail(fromEmail)
                .toEmail(toEmail)
                .subject(subject)
                .content(content)
                .build()
            );

            mailSendClient.a();
            mailSendClient.b();
            mailSendClient.c();

            return true;
        }

        return false;
    }
}

package dev.martinl.linkfacil.api.application.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets

@Service
class EmailService(private val mailSender: JavaMailSender) {

    @Value("\${spring.mail.username}")
    private lateinit var fromEmail: String

    fun sendVerificationEmail(to: String, token: String) {
        val subject = "Please verify your email"
        val verificationUrl = "http://localhost:8080/api/auth/verify?token=$token"
        
        val content = """
            <div>
                <h1>Email Verification</h1>
                <p>Thank you for registering. Please click on the link below to verify your email:</p>
                <a href="$verificationUrl">Verify Email</a>
            </div>
        """.trimIndent()
        
        sendEmail(to, subject, content)
    }

    private fun sendEmail(to: String, subject: String, content: String) {
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(
            message,
            MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
            StandardCharsets.UTF_8.name()
        )
        
        helper.setFrom(fromEmail)
        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(content, true) // true indicates HTML content
        
        mailSender.send(message)
    }
}
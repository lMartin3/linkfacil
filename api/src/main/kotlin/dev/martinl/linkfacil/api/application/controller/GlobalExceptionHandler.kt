package dev.martinl.linkfacil.api.application.controller

import dev.martinl.linkfacil.api.domain.dto.MessageResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<MessageResponse> {
        val errors = ex.bindingResult.allErrors.joinToString(", ") { error ->
            val fieldName = (error as? FieldError)?.field ?: ""
            val message = error.defaultMessage ?: "Invalid input"
            if (fieldName.isNotEmpty()) "$fieldName: $message" else message
        }

        return ResponseEntity.badRequest().body(MessageResponse("Validation error: $errors"))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException): ResponseEntity<MessageResponse> {
        val errorMessage = ex.message ?: "Invalid request body"

        // Extract field name from the error message if possible
        val fieldNameRegex = "JSON property (\\w+)".toRegex()
        val fieldNameMatch = fieldNameRegex.find(errorMessage)
        val fieldName = fieldNameMatch?.groupValues?.getOrNull(1)

        val userFriendlyMessage = if (fieldName != null) {
            "Required field '$fieldName' is missing or has an invalid format"
        } else {
            "Invalid request format. Please check your request body"
        }

        return ResponseEntity.badRequest().body(MessageResponse(userFriendlyMessage))
    }
}
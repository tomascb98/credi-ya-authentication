package co.com.crediya.api.dto.request;

public record GetUsersByDocumentNumbersRequestDto(
        String[] userDocumentNumbers
){}

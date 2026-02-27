package model;

public record LoginRequest(
        String taxId,
        String password
) {
}

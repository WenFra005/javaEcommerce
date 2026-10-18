package com.ecommerce.userservice.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Perfis de acesso atribuídos a um
 * {@link com.ecommerce.userservice.model.User}.
 *
 * <p>
 * O conjunto é usado para classificar permissões e responsabilidades dentro do
 * serviço.
 *
 * @since 0.1.0
 */
public enum UserRole {
    ADMIN, CLIENTE, VENDEDOR, FORNECEDOR;

    /**
     * Converte um valor textual no respectivo perfil de acesso.
     *
     * <p>
     * A conversão é case-insensitive para tolerar entradas vindas de payloads e
     * integrações externas sem exigir formatação exata.
     *
     * @param value valor textual informado para o perfil.
     * @return o perfil correspondente ao valor informado.
     * @throws IllegalArgumentException se {@code value} for nulo, vazio ou não
     *                                  corresponder a um perfil conhecido.
     */
    @JsonCreator
    public static UserRole fromString(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("UserRole cannot be null or empty");
        }

        try {
            return UserRole.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException _) {
            throw new IllegalArgumentException(
                    String.format("Invalid value for 'userRole': '%s'. Allowed values: %s", value,
                            Arrays.toString(UserRole.values())));
        }
    }

}

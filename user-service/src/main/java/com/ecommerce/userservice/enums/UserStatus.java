package com.ecommerce.userservice.enums;

/**
 * Estados operacionais de um {@link com.ecommerce.userservice.model.User}.
 *
 * <p>
 * Define se a conta pode operar normalmente, foi desativada temporariamente ou
 * está suspensa por alguma restrição administrativa.
 *
 * @since 1.0
 */
public enum UserStatus {
    ATIVO,
    INATIVO,
    SUSPENSO
}

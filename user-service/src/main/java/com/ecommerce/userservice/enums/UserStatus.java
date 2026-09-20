package com.ecommerce.userservice.enums;

/**
 * Estados possíveis de um {@link User} no sistema.
 *
 * <p>
 * Controla o ciclo de vida de um usuário:
 * <ul>
 * <li>{@code ATIVO} – conta operacional.</li>
 * <li>{@code INATIVO} – conta desativada temporariamente.</li>
 * <li>{@code SUSPENSO} – conta suspensa por violação de políticas.</li>
 * </ul>
 *
 * @since 1.0
 */
public enum UserStatus {
    ATIVO,
    INATIVO,
    SUSPENSO
}

package com.ecommerce.userservice.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;

import com.ecommerce.userservice.enums.UserRole;
import com.ecommerce.userservice.enums.UserStatus;
import com.ecommerce.userservice.enums.UserType;
import com.ecommerce.userservice.model.LegalEntity;
import com.ecommerce.userservice.model.NaturalPerson;
import com.ecommerce.userservice.model.RefreshToken;
import com.ecommerce.userservice.model.User;

public class TestDataFactory {

    private static final String DEFAULT_NAME = "Test User";
    private static final String DEFAULT_EMAIL = "test@example.com";
    private static final String DEFAULT_PASSWORD = "encodedPassword";

    public static User createUser() {
        return createUser(DEFAULT_EMAIL);
    }

    public static User createUser(String email) {
        User user = new User();
        user.setName(DEFAULT_NAME);
        user.setUserEmail(email);
        user.setUserPassword(DEFAULT_PASSWORD);
        user.setUserStatus(UserStatus.ATIVO);
        user.setUserRole(UserRole.CLIENTE);
        user.setUserType(UserType.PF);
        user.setUserCreatedAt(Instant.now());

        return user;
    }

    public static User createAdmin() {
        return createAdmin(DEFAULT_EMAIL);
    }

    public static User createAdmin(String email) {
        User user = createUser(email);
        user.setUserRole(UserRole.ADMIN);
        user.setUserType(UserType.SYSTEM);

        return user;
    }

    public static User createUserWithStatus(String email, UserStatus status) {
        User user = createUser(email);
        user.setUserStatus(status);
        return user;
    }

    public static NaturalPerson createNaturalPerson(User user, String cpf) {
        NaturalPerson np = new NaturalPerson();
        np.setUser(user);
        np.setCpf(cpf);
        np.setBirthDate(LocalDate.of(1990, Month.JANUARY, 1));

        return np;
    }

    public static LegalEntity createLegalEntity(User user, String cnpj, String companyName, String stateRegistration) {
        LegalEntity le = new LegalEntity();
        le.setUser(user);
        le.setCnpj(cnpj);
        le.setCompanyName(companyName);
        le.setStateRegistration(stateRegistration);

        return le;
    }

    public static RefreshToken createRefreshToken(User user, String token, Instant expiryDate) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(expiryDate);
        refreshToken.setRevoked(false);

        return refreshToken;
    }

}

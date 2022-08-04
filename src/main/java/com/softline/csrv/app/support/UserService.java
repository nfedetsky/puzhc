package com.softline.csrv.app.support;

import com.softline.csrv.entity.Function;
import com.softline.csrv.entity.InfoSystem;
import com.softline.csrv.entity.Request;
import com.softline.csrv.entity.User;
import com.softline.csrv.enums.RequestTypeCode;
import com.softline.csrv.enums.RoleCode;
import com.softline.csrv.security.DatabaseUserRepository;
import com.softline.jmix.poib.userdetails.PoibSearchProfileRepository;
import io.jmix.core.security.CurrentAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.*;


@Service(UserService.NAME)
public class UserService {
    public static final String NAME = "support_UserService";

    private final CurrentAuthentication currentAuthentication;
    private final DatabaseUserRepository databaseUserRepository;
    private final PoibSearchProfileRepository poibSearchProfileRepository;

    @Autowired
    public UserService(CurrentAuthentication currentAuthentication,
                       DatabaseUserRepository databaseUserRepository,
                       PoibSearchProfileRepository poibSearchProfileRepository) {
        this.currentAuthentication = currentAuthentication;
        this.databaseUserRepository = databaseUserRepository;
        this.poibSearchProfileRepository = poibSearchProfileRepository;
    }

    public User loadUser(UserDetails userDetails) {
        return userDetails != null ? databaseUserRepository.loadUserByUsername(userDetails.getUsername()) : null;
    }

    private List<User> loadUsers(List<UserDetails> userDetailsList) {
        List<User> users = new ArrayList<>();
        for (UserDetails userDetails : userDetailsList) {
            User user = loadUser(userDetails);
            if (user != null) {
                users.add(user);
            }
        }
        return users;
    }

    public User getCurrentUser() {
        return loadUser(currentAuthentication.getUser());
    }

    /*
     * У пользователя есть Роль?
     */
    public boolean isUserHasRole(User user, RoleCode roleCode) {
        List<User> list = getUsers(roleCode);
        return list.contains(user);
    }

    /*
     * Поиск пользователя по Роли
     */
    public User getUser(RoleCode roleCode) {
        return getUsers(roleCode).stream().findFirst().orElse(null);
    }

    /*
     * Поиск пользователей по Роли
     */
    public List<User> getUsers(RoleCode roleCode) {
        return loadUsers(poibSearchProfiles(roleCode));
    }

    /*
     * Поиск пользователей по Роли - ИС
     * 1) Ищем по ИС, если нет, то
     * 2) Ищем по родительской ИС
     */
    public List<User> getUsers(RoleCode roleCode, InfoSystem infoSystem) {
        return loadUsers(searchUsers(roleCode, infoSystem));
    }

    /*
     * Поиск пользователей по Роли - Функции - ИС
     * 1) Ищем по функции, если нет, то
     * 2) Ищем по родительской Функции, если нет, то
     * 3) Ищем по ИС, если нет, то
     * 4) Ищем по родительской ИС
     */
    public List<User> getUsers(RoleCode roleCode, Function function) {
        List<UserDetails> userDetailsList = searchUsers(roleCode, function);
        if (userDetailsList.isEmpty() && function != null) {
            userDetailsList = searchUsers(roleCode, function.getSystem());
        }

        return loadUsers(userDetailsList);
    }

    private List<UserDetails> searchUsers(RoleCode roleCode, Function function) {
        if (function == null) {
            return new ArrayList<>();
        }
        List<UserDetails> userDetailsList = poibSearchProfiles(roleCode, function);
        if (userDetailsList.isEmpty() && function.getParent() != null) {
            userDetailsList = searchUsers(roleCode, function.getParent());
        }
        return userDetailsList;
    }

    private List<UserDetails> searchUsers(RoleCode roleCode, InfoSystem infoSystem) {
        if (infoSystem == null) {
            return new ArrayList<>();
        }
        List<UserDetails> userDetailsList = poibSearchProfiles(roleCode, infoSystem);
        if (userDetailsList.isEmpty() && infoSystem.getParent() != null) {
            userDetailsList = searchUsers(roleCode, infoSystem.getParent());
        }
        return userDetailsList;
    }

    private List<UserDetails> poibSearchProfiles(RoleCode roleCode) {
        return poibSearchProfileRepository.searchProfiles(roleCode.getCode());
    }

    private List<UserDetails> poibSearchProfiles(RoleCode roleCode, InfoSystem infoSystem) {
        return poibSearchProfileRepository.searchProfiles(roleCode.getCode(),
                roleCode.getCode() + "_IS_" + infoSystem.getName());
    }

    private List<UserDetails> poibSearchProfiles(RoleCode roleCode, Function function) {
        return poibSearchProfileRepository.searchProfiles(roleCode.getCode(),
                roleCode.getCode() + "_IS_" + function.getSystem().getName(), roleCode.getCode() + "_FUNCTION_" + function.getName());
    }

    public Set<String> getEmails(RoleCode roleCode) {
        return poibSearchProfileRepository.searchEmails(roleCode.getCode());
    }
}
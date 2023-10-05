package com.gsc.shopcart.security;

import com.gsc.shopcart.config.environment.EnvironmentConfig;
import com.gsc.shopcart.constants.AppProfile;
import com.gsc.shopcart.model.scart.entity.Client;
import com.gsc.shopcart.repository.scart.ClientRepository;
import com.gsc.shopcart.repository.scart.ConfigurationRepository;
import com.gsc.scgscwsauthentication.response.AuthenticationExtraResponse;
import com.gsc.scgscwsauthentication.response.ExtranetUser;
import com.gsc.scgscwsauthentication.response.PairIdName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;

import javax.naming.directory.Attributes;
import java.util.*;

public class JwtAuthenticationManager implements AuthenticationManager {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationManager.class);

    // For now we are using a hardcoded client
    public static final Long CLIENT_ID = 1L;

    private final ConfigurationRepository configurationRepository;
    private final EnvironmentConfig environmentConfig;

    public JwtAuthenticationManager(ConfigurationRepository configurationRepository, EnvironmentConfig environmentConfig) {
        this.configurationRepository = configurationRepository;
        this.environmentConfig = environmentConfig;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!configurationRepository.isLoginEnabled()) {
            throw new AuthenticationServiceException("Login is disabled");
        }

        String loginToken = authentication.getPrincipal() != null ? authentication.getPrincipal().toString() : null;
        String tokenParts[] = loginToken != null ? loginToken.split("\\|\\|\\|") : new String[0];
        loginToken = tokenParts[1];

        if (!StringUtils.hasText(loginToken)) {
            throw new BadCredentialsException("Invalid login token");
        }

        String[] parts = loginToken.split("\\|\\|");
        if (parts.length != 2) {
            throw new BadCredentialsException("Bad credentials");
        }

        /*Optional<Client> client = clientRepository.findById(CLIENT_ID);

        if (!client.isPresent()) {
            throw new BadCredentialsException("Invalid client");
        }*/

        AuthenticationExtraResponse authenticationExtra = environmentConfig.getAuthenticationInvoker().authenticationExtra(parts[0], parts[1], Integer.parseInt(tokenParts[0]));
        if (!authenticationExtra.getCode().equals("0") || authenticationExtra.getUser() == null) {
            throw new BadCredentialsException("Bad credentials");
        }

        ExtranetUser user = authenticationExtra.getUser();

        final String userId = user.getIdUser() + "||" + user.getLogin() + "||" +user.getMail();
        final Set<AppProfile> roles = getRoles(user);

        if (roles.isEmpty()) {
            throw new AuthenticationServiceException("No permissions");
        }

        UserPrincipal authUser = new UserPrincipal(userId, roles, Long.parseLong(tokenParts[0]));
        authUser.setOidDealerParent(user.getDealerParent().getObjectId());
        authUser.setOidDealer(user.getDealer().getObjectId());
        authUser.setLogin(authenticationExtra.getUser().getLogin());
        return JwtAuthenticationToken.authenticated(authUser, Collections.emptyList());
    }

    private Set<AppProfile> getRoles(ExtranetUser user) {
        Set<AppProfile> roles = new LinkedHashSet<>(AppProfile.values().length);
        Set<Integer> profiles = getProfiles(user);

        roles.add(AppProfile.TOYOTA_LEXUS_PRF_TCAP);

        for (Integer profileId : profiles) {
            if (AppProfile.compareId(profileId, AppProfile.TOYOTA_LEXUS_PRF_TCAP)) {
                roles.add(AppProfile.TOYOTA_LEXUS_PRF_TCAP);
                roles.add(AppProfile.ROLE_VIEW_ALL_DEALERS);
            } else if (AppProfile.compareId(profileId, AppProfile.TOYOTA_LEXUS_PRF_MANAGER_CA)) {
                roles.add(AppProfile.TOYOTA_LEXUS_PRF_MANAGER_CA);
                roles.add(AppProfile.ROLE_VIEW_CA_DEALERS);
            } else if (AppProfile.compareId(profileId, AppProfile.TOYOTA_LEXUS_PRF_CALLCENTER)) {
                roles.add(AppProfile.TOYOTA_LEXUS_PRF_CALLCENTER);
                roles.add(AppProfile.ROLE_VIEW_CALL_CENTER_DEALERS);
            } else if (AppProfile.compareId(profileId, AppProfile.TOYOTA_LEXUS_PRF_MANAGER_DEALER)) {
                roles.add(AppProfile.TOYOTA_LEXUS_PRF_MANAGER_DEALER);
                roles.add(AppProfile.ROLE_VIEW_DEALER_ALL_INSTALLATION);
            } else if (AppProfile.compareId(profileId, AppProfile.TOYOTA_PRF_MANAGER_INSTALLATION)) {
                roles.add(AppProfile.TOYOTA_PRF_MANAGER_INSTALLATION);
                roles.add(AppProfile.ROLE_VIEW_DEALER_OWN_INSTALLATION);
            } else if (AppProfile.compareId(profileId, AppProfile.TOYOTA_PRF_IMPORT_EXPORT)) {
                roles.add(AppProfile.TOYOTA_PRF_IMPORT_EXPORT);
                roles.add(AppProfile.ROLE_IMPORT_EXPORT);
            } else if (AppProfile.compareId(profileId, AppProfile.TOYOTA_PRF_TPA_BO_MANAGER_TPA)) {
                roles.add(AppProfile.TOYOTA_PRF_TPA_BO_MANAGER_TPA);
                roles.add(AppProfile.ROLE_TPA_BO);
            }
        }
        return roles;
    }

    private Set<Integer> getProfiles(ExtranetUser user) {
        if (user.getProfiles() == null) {
            return Collections.emptySet();
        }
        Set<Integer> profiles = new LinkedHashSet<>(user.getProfiles().size());
        for (PairIdName profile : user.getProfiles()) {
            profiles.add(profile.getId());
        }
        return profiles;
    }

}

package com.softline.csrv.userdetails;


import com.softline.csrv.entity.User;
import com.softline.jmix.poib.user.JmixPoibProfile;
import com.softline.jmix.poib.userdetails.AbstractPoibUserDetailsSynchronizationStrategy;
import org.springframework.stereotype.Component;

@Component("poib_UserSynchronizationStrategy")
public class UserSynchronizationStrategy extends AbstractPoibUserDetailsSynchronizationStrategy<User> {

    @Override
    protected Class<User> getUserClass() {
        return User.class;
    }

    @Override
    protected void mapUserDetailsAttributes(User userDetails, JmixPoibProfile ctx) {
        userDetails.setFirstName(ctx.getFirstName());
        userDetails.setLastName(ctx.getLastName());
        userDetails.setEmail(ctx.getEmail());
    }
}

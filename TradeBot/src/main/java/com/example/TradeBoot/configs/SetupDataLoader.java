package com.example.TradeBoot.configs;

import com.example.TradeBoot.ui.UserRepository;
import com.example.TradeBoot.ui.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;


//
//public class SetupDataLoader implements
//        ApplicationListener<ContextRefreshedEvent> {
//
//    boolean alreadySetup = false;
//
//    @Autowired
//    private UserRepository userRepository;
//
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Override
//    @Transactional
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//
//        if (alreadySetup)
//            return;
//
//        var adminName = "u";
//        var defaultPassword = "p";
//        var adminInDB = userRepository.findByUsername(adminName);
//        if (adminInDB == null) {
//
//            User user = new User();
//            user.setUsername(adminName);
//            user.setPassword(passwordEncoder.encode(defaultPassword));
//            user.setActive(true);
//
//            userRepository.save(user);
//        }
//
//
//        alreadySetup = true;
//    }
//
//
//}

package com.godcoder.myhome.service;

import com.godcoder.myhome.model.Role;
import com.godcoder.myhome.model.User;
import com.godcoder.myhome.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setEnabled(true);  //회원가입시 활성화된 상태
        Role role = new Role();
        role.setId(1l); //하드코팅... 롤 검색해서 가져오고 롤롤레지토리 만들어서 조회해야 하지만 귀찮아서 임시로..
        user.getRoles().add(role);  //롤에 어떤 권한을 줄지 롤스에 저장가능.
        return userRepository.save(user);
    }

}

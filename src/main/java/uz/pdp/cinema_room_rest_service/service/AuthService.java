package uz.pdp.cinema_room_rest_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.cinema_room_rest_service.dto.UserRegisterDto;
import uz.pdp.cinema_room_rest_service.model.Role;
import uz.pdp.cinema_room_rest_service.model.RoleEnum;
import uz.pdp.cinema_room_rest_service.model.User;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.repository.RoleRepository;
import uz.pdp.cinema_room_rest_service.repository.UserRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }else throw new UsernameNotFoundException("user not found");
    }

    public HttpEntity<?> register(UserRegisterDto userRegisterDto) {
        ApiResponse response = new ApiResponse();
        try {
            User user = new User();
            user.setPassword(userRegisterDto.getPassword());
            user.setUsername(userRegisterDto.getUsername());
            user.setPhoneNumber(userRegisterDto.getPhoneNumber());
            user.setEmail(userRegisterDto.getEmail());
            user.setBio(userRegisterDto.getBio());
            user.setFullName(userRegisterDto.getFullName());
            Role role_user = roleRepository.findByName("ROLE_USER");
            if (role_user == null) {
                role_user = new Role(null, RoleEnum.ROLE_USER,"ROLE_USER");
                roleRepository.save(role_user);
            }
            user.setRoles(Collections.singleton(role_user));
            userRepository.save(user);
            Map<String ,String> info = new HashMap<>();
            info.put("username", userRegisterDto.getUsername());
            info.put("password",userRegisterDto.getPassword());
            response.setSuccess(true);
            response.setData(info);
            response.setMessage("successfully registered");
        }catch (Exception e){
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setSuccess(false);
        }
        return ResponseEntity.ok(response);
    }
}

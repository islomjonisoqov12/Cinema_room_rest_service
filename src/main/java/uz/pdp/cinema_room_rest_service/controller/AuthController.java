package uz.pdp.cinema_room_rest_service.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.speing_jwt_demo.dto.UserDto;
import uz.pdp.speing_jwt_demo.security.JwtProvider;
import uz.pdp.speing_jwt_demo.service.UserService;

@RestController
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    JwtProvider jwtProvider;


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {
        UserDetails userDetails = userService.loadUserByUsername(userDto.getUsername());
        String generatedToken = jwtProvider.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(generatedToken);


    }

}

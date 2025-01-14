package com.example.usermanagementservice.rest;


import com.example.usermanagementservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import com.example.usermanagementservice.rest.dto.UserDTO;
import com.example.usermanagementservice.service.UserService;

import java.util.function.Function;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public Long savingUser(@RequestBody UserDTO userDTO){

        return userService.registerUser(toUser(userDTO));
    }


    @GetMapping("/{id}")
    public UserDTO loadingUser(@PathVariable Long id){
        return userService.loadById(id)
                .map(toUserDto())
                .orElseThrow(()->new RuntimeException("User not found"));
    }

    @GetMapping("/search")
    public Page<UserDTO> loading(int  pageNo, int pageSize){
        return userService.loadAll(PageRequest.of(pageNo,pageSize))
                .map(toUserDto());

    }

    @GetMapping("/search-by-name")
    public Page<UserDTO> loadingByName(@RequestParam String name,int  pageNo,int pageSize){
        return userService.loadAllByName(name,PageRequest.of(pageNo,pageSize))
                .map(toUserDto());

    }

    @DeleteMapping("/{id}")
    public void removingUser(@PathVariable Long id){
        userService.unregisterUser(id);
    }

    private Function<User, UserDTO> toUserDto() {
        return user-> UserDTO
                .builder()
                .active(user.isActive())
                .age(user.getAge())
                .city(user.getCity())
                .email(user.getEmail())
                .id(user.getId())
                .name(user.getName())
                .build();

    }

    private User toUser(UserDTO userDTO) {
        return User
                .builder()
                .id(userDTO.getId())
                .active(userDTO.isActive())
                .age(userDTO.getAge())
                .city(userDTO.getCity())
                .email(userDTO.getEmail())
                .name(userDTO.getName())
                .build();
    }
}


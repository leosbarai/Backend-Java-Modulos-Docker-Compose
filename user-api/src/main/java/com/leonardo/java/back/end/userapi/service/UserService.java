package com.leonardo.java.back.end.userapi.service;

import com.leonardo.java.back.end.exception.UserNotFoundException;
import com.leonardo.java.back.end.user.dto.UserDTO;
import com.leonardo.java.back.end.userapi.converter.DTOConverter;
import com.leonardo.java.back.end.userapi.model.User;

import com.leonardo.java.back.end.userapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Value("${USER_API_URL:http://localhost:8081/user/}")
    private String userApiURL;

    @Autowired
    private UserRepository userRepository;

    public UserDTO getUserByCpf(String cpf) {
        RestTemplate restTemplate = new RestTemplate();
        String url = userApiURL + cpf;
        ResponseEntity<UserDTO> response = restTemplate.getForEntity(url, UserDTO.class);

        return response.getBody();
    }

    public List<UserDTO> getAll() {
        List<User> usuarios = userRepository.findAll();
        return usuarios.stream().map(DTOConverter::convert).collect(Collectors.toList());
    }

    public UserDTO findById(Long userId) {
        Optional<User> usuario = userRepository.findById(userId);
        if (usuario.isPresent()) {
            return DTOConverter.convert(usuario.get());
        }

        return null;
    }

    public UserDTO save(UserDTO userDTO) {
        userDTO.setKey(UUID.randomUUID().toString());
        User user = userRepository.save(User.convert(userDTO));
        return DTOConverter.convert(user);
    }

    public UserDTO delete(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        }

        return null;
    }

    public UserDTO findByCpf(String cpf, String key) {
        User user = userRepository.findByCpfAndKey(cpf, key);
        if (user != null) {
            return DTOConverter.convert(user);
        }
        throw new UserNotFoundException();
    }

    public List<UserDTO> queryByName(String name) {
        List<User> usuarios = userRepository.queryByNomeLike(name);
        return usuarios.stream().map(DTOConverter::convert).collect(Collectors.toList());
    }
}

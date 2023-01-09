package com.leonardo.java.back.end.userapi.converter;

import com.leonardo.java.back.end.user.dto.UserDTO;
import com.leonardo.java.back.end.userapi.model.User;

public class DTOConverter {

    public static UserDTO convert(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setNome(user.getNome());
        userDTO.setEndereco(user.getEndereco());
        userDTO.setCpf(user.getCpf());
        return userDTO;
    }
}

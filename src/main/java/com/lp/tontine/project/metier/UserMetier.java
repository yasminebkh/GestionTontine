package com.lp.tontine.project.metier;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.lp.tontine.project.Repository.LoginRepository;
import com.lp.tontine.project.entities.User;

@Component
@Service
public class UserMetier {

	@Autowired
	private LoginRepository repository;
	

	public void signUpUser(User user) {
		repository.save(user);
	}
	
	public User getUser(String email, String password){
		return repository.findByEmailAndPassword(email, password);
	}

	public User getUserByEmail(String email){
		return repository.findByEmail(email);
	}
	
//	public List<User> findAllUsers(){
//		List<User> users = repository.findAll();
//		return users.stream().map((user) -> mapToUserDto(user))
//                .collect(Collectors.toList());
//	}
//	private UserDto mapToUserDto(User user){
//        UserDto userDto = new UserDto();
//        String[] str = user.getName().split(" ");
//        userDto.setFirstName(str[0]);
//        userDto.setLastName(str[1]);
//        userDto.setEmail(user.getEmail());
//        return userDto;
//    }
}

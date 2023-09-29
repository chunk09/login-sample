package kr.lambda.loginsample.services;

import kr.lambda.loginsample.entities.User;
import kr.lambda.loginsample.repositories.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    /**
     * This function gets all user
     * @return all user's data
     */
    public List<User> findAll() {
        return repository.findAll();
    }

    /**
     * This function gets user that found by key
     * @param key user's key
     * @return user's data
     */
    public User findByKey(long key) {
        return repository.findById(key).orElseGet(User::new);
    }

    public void createUser(User user) {
        repository.save(user);
        System.out.println("User creation succeeded.");
    }
}

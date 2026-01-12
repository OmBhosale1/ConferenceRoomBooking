package Project.ConferenceBookingSystem.Services;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisTestService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisTestService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void test() {
        redisTemplate.opsForValue().set("test-key", "hello-redis");
        Object value = redisTemplate.opsForValue().get("test-key");
        System.out.println("Redis value = " + value);
    }
}

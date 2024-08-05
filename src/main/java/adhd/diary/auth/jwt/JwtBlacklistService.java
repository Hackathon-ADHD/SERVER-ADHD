package adhd.diary.auth.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class JwtBlacklistService {

    private static final String BLACKLIST_KEY_PREFIX = "blacklist:";

    private RedisTemplate<String, Object> redisTemplate;

    public JwtBlacklistService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addTokenToBlacklist(String token) {
        redisTemplate.opsForValue().set(BLACKLIST_KEY_PREFIX + token, "true");
    }

    public boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey(BLACKLIST_KEY_PREFIX + token);
    }

    public void removeTokenFromBlacklist(String token) {
        redisTemplate.delete(BLACKLIST_KEY_PREFIX + token);
    }
}

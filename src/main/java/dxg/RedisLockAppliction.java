package dxg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *  测试redis锁各种情况
 *
 * @author dingxigui
 * @date 2020/4/22
 */
@SpringBootApplication
public class RedisLockAppliction {

    public static void main(String[] args) {
        SpringApplication.run(RedisLockAppliction.class,args);
    }

}

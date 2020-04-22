package dxg;



import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import net.bytebuddy.asm.Advice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;



import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

/**
 * springbootTest
 *
 * @author dingxigui
 * @date 2020/4/22
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisLockAppliction.class)
public class RedisLoclApplictionTest {

   @Autowired
    private RedisTemplate redisTemplate;

    /**
     *  采用  lettuce的reids nio异步客户端
     */

   //@Test
    public void test1(){

        String lock = "lock" ;
        String requester = "123" ;
        redisTemplate.multi();

        RedisCallback<String> redisCallback = (c)->{
            //开启事务
            //c.multi();
            try {


                //lettuce的reids
                  // 成功返回true ，失败返回false ， 事务或者pipeline返回 null
                //Boolean bool = c.set(lock.getBytes("utf-8"), requester.getBytes("utf-8"), Expiration.from(60, TimeUnit.SECONDS), RedisStringCommands.SetOption.SET_IF_ABSENT);
                //System.out.println("bool = "+bool);


                ////jedis的reids
                //如果中途使用pipiline或者事务会抛出异常
                // jedis.set(lock,requester,"NX","PX",100000)  成功返回   字符 ok  失败返回 null
//                Jedis jedis = (Jedis)c.getNativeConnection();
//                System.out.println(jedis.set(lock,requester,"NX","PX",100000));
            //执行事务
            c.exec();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "" ;
        };
        redisTemplate.execute(redisCallback);
      // redisTemplate.exec();
    }

    String sc = "local a=2\n" +
            "if(a==1)\n" +
            "then\n" +
            "\treturn 10\n" +
            "else \n" +
            "\treturn 30\n" +
            "\t\n" +
            "end\t";

    //@Test
//    public void test2(){
////
////        RedisCallback<Object> redisCallback = (c)->{
////
////            try {
////                Jedis jedis = (Jedis) c.getNativeConnection();
////                Object res = jedis.eval(sc);
////                System.out.println(res);
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////            return 1  ;
////        };
////        redisTemplate.execute(redisCallback);
////
////
////    }


   // @Test
    public void test3(){

        RedisCallback<Object> redisCallback = (c)->{

            try {
                RedisAsyncCommands redisAsyncCommands =(RedisAsyncCommands) c.getNativeConnection() ;
                RedisFuture eval = redisAsyncCommands.eval(sc, ScriptOutputType.INTEGER);
                System.out.println(eval.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 1  ;
        };
        redisTemplate.execute(redisCallback);

    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate ;

    @Test
    public void test4(){

        String sc = "redis.call(\"set\",KEYS[1],KEYS[1])\n" +
                "redis.call(\"set\",KEYS[2],KEYS[2])\n" +
                "return 1 " ;
        RedisCallback<Object> redisCallback = (c)->{

            try {
                RedisAsyncCommands redisAsyncCommands =(RedisAsyncCommands) c.getNativeConnection() ;
                //使用同步
               // RedisCommands sync = redisAsyncCommands.getStatefulConnection().sync();
                StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

                //ScriptOutputType.VALUE 数据类型还要对上不然抛出  java.lang.IllegalStateException
                RedisFuture eval = redisAsyncCommands.eval(sc, ScriptOutputType.INTEGER, stringRedisSerializer.serialize("ding1"), stringRedisSerializer.serialize("ding2"));
                System.out.println(eval.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 1  ;
        };
        stringRedisTemplate.execute(redisCallback);

    }

}

package com.example.ctf;


import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;


@EnableAutoConfiguration
@RestController
public class IndexController {

    public static JWTs dsa = new JWTs();
    static {
        //JWTs dsa = new JWTs();
        JWTs.init(dsa);
        JWTs.getKey(dsa);
    }
        @GetMapping("/")
        public String hello(HttpServletResponse response) throws Exception {
            response.addHeader("Token",JWTs.generateToken("banana",dsa));

            return "<h1>Welcome to NCTF 2022!!</h1>";
        }

        @RequestMapping("/object")
        public String starter(HttpServletRequest request) throws Exception {
            String token = request.getHeader("Token");
            if(token!=null){
                if ((boolean)JWTs.verifyToken(token,dsa)){
                    try {
                        Hessian2Input hessian2Input = new Hessian2Input(request.getInputStream());
                        Object o = hessian2Input.readObject();
                        //System.out.println(o);
                    }catch (Exception e){}

                    return "<h1>Do You Like Alibanana~</h1>";
                }

            }
                return "<h1>No Token ~</h1>";
            //return  "<h1>ohhh Token and object :)</h1>";

        }

}

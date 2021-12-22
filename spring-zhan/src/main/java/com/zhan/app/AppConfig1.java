/**
 * 在配置类上通过Import注解向Spring容器中注册RegularBean
 */
@Configuration
@Import(RegularBean.class)
public class AppConfig {
}
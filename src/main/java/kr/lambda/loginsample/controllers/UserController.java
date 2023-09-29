package kr.lambda.loginsample.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.lambda.loginsample.SessionConst;
import kr.lambda.loginsample.body.RegisterUser;
import kr.lambda.loginsample.entities.User;
import kr.lambda.loginsample.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    private String reigstErrMsg = "";
    private String logErrMsg = "";

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        boolean isLogin = false;

        if (session != null) {
            isLogin = true;
            model.addAttribute("key", session.getAttribute(SessionConst.LOGIN_MEMBER).toString());
        }

        model.addAttribute("isLogin", isLogin);

        return "home";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("errorMsg", reigstErrMsg);

        return "register";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("errorMsg", logErrMsg);

        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if(session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }

    @PostMapping("/register/create")
    public String signUp(RegisterUser user) {
        String id = user.getId();
        String password = user.getPassword();
        String confirm = user.getConfirm();
        String name = user.getName();

        if (id.isBlank()) {
            reigstErrMsg = "ID is blanked";
            return "redirect:/register";
        } else if (password.isBlank()) {
            reigstErrMsg = "Password is blanked";
            return "redirect:/register";
        } else if (name.isBlank()) {
            reigstErrMsg = "Name is blanked";
            return "redirect:/register";
        }

        if (!password.equals(confirm)) {
            reigstErrMsg = "password does not confirm";
            return "redirect:/register";
        }

        User createUser = new User();

        createUser.setId(id);
        createUser.setPassword(password);
        createUser.setName(name);

        service.createUser(createUser);

        return "redirect:/";
    }

    @PostMapping("/login")
    public String signIn(User user, HttpServletRequest request) {
        String id = user.getId();
        String password = user.getPassword();

        if (id.isBlank()) {
            logErrMsg = "ID is blanked";
            return "redirect:/login";
        } else if (password.isBlank()) {
            logErrMsg = "Password is blanked";
            return "redirect:/login";
        }

        for (User it: service.findAll()) {
            if (id.equals(it.getId()) && password.equals(it.getPassword())) {
                //로그인 성공 처리
                //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
                HttpSession session = request.getSession();
                //세션에 로그인 회원 정보 보관
                session.setAttribute(SessionConst.LOGIN_MEMBER, it.getKey());

                return "redirect:/";
            } else {
                logErrMsg = "This user doesn't exist";
            }
        }

        return "redirect:/login";
    }
}

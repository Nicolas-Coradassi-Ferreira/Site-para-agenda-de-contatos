package br.com.nicoservices.contatospersistence.controller;


import br.com.nicoservices.contatospersistence.dto.usuario.NovoUsuarioForm;
import br.com.nicoservices.contatospersistence.exception.UsuarioJaCadastradoException;
import br.com.nicoservices.contatospersistence.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AutenticacaoController {

    @Autowired
    private UsuarioService service;


    @GetMapping("/login")
    public String login() {
        return "usuario/formLogin";
    }

    @GetMapping("/registrar")
    public String registrar(NovoUsuarioForm novoUsuarioForm) {
        return "usuario/formNovoUsuario";
    }

    @PostMapping("/registrar")
    public String cadastrar(@Valid NovoUsuarioForm novoUsuarioForm, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            return "usuario/formNovoUsuario";
        }
        service.cadastrar(novoUsuarioForm);
        return "redirect:/login";
    }

    @ExceptionHandler(UsuarioJaCadastradoException.class)
    public ModelAndView usuarioJaCadastrado(UsuarioJaCadastradoException e) {
        return new ModelAndView("usuario/formNovoUsuario")
                .addObject("novoUsuarioForm", new NovoUsuarioForm("", "", ""))
                .addObject("exception", true)
                .addObject("message", e.getMessage());
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }

}

package it.uniroma3.siw.negozio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.negozio.model.Credentials;
import it.uniroma3.siw.negozio.model.User;
import it.uniroma3.siw.negozio.service.CredentialsService;
import jakarta.validation.Valid;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthenticationController {

	private final CredentialsService credentialsService;
    private final UserDetailsService userDetailsService;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

	public AuthenticationController(CredentialsService credentialsService, UserDetailsService userDetailsService) {
		this.credentialsService = credentialsService;
        this.userDetailsService = userDetailsService;
	}

	@GetMapping(value = "/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "authentication/registerUser";
	}

	@GetMapping(value = "/login")
	public String showLoginForm(Model model) {
		return "authentication/login";
	}

	@GetMapping(value = "/admin/index")
	public String index() {
		return "admin/index";
	}

	@GetMapping(value = "/success")
	public String defaultAfterLogin() {
		return "redirect:/";
	}

	@PostMapping(value = { "/register" })
	public String registerUser(@Valid @ModelAttribute("user") User user,
			BindingResult userBindingResult, @Valid @ModelAttribute("credentials") Credentials credentials,
			BindingResult credentialsBindingResult,
            HttpServletRequest request,
            HttpServletResponse response) {

		if (!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
			user.setUsername(credentials.getUsername());
			credentials.setUser(user);
			credentialsService.saveCredentials(credentials);

            // Effettua il login automatico e lo salva nella sessione
            UserDetails userDetails = userDetailsService.loadUserByUsername(credentials.getUsername());
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            
            securityContextRepository.saveContext(context, request, response);

			return "redirect:/";
		}
		return "authentication/registerUser";
	}
}
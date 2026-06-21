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

@Controller
public class AuthenticationController {

	private final CredentialsService credentialsService;
    private final org.springframework.security.core.userdetails.UserDetailsService userDetailsService;
    private final org.springframework.security.web.context.SecurityContextRepository securityContextRepository = new org.springframework.security.web.context.HttpSessionSecurityContextRepository();

	public AuthenticationController(CredentialsService credentialsService, org.springframework.security.core.userdetails.UserDetailsService userDetailsService) {
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
            jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response) {

		if (!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
			user.setUsername(credentials.getUsername());
			credentials.setUser(user);
			credentialsService.saveCredentials(credentials);

            // Effettua il login automatico e lo salva nella sessione
            org.springframework.security.core.userdetails.UserDetails userDetails = userDetailsService.loadUserByUsername(credentials.getUsername());
            org.springframework.security.core.Authentication authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            
            org.springframework.security.core.context.SecurityContext context = org.springframework.security.core.context.SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            org.springframework.security.core.context.SecurityContextHolder.setContext(context);
            
            securityContextRepository.saveContext(context, request, response);

			return "redirect:/";
		}
		return "authentication/registerUser";
	}
}
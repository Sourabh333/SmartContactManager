package SmartContactManager.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import SmartContactManager.dao.UserRepository;
import SmartContactManager.entities.User;
import SmartContactManager.helper.Message;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/")
	public String home(Model m)
	{
		m.addAttribute("title","Home-SmartContactManager");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model m)
	{
		m.addAttribute("title","About-SmartContactManager");
		return "about";
	}
	@RequestMapping("/signup")
	public String signUp(Model m)
	{
		m.addAttribute("title","Register-SmartContactManager");
		User user =new User();
		m.addAttribute("user", user);
		return "signup";
	}
	
//	this is registration handler
	@RequestMapping(value="/do_register",method = RequestMethod.POST)
	public String registerUser(HttpSession session,@ModelAttribute("user")User user, @RequestParam(value="ucheck",defaultValue = "false")boolean ucheck,Model m)
	{
		try {
			if(ucheck==false)
			{
				System.out.println("ERROR");
				throw new Exception();
			}
			else
			{
			user.setRole("ROLE_USER");
			user.setStatus(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			User result = this.userRepository.save(user);
			
			
			System.out.println("User : "+result);
			m.addAttribute("user", new User());
			session.setAttribute("msg", new Message("Success","alert-success"));
			}
		} catch (Exception e) {
				e.printStackTrace();
				m.addAttribute("user", user);
				session.setAttribute("msg", new Message("Something went Wrong", "alert-danger"));
				return "signup";
		}
		return "signup";
	}
	
	
	//Cusstom Login Handler
	@RequestMapping("/signin")
	public String customLogin(Model m)
	{
		m.addAttribute("title","Login-SmartContactManager");

		return "login";
	}

}

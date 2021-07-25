package SmartContactManager.controller;

import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgotController {
	Random random = new Random(9999);
// email id form open handler
	@RequestMapping("/forgot")
	public String openEmailForm()
	{
		return "forgot_email_form";
	}
	@PostMapping("/send-otp")
	public String openEmailForm(@RequestParam("email")String email)
	{
		//generating Random otp
		int otp=random.nextInt(99999);
		return "varify_otp";
	}
	
}

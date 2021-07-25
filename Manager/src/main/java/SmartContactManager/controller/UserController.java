package SmartContactManager.controller;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import SmartContactManager.dao.ContactRepository;
import SmartContactManager.dao.UserRepository;
import SmartContactManager.entities.Contact;
import SmartContactManager.entities.User;
import SmartContactManager.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private ContactRepository contactRepository;
	@Autowired
	private UserRepository userRepository;
	
	public void addCommonData(Model m,Principal p)
	{
		User user=userRepository.getUserByUserName(p.getName());
		System.out.println("user : "+user);
		m.addAttribute("user", user);

	
	}
	
	@RequestMapping("/index")
	public String dashboard(Model m,Principal p) {
		
		//System.out.println(p.getName());
		//get user using username
		addCommonData(m, p);
		return "/normal/user_dashboard";
	}
	
	// add form handler
	@RequestMapping("/addcontact")
	public String addContact(Model m,Principal p)
	{
		m.addAttribute("title", "Add Contact");
		addCommonData(m, p);
		m.addAttribute("contact", new Contact());

		return "normal/addcontacts";
	}
	
	//proccessing contact form
	@RequestMapping(value = "/process-contact",method = RequestMethod.POST)
	public String processContact(@ModelAttribute("contact")Contact contact,Model m, Principal p, @RequestParam("profile-image")MultipartFile file,HttpSession session)
	{
		try {
			String name=p.getName();
			User user = this.userRepository.getUserByUserName(name);
			addCommonData(m,p);

			//processing and upoading image file
			if(file.isEmpty())
			{

			}
			else
			{
				contact.setImageUrl(file.getOriginalFilename());
				File f=new ClassPathResource("static/images").getFile();
			    Path pt=Paths.get(f.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(),pt,StandardCopyOption.REPLACE_EXISTING);
			}
			
			//message success
			session.setAttribute("message", new Message("Successfully Added", "success"));
			contact.setUser(user);
			user.getContacts().add(contact);
			this.userRepository.save(user);
			
			
			
		} catch (Exception e) {
			//message failure
			
			session.setAttribute("message", new Message("Something went Wrong", "danger"));
			e.printStackTrace();
			}
		return "normal/addcontacts";
	}
	
	//show contact handler 
	//per page = 5
	//current page =0[curpage]
	@RequestMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page")Integer page,Model m,Principal p)
	{ 
		addCommonData(m,p);
		m.addAttribute("title", "Contacts");
		/*
		 * //sending list of contacts 
		 * String name=p.getName(); 
		 * User user=this.userRepository.getUserByUserName(name); 
		 * List<Contact> list=user.getContacts();
		 */
		
		String name=p.getName(); 
		User user=this.userRepository.getUserByUserName(name);
		Pageable pg=PageRequest.of(page,2);
		Page<Contact> list=this.contactRepository.getContactByUserID(user.getId(),pg);
		m.addAttribute("contacts", list);
		m.addAttribute("curpage", page);
		m.addAttribute("totalpage", list.getTotalPages());
		return "normal/showcontacts";
				
	}
	
	//show particular contact details
	@RequestMapping("/contact/{cid}")
	public String showcontactDetails(@PathVariable("cid") Integer cid,Model m,Principal p)
	{
		addCommonData(m,p);
		String name=p.getName();
		User user=this.userRepository.getUserByUserName(name);
		Optional<Contact> contactOptional = this.contactRepository.findById(cid);
		Contact contact=contactOptional.get();
		
		if(user.getId()==contact.getUser().getId())
		{
			m.addAttribute("contact", contact);			
		}
		return "normal/contact_detail";
	}
	
	//delete a contact handler
	@GetMapping("/delete/{cid}")
	public String deleteContact(Model m,Principal p,@PathVariable("cid")Integer cid,HttpSession session)
	{
		addCommonData(m, p);
		String name=p.getName();
		User user=this.userRepository.getUserByUserName(name);
		Optional<Contact> contactOptional = this.contactRepository.findById(cid);
		Contact contact=contactOptional.get();
		
		if(user.getId()==contact.getUser().getId())
		{
			user.getContacts().remove(contact);
			this.userRepository.save(user);
		session.setAttribute("message", new Message("Contact deleted Successfully", "success"));
		return "redirect:/user/show-contacts/0";
		}
		return "";
	}
	
	//update contact
	@PostMapping("/open-contact/{cid}")
	public String updateContact(Model m,Principal p,@PathVariable("cid")Integer cid)
	{
		addCommonData(m, p);
		String name=p.getName();
		User user=this.userRepository.getUserByUserName(name);
		Optional<Contact> contactOptional = this.contactRepository.findById(cid);
		Contact contact=contactOptional.get();
		m.addAttribute("contact", contact);
		return "normal/updateform";
		
	}
	
	//update handler
	@RequestMapping(value="/process-update",method = RequestMethod.POST)
	public String updateHadler(Model m,Principal p,@ModelAttribute Contact contact, @RequestParam("profile-image")MultipartFile file,HttpSession session)
	{
		addCommonData(m, p);
		try {
			//old contact detail
			Contact contactold =this.contactRepository.findById(contact.getCid()).get();
			if(!file.isEmpty())
			{
				//file work
				//delete old photo
				File df=new ClassPathResource("static/images").getFile();
				File f1=new File(df,contactold.getImageUrl());
				f1.delete();
				//update new photo
				contact.setImageUrl(file.getOriginalFilename());
				File f=new ClassPathResource("static/images").getFile();
			    Path pt=Paths.get(f.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(),pt,StandardCopyOption.REPLACE_EXISTING);
			}
			else
			{
				contact.setImageUrl(contactold.getImageUrl());
			}
			String name=p.getName();
			User user=this.userRepository.getUserByUserName(name);
			contact.setUser(user);
			this.contactRepository.save(contact);
			session.setAttribute("message", new Message("Contact Updated Successfully", "success"));
		}
		
		catch (Exception e) {
			// TODO: handle exception
		}		return "redirect:/user/contact/"+contact.getCid();		
	}
	
	//user profile handler
	@GetMapping("/profile")
	public String userProfile(Model m , Principal p)
	{
		addCommonData(m, p);
		m.addAttribute("title", "Profile");
		return "normal/userprofile";
	}
	
	
	//open setting handler
	@GetMapping("/settings")
	public String openSettings(Model m,Principal p)
	{
		addCommonData(m, p);
		return "normal/usersettings";
	}
	
	//change password handler
	
	@PostMapping("/change-password")
	public String changePassword(Model m,Principal p, @RequestParam("oldpass")String oldpass,@RequestParam("newpass")String newpass,HttpSession session)
	{
		addCommonData(m, p);
//		System.out.println("old : "+oldpass);
//		System.out.println("new : "+newpass);
		User user = this.userRepository.getUserByUserName(p.getName());
		if(this.bCryptPasswordEncoder.matches(oldpass,user.getPassword()))
		{
			//chane the password
			user.setPassword(this.bCryptPasswordEncoder.encode(newpass));
			this.userRepository.save(user);
			session.setAttribute("message", new Message("PasswordUpdated Successfully", "success"));
			return "redirect:/user/index";
		}
		else
		{
			session.setAttribute("message", new Message("Password Failed to Update", "danger"));
			return "normal/usersettings";


		}
	}
	
}

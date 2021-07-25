package SmartContactManager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import SmartContactManager.dao.UserRepository;
import SmartContactManager.entities.User;

public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository ur;
	@Override
	public UserDetails loadUserByUsername(String uname) throws UsernameNotFoundException {
		
		User user = ur.getUserByUserName(uname);
		if(user==null)
		{
			throw new UsernameNotFoundException("No User Existed with this email");
		}
		CustomUserDetails cu=new CustomUserDetails(user);
		return cu;
	}

}

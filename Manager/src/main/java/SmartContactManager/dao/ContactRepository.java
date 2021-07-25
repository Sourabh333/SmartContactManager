package SmartContactManager.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import SmartContactManager.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact,Integer>{

	//Pagination pass two values  no values per pages and current page.
	@Query("from Contact as c where c.user.id = :userId")
	public Page<Contact> getContactByUserID(@Param("userId")int userId,Pageable pegable);
	
}

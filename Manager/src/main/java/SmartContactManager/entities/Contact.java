package SmartContactManager.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ManyToAny;

@Entity
@Table(name = "CONTACT")
public class Contact {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cid;
	private String name;
	private String secname;
	private String email;
	private String work;
	private String phone;
	private String imageUrl;
	@Column(length = 5000)
	private String description;
	@ManyToOne
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Contact(int cid, String name, String secname, String email, String work, String phone, String imageUrl,
			String description) {
		super();
		this.cid = cid;
		this.name = name;
		this.secname = secname;
		this.email = email;
		this.work = work;
		this.phone = phone;
		this.imageUrl = imageUrl;
		this.description = description;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecname() {
		return secname;
	}

	public void setSecname(String secname) {
		this.secname = secname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

//	@Override
//	public String toString() {
//		return "Contact [cid=" + cid + ", name=" + name + ", secname=" + secname + ", email=" + email + ", work=" + work
//				+ ", phone=" + phone + ", imageUrl=" + imageUrl + ", description=" + description + ", user=" + user
//				+ "]";
//	}
//	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.cid == ((Contact)obj).getCid();
	}

}

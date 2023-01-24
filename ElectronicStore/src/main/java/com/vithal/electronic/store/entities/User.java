package com.vithal.electronic.store.entities;

import lombok.*;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails{

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userId;

    @Column(name = "user_name")
    @ApiModelProperty(value = "UserName",name = "username",required = true)
    private String name;

    @Column(name = "user_email", unique = true)
    private String email;

    @Column(name = "user_password", length = 500)
    private String password;

    private String gender;

    @Column(length = 1000)
    private String about;

    @Column(name = "user_image_name")
    private String imageName;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<Order> orders=new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Role> roles=new HashSet<>();
    
    @OneToOne(mappedBy = "user",cascade = CascadeType.REMOVE)
    private Cart cart;
    
    //security related Very importants..
    
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Set<SimpleGrantedAuthority> set = roles.stream().map(role-> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toSet());

return set;
	}

	@Override
	public String getUsername() {
	
		return this.email;
	}
	@Override
	public String getPassword() {
	
		return this.password;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}


}

package project.finalproject1backend.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project.finalproject1backend.domain.AttachmentFile;
import project.finalproject1backend.domain.Cart;
import project.finalproject1backend.domain.Inquiry.BuyInquiry;
import project.finalproject1backend.domain.Inquiry.SaleInquiry;
import project.finalproject1backend.domain.UserRole;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Builder
@Data

public class PrincipalDTO implements UserDetails {
    private Long id;
    private String userId;
    private String password;
    private String ownerName;
    private String companyName;
    private LocalDate openingDate;
    private String corporateNumber;
    private List<AttachmentFile> businessLicense;
    private String managerName;
    private String email;
    private String phoneNumber;
    private List<Cart> carts;
    private List<BuyInquiry> buyInquiry;
    private List<SaleInquiry> saleInquiry;
    private Set<UserRole> role ;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Collection<? extends GrantedAuthority> authorities;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return  true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

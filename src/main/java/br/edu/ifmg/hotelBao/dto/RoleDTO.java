package br.edu.ifmg.hotelBao.dto;


import br.edu.ifmg.hotelBao.entitie.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoleDTO {
    private Long id;
    private String authority;




    public RoleDTO(Role role) {
        this.id = role.getId();
        this.authority = role.getAuthority();
    }
}

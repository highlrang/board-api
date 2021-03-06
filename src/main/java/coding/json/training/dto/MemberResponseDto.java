package coding.json.training.dto;

import coding.json.training.domain.Member;
import coding.json.training.domain.dept.Department;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@NoArgsConstructor
@Slf4j
public class MemberResponseDto {

    private Long id;
    private String name;
    private String email;
    private Department department;

    @Builder
    public MemberResponseDto(Member entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.department = entity.getDepartment();
    }
}


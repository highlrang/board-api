package coding.json.training.domain.dept;

import coding.json.training.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

@Entity
@DiscriminatorValue("post_admin")
@NoArgsConstructor
@DynamicInsert
@Getter
public class PostAdmin extends Department{

    @Enumerated(EnumType.STRING)
    private Category category;

    @Builder
    public PostAdmin(Category category){
        this.category = category;
    }
}

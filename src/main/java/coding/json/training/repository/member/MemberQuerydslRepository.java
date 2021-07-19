package coding.json.training.repository.member;

import coding.json.training.domain.Member;
import coding.json.training.domain.dept.Category;
import coding.json.training.domain.dept.Department;
import coding.json.training.dto.BestPostAdminDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static coding.json.training.domain.QMember.member;
import static coding.json.training.domain.dept.QDepartment.department;
import static coding.json.training.domain.dept.QPostAdmin.postAdmin;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
@RequiredArgsConstructor
public class MemberQuerydslRepository { // extends QuerydslRepositorySupport

    private final JPAQueryFactory queryFactory;

    public List<Member> findByName(String name){
        return queryFactory
                .selectFrom(member)
                .where(member.name.eq(name))
                .fetch(); // fetchOne() fetchCount()
        // .orderBy(member.id.desc())
        // groupBy having join
        // select avg sum max min
        // paging >> offset, limit


    }

    public List<BestPostAdminDto> findBestPostAdmins(Category category, Integer degree){

        return queryFactory.select(Projections.constructor(BestPostAdminDto.class, member.id, member.name, postAdmin.category, postAdmin.resolutionDegree))
                .from(member)
                .innerJoin(postAdmin) // innerJoin 교집합 출력, leftJoin은 상대 데이터가 null이더라도 자기 데이터 모두 출력
                .on(member.department.id.eq(postAdmin.id))
                .where(postAdmin.category.eq(category), postAdmin.resolutionDegree.eq(degree))
                .fetch();
    }

    /*
    0. fetchJoin
    List<Member> fetch = queryFactory.selectFrom(member).innerJoin(member.department).fetchJoin().fetch();

    1.
    List<Department> departments = queryFactory.selectFrom(department) // department.* member.* 조회됨(department.member로 join해야함)
            .leftJoin(department.members, member) // , 다음은 alias
            .fetchJoin()
            .fetch(); // 이후에 원하는 dto로 변형

    2.
    Map<Department, List<Member>> transForm = queryFactory.from(department)
            .leftJoin(department.members, member)
            .transform(groupBy(department).as(list(member))); // 이후에 원하는 dto로 변형

            // .as(new Dto(department.col, member.col)) 가능? 생성자에 @QueryProjection
            // .as(map(col, col)) .asMultimap(map(col, col))
            // .as(sum()) .as(max(()) .as(Projections.constructor())



    // oneToOne 관계도 innerJoin 필요



    #. group by 사용 시
    // index 컬럼으로 group by하면 정렬 없이 정렬,
    // index 아닌 컬럼으로 group by 하는 경우는 order by null asc 사용해서 정렬 없이 정렬
    ##. querydsl에서는 새로 생성
    // public class OrderByNull extends OrderSpecifier {
    //     public static final OrderByNull DEFAULT = new OrderByNull();
    //
    //     private OrderByNull() {
    //         super(Order.ASC, NullExpression.DEFAULT, NullHandling.Default);
    //     }
    // }


    count 보다 exists의 성능이 더 좋음 >> exists 없기에 fetchFirst()으로 우회해서 사용(== limit 1)

     */
}
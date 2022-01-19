
package start.openApi.openapi.xml;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "weatherDB")
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DB {

    @Id
    private int id;
    private String baseDate;
    private String baseTime;
    private String category;
    private int nx;
    private int ny;
    private String obsrValue;

//    public DB(int id,String baseDate, String baseTime, String category, int nx, int ny, String obsrValue) {
//        this.id = id;
//        this.baseDate = baseDate;
//        this.baseTime = baseTime;
//        this.category = category;
//        this.nx = nx;
//        this.ny = ny;
//        this.obsrValue = obsrValue;
//    }

}

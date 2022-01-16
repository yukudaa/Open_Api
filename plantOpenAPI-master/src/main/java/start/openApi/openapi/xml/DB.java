package start.openApi.openapi.xml;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class DB {
    private String baseDate;
    private String baseTime;
    @Id
    private String category;
    private int nx;
    private int ny;
    private String obsrValue;

    public DB(String baseDate, String baseTime, String category, int nx, int ny, String obsrValue) {
        this.baseDate = baseDate;
        this.baseTime = baseTime;
        this.category = category;
        this.nx = nx;
        this.ny = ny;
        this.obsrValue = obsrValue;
    }
}

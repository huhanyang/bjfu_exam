package exam.request;

import lombok.Data;

@Data
public class BasePageListRequest {
    private int page;
    private int size;
}

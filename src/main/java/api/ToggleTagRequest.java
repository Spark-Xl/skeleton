package api;

import org.hibernate.validator.constraints.NotEmpty;

public class ToggleTagRequest {
    @NotEmpty
    public Integer id;
}

package info.colinhan.sisyphus.jacal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormItem {
    private String name;
    private FormItemType type;
    private String title;
}

package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

/**
 * This is an API Object.  Its purpose is to model the JSON API that we expose.
 * This class is NOT used for storing in the Database.
 *
 * This ReceiptResponse in particular is the model of a Receipt that we expose to users of our API
 *
 * Any properties that you want exposed when this class is translated to JSON must be
 * annotated with {@link JsonProperty}
 */
public class ReceiptResponse {
    @JsonProperty
    Integer id;

    @JsonProperty
    String merchantName;

    @JsonProperty
    BigDecimal value;

    @JsonProperty
    Timestamp created;

    @JsonProperty
    String [] tags;

    public ReceiptResponse(ReceiptsRecord dbRecord) {
        this.merchantName = dbRecord.getMerchant();
        this.value = dbRecord.getAmount();
        this.created = dbRecord.getUploaded();
        this.id = dbRecord.getId();
    }

    public void setTags(List<TagsRecord> tags) {
        ArrayList<String> tagsArray = new ArrayList<>();
        for (TagsRecord tag : tags) {
            tagsArray.add(tag.getTag());
        }
        this.tags = tagsArray.toArray(new String[0]);
    }
}
